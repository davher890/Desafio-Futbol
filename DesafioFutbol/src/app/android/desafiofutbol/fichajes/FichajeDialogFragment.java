package app.android.desafiofutbol.fichajes;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.android.desafiofutbol.Constants;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

//Define a DialogFragment that displays the error dialog
public class FichajeDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	// Global field to contain the error dialog
	private Dialog mDialog;
	private Jugador jugador = null;

	private TextView equipo;
	private TextView posicion;
	private TextView puntos;
	private TextView valor;
	private TextView propietario;
	private EditText cantidad;
	private FragmentFichajes fragment;
	private ImageView imageJugador;

	// Default constructor. Sets the dialog field to null
	public FichajeDialogFragment() {
		super();
	}

	// Set the dialog to display
	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}

	// Return a Dialog to the DialogFragment.
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public AlertDialog createDialogLugar(final Context contexto, final View v, Jugador jugador, FragmentFichajes fragmentFichajes) {

		this.jugador = jugador;
		this.fragment = fragmentFichajes;

		equipo = (TextView) v.findViewById(R.id.FichajeEquipo);
		posicion = (TextView) v.findViewById(R.id.FichajePosicion);
		puntos = (TextView) v.findViewById(R.id.FichajePuntos);
		valor = (TextView) v.findViewById(R.id.FichajeValor);
		cantidad = (EditText) v.findViewById(R.id.editTextFichajeCantidad);
		imageJugador = (ImageView) v.findViewById(R.id.imageViewFichajeJugador);
		propietario = (TextView) v.findViewById(R.id.FichajePropietario);

		equipo.setText(jugador.getEquipo());
		posicion.setText(jugador.getPosicion());
		puntos.setText(String.valueOf(jugador.getPuntos()));

		DecimalFormat formatterValor = new DecimalFormat("###,###,###,###,### €");

		valor.setText(String.valueOf(formatterValor.format(jugador.getValor())));
		propietario.setText(jugador.getPropietarioNombre().equals("null") ? "" : jugador.getPropietarioNombre());

		if (jugador.getMiOferta() != -1) {
			cantidad.setText(String.valueOf(jugador.getMiOferta()));
		} else {
			cantidad.setText(String.valueOf(jugador.getValor()));
		}

		ImageRequest request = new ImageRequest(jugador.getUrlImagen().replace("small", "medium"), new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				actualizaImagen(response);
			}
		}, 0, 0, null, null, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Error handling
				System.out.println("Something went wrong!");
				error.printStackTrace();
			}
		});
		// Add the request to the queue
		VolleyRequest.getInstance(getActivity()).addToRequestQueue(request);

		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

		if (jugador.getMiOferta() != -1) {
			builder.setTitle(jugador.getApodo()).setPositiveButton(Constants.CAMBIAR_OFERTA, this).setNegativeButton(Constants.ELIMINAR_OFERTA, this)
					.setNeutralButton(Constants.PAGAR_CLÁUSULA + "(" + formatterValor.format(2 * jugador.getValor()) + ")", this).setView(v);
		} else if (jugador.getPropietarioId() == DatosUsuario.getIdEquipoSeleccionado()) {
			builder.setTitle(jugador.getApodo()).setPositiveButton(Constants.VENTA_EXPRES, this).setView(v)
					.setNegativeButton(Constants.QUITAR_DEL_MERCADO, this);
		} else {
			builder.setTitle(jugador.getApodo()).setPositiveButton(Constants.ENVIAR_OFERTA, this)
					.setNeutralButton(Constants.PAGAR_CLÁUSULA + "(" + formatterValor.format(2 * jugador.getValor()) + ")", this).setView(v);
		}
		return builder.create();
	}

	public void actualizaImagen(final Bitmap bm) {
		fragment.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				imageJugador.setImageBitmap(bm);
			}
		});
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			if (jugador.getPropietarioId() == DatosUsuario.getIdEquipoSeleccionado()) {
				ventaExpres(dialog, Constants.VENTA_EXPRES);
			} else {
				// Cambiar oferta y Hacer oferta
				hacerOferta(dialog, Constants.ENVIAR_OFERTA);
			}
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			if (jugador.getPropietarioId() == DatosUsuario.getIdEquipoSeleccionado()) {
				quitarDelMercado(dialog, Constants.QUITAR_DEL_MERCADO);
			} else {
				eliminarOferta(dialog, Constants.ELIMINAR_OFERTA);
			}
			break;

		case DialogInterface.BUTTON_NEUTRAL:
			pagarClausula(dialog, Constants.PAGAR_CLÁUSULA);
			break;

		default:
			break;
		}
	}

	private void quitarDelMercado(DialogInterface dialog, final String accion) {

		StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/").append(jugador.getId()).append("?auth_token=")
				.append(DatosUsuario.getToken());
		// Request a string response
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url.toString(), null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				fragment.gestionaWS(response, jugador, 0, accion);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Error handling
				System.out.println("Something went wrong!");
				error.printStackTrace();
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Accept", "application/json");
				map.put("Content-Type", "application/json");
				map.put("Accept-Charset", "utf-8");

				return map;
			}
		};
		// Add the request to the queue
		VolleyRequest.getInstance(getActivity()).addToRequestQueue(request);

		dialog.cancel();
	}

	private void pagarClausula(DialogInterface dialog, final String accion) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", jugador.getId());
			json.put("oferta", 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/").append(jugador.getIdMercado()).append("/clausula_pagar")
				.append("?auth_token=").append(DatosUsuario.getToken());
		// Request a string response
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				fragment.gestionaWS(response, jugador, 0, accion);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Error handling
				System.out.println("Something went wrong!");
				error.printStackTrace();
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Accept", "application/json");
				map.put("Content-Type", "application/json");
				map.put("Accept-Charset", "utf-8");

				return map;
			}
		};
		// Add the request to the queue
		VolleyRequest.getInstance(getActivity()).addToRequestQueue(request);

		dialog.cancel();
	}

	private void eliminarOferta(DialogInterface dialog, final String accion) {
		JSONObject json = new JSONObject();
		try {
			json.put("oferta_" + jugador.getIdMercado(), "" + jugador.getMiOferta());
			json.put("delflag" + jugador.getIdMercado(), "1");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/create_ofertas").append("?auth_token=").append(
				DatosUsuario.getToken());
		// Request a string response
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				fragment.gestionaWS(response, jugador, -1, accion);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Error handling
				System.out.println("Something went wrong!");
				error.printStackTrace();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Accept", "application/json");
				map.put("Content-Type", "application/json");
				map.put("Accept-Charset", "utf-8");

				return map;
			}
		};
		// Add the request to the queue
		VolleyRequest.getInstance(getActivity()).addToRequestQueue(request);

		dialog.cancel();
	}

	private void hacerOferta(DialogInterface dialog, final String accion) {
		if (!cantidad.getText().toString().equals("")) {
			final int cantidadOferta = Integer.parseInt(cantidad.getText().toString());
			if (cantidadOferta < jugador.getValor()) {
				Toast.makeText(fragment.getActivity(), "El valor mínimo para fichar a " + jugador.getApodo() + " es " + jugador.getValor(),
						Toast.LENGTH_SHORT);
			} else {
				JSONObject json = new JSONObject();
				try {
					json.put("oferta_" + jugador.getIdMercado(), cantidadOferta);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/create_ofertas").append("?auth_token=").append(
						DatosUsuario.getToken());
				// Request a string response
				JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						fragment.gestionaWS(response, jugador, cantidadOferta, accion);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Error handling
						System.out.println("Something went wrong!");
						error.printStackTrace();
					}
				}) {

					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("Accept", "application/json");
						map.put("Content-Type", "application/json");
						map.put("Accept-Charset", "utf-8");

						return map;
					}
				};
				// Add the request to the queue
				VolleyRequest.getInstance(getActivity()).addToRequestQueue(request);

				dialog.cancel();
			}
		}
	}

	private void ventaExpres(DialogInterface dialog, final String accion) {
		JSONObject json = new JSONObject();
		try {
			json.put("jugador", FichajeDialogFragment.this.jugador.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/0/clausula_pagar").append("?jugador=" + jugador.getId())
				.append("auth_token=").append(DatosUsuario.getToken());

		// Request a string response
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url.toString(), new JSONObject(), new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray json) {
				fragment.gestionaWS(json, jugador, 2 * jugador.getValor(), accion);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Accept", "application/json");
				map.put("Content-Type", "application/json");
				map.put("Accept-Charset", "utf-8");

				return map;
			}
		};
		// Add the request to the queue
		VolleyRequest.getInstance(getActivity()).addToRequestQueue(request);

		dialog.cancel();
	}
}