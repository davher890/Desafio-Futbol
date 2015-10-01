package app.android.desafiofutbol.entrenadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Entrenador;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

public class FragmentEntrenadores extends Fragment {

	LayoutInflater inflater;
	private ListView listViewEntrenadores = null;
	private EntrenadoresAdapter adapter = null;

	private SQLiteDesafioFutbol admin = null;

	private ArrayList<Entrenador> listaEntrenadores = null;
	private Activity activity = null;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 * 
	 * @param mainActivity
	 */

	public static FragmentEntrenadores newInstance(Activity activity) {
		FragmentEntrenadores fragment = new FragmentEntrenadores(activity);
		return fragment;
	}

	public FragmentEntrenadores(Activity activity) {
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_entrenadores, container, false);
		this.inflater = inflater;

		listViewEntrenadores = (ListView) rootView.findViewById(R.id.listViewEntrenadores);

		listViewEntrenadores.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Entrenador entrenador = adapter.getItem(position);
				LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.dialog_entrenador, null);

				EntrenadorDialogFragment alert = new EntrenadorDialogFragment(entrenador, FragmentEntrenadores.this);
				final AlertDialog createDialogLugar = alert.createDialogLugar(activity, v);
				createDialogLugar.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				createDialogLugar.show();
				final Button okButton = createDialogLugar.getButton(Dialog.BUTTON_POSITIVE);
				okButton.setTypeface(null, Typeface.BOLD);
			}
		});

		admin = new SQLiteDesafioFutbol(activity);
		// Obtiene los datos de los entrenadores
		listaEntrenadores = admin.getEntrenadores();

		if (listaEntrenadores == null || listaEntrenadores.size() == 0) {

			StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/entrenadores?auth_token=").append(DatosUsuario.getToken());

			// Request a string response
			JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url.toString(), new JSONObject(), new Response.Listener<JSONArray>() {
				@Override
				public void onResponse(JSONArray response) {
					parseEntrenadoresJson(response);
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
			VolleyRequest.getInstance(activity).addToRequestQueue(request);
		} else {
			setData();
		}
		return rootView;
	}

	public void parseEntrenadoresJson(JSONArray response) {
		try {
			int length = response.length();
			if (response != null) {
				listaEntrenadores = new ArrayList<Entrenador>(length);

				for (int i = 0; i < length; i++) {
					JSONObject usuarioJson = (JSONObject) response.get(i);

					Entrenador entrenador = new Entrenador();
					entrenador.setId(usuarioJson.getInt("id_entrenador"));
					entrenador.setNombre(usuarioJson.getString("nombre"));
					entrenador.setEquipo(usuarioJson.getString("equipo"));
					entrenador.setSalario(usuarioJson.getDouble("salario"));
					entrenador.setPuntos(usuarioJson.getDouble("puntos"));
					entrenador.setPropietario(usuarioJson.getString("propietario"));

					listaEntrenadores.add(entrenador);

				}
				Thread thread = new Thread() {
					public void run() {
						admin.saveEntrenadores(listaEntrenadores);
					}
				};
				thread.start();

				setData();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setData() {
		adapter = new EntrenadoresAdapter(activity, listaEntrenadores);
		listViewEntrenadores.setAdapter(adapter);
	}

	public void actualizaEntrenador(JSONArray resultJson, int idEnt, String propietario) {

		try {
			String tipoMensaje = ((JSONArray) resultJson.get(0)).getString(0);
			if (tipoMensaje.equals("notice")) {
				// Actualizar base de datos y refrescar tabla
				admin.updateEntrenador(idEnt, propietario);

				listaEntrenadores = admin.getEntrenadores();
				setData();
			}
			String mensaje = ((JSONArray) resultJson.get(0)).getString(1);
			Toast.makeText(this.activity, mensaje, Toast.LENGTH_LONG).show();
			;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}