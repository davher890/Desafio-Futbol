package app.android.desafiofutbol.alineacion;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

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
import android.widget.ImageView;
import android.widget.TextView;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

//Define a DialogFragment that displays the error dialog
public class JugadorDialogFragment extends DialogFragment {

	// Global field to contain the error dialog
	private Dialog mDialog;
	private Jugador jugador = null;

	private FragmentAlineacion fragment;

	private TextView nombre;
	private TextView equipo;
	private TextView posicion;
	private TextView puntos;
	private TextView valor;
	private ImageView imageJugador;

	// Default constructor. Sets the dialog field to null
	public JugadorDialogFragment(Jugador jugador, FragmentAlineacion fragment, int id) {
		super();
		this.jugador = jugador;
		this.fragment = fragment;
		mDialog = null;
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

	public AlertDialog createDialogJugador(final Context contexto, final View v) {

		nombre = (TextView) v.findViewById(R.id.DialogJugadorNombreApellidos);
		equipo = (TextView) v.findViewById(R.id.DialogJugadorEquipo);
		posicion = (TextView) v.findViewById(R.id.DialogJugadorPosicion);
		puntos = (TextView) v.findViewById(R.id.DialogJugadorPuntos);
		valor = (TextView) v.findViewById(R.id.DialogJugadorValor);
		imageJugador = (ImageView) v.findViewById(R.id.imageViewDialogJugador);

		nombre.setText(jugador.getNombre() + " " + jugador.getApellidos());

		StringBuffer nombreEquipo = new StringBuffer(jugador.getEquipo().substring(0, 1).toUpperCase()).append(jugador.getEquipo().substring(1));

		equipo.setText(nombreEquipo.toString().replaceAll("-", " "));
		posicion.setText(jugador.getPosicion());
		puntos.setText(String.valueOf(jugador.getPuntos()));

		DecimalFormat formatterSalario = new DecimalFormat("###,###,###,###,### euros");
		valor.setText(String.valueOf(formatterSalario.format(jugador.getValor())));

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
		builder.setTitle(jugador.getApodo()).setPositiveButton("Poner a la Venta", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				JSONObject json = new JSONObject();
				try {
					json.put("jugador_id", JugadorDialogFragment.this.jugador.getId());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado").append("?auth_token=").append(DatosUsuario.getToken());

				// Request a string response
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject json) {
						gestionaWS(json);

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
		}).setNegativeButton("Venta Expres", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				JSONObject json = new JSONObject();
				try {
					json.put("jugador", JugadorDialogFragment.this.jugador.getId());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/0/clausula_pagar").append("?auth_token=").append(
						DatosUsuario.getToken());

				// Request a string response
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject json) {
						gestionaWS(json);

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
		}).setView(v);
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

	public void gestionaWS(JSONObject resultJson) {

		System.out.println();

		// try {
		// String tipoMensaje = ((JSONArray) resultJson.get(0)).getString(0);
		// String mensaje = null;
		// if (tipoMensaje.equals("notice")) {
		// // Actualizar base de datos y refrescar tabla
		//
		// // Clausula Pagada
		// if (oferta == 0) {
		// mensaje = ((JSONArray) resultJson.get(0)).getString(1);
		// admin.deleteFichaje(idFichaje);
		// }
		// // Elimina oferta
		// else if (oferta == -1) {
		// mensaje = "Oferta eliminada";
		// admin.updateFichaje(idFichaje, oferta);
		// }
		// // Cambia/Crea oferta
		// else {
		// mensaje = ((JSONArray) resultJson.get(0)).getString(1);
		// admin.updateFichaje(idFichaje, oferta);
		// }
		//
		// fichajes = admin.getFichajes();
		// setData(fichajes, SortTypes.puntos.name());
		// }
		// Toast.makeText(this.activity, mensaje, Toast.LENGTH_LONG).show();
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

	}
}