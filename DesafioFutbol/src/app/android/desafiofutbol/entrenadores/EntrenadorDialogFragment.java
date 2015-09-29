package app.android.desafiofutbol.entrenadores;

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
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Entrenador;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

//Define a DialogFragment that displays the error dialog
public class EntrenadorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	// Global field to contain the error dialog
	private Dialog mDialog;
	private Entrenador entrenador = null;

	private TextView salario;
	private TextView puntos;
	private TextView numPartidos;
	private Spinner listPartidos;
	String opcionButton;

	private FragmentEntrenadores fragment;

	// private ImageView imageFichaje;

	// Default constructor. Sets the dialog field to null
	public EntrenadorDialogFragment(Entrenador entrenador, FragmentEntrenadores fragment) {
		super();
		this.entrenador = entrenador;
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

	public AlertDialog createDialogLugar(final Context contexto, final View v) {

		salario = (TextView) v.findViewById(R.id.DialogJugadorSalario);
		puntos = (TextView) v.findViewById(R.id.DialogEntPuntos);
		listPartidos = (Spinner) v.findViewById(R.id.spinnerPartidosEnt);
		numPartidos = (TextView) v.findViewById(R.id.textViewDialogEntPartidos);

		puntos.setText(String.valueOf(entrenador.getPuntos()));

		DecimalFormat formatterSalario = new DecimalFormat("###,###,###,###,### euros");
		salario.setText(String.valueOf(formatterSalario.format(entrenador.getSalario())) + "/partido");

		if (entrenador.getPropietario().equals(DatosUsuario.getNombreEquipo())) {
			opcionButton = "Despedir";
			numPartidos.setVisibility(View.INVISIBLE);
			listPartidos.setVisibility(View.INVISIBLE);
		} else {
			opcionButton = "Contratar";
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(contexto, R.array.num_partidos, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			listPartidos.setAdapter(adapter);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

		builder.setTitle(entrenador.getNombre()).setPositiveButton(opcionButton, this).setView(v);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		gestionaEntrenador(dialog, opcionButton);
	}

	private void gestionaEntrenador(DialogInterface dialog, String opcionButton) {

		if (opcionButton.equals("Despedir")) {

			JSONObject json = new JSONObject();
			try {
				json.put("delflag" + entrenador.getId(), "1");
				json.put("id", entrenador.getId());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/entrenadores/").append(entrenador.getId()).append("/contrato")
					.append("?auth_token=").append(DatosUsuario.getToken());
			// Request a string response
			JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONArray>() {
				@Override
				public void onResponse(JSONArray response) {
					fragment.actualizaEntrenador(response, entrenador.getId(), "null");
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

		} else {
			String num = listPartidos.getSelectedItem().toString();
			final int numPartidos = Integer.parseInt(num);
			JSONObject json = new JSONObject();
			try {
				json.put("jornadas", numPartidos);
				json.put("id", entrenador.getId());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/entrenadores/").append(entrenador.getId()).append("/contrato")
					.append("?auth_token=").append(DatosUsuario.getToken());
			// Request a string response
			JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url.toString(), json, new Response.Listener<JSONArray>() {
				@Override
				public void onResponse(JSONArray response) {
					fragment.actualizaEntrenador(response, entrenador.getId(), DatosUsuario.getNombreEquipo());
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
		}
		dialog.cancel();
	}
}