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

		DecimalFormat formatterValor = new DecimalFormat("###,###,###,###,### euros");

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
		String buttonOferta = "Enviar Oferta";

		if (jugador.getMiOferta() != -1) {
			buttonOferta = "Cambiar Oferta";
		}

		builder.setTitle(jugador.getApodo()).setPositiveButton(buttonOferta, this)
				.setNegativeButton("Pagar Cláusula (" + formatterValor.format(2 * jugador.getValor()) + ")", this)
				.setNeutralButton("Eliminar Oferta", this).setView(v);
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
			hacerOferta(dialog);
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			pagarClausula(dialog);
			break;

		case DialogInterface.BUTTON_NEUTRAL:
			eliminarOferta(dialog);
			break;

		default:
			break;
		}
	}

	private void pagarClausula(DialogInterface dialog) {
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
				fragment.gestionaWS(response, jugador.getId(), 0);
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

	private void eliminarOferta(DialogInterface dialog) {
		JSONObject json = new JSONObject();
		try {
			json.put("delflag" + jugador.getIdMercado(), 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/create_ofertas").append("?auth_token=").append(
				DatosUsuario.getToken());
		// Request a string response
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				fragment.gestionaWS(response, jugador.getId(), -1);
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

	private void hacerOferta(DialogInterface dialog) {
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
						fragment.gestionaWS(response, jugador.getId(), cantidadOferta);
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
}