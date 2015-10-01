package app.android.desafiofutbol.clasificacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

public class FragmentClasificacion extends Fragment {

	private ListView listViewClasificacion = null;
	private SQLiteDesafioFutbol admin = null;
	private ArrayList<UsuarioClasificacion> listaClasificacion = null;
	private Activity activity = null;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 * 
	 * @param mainActivity
	 */

	public static FragmentClasificacion newInstance(Activity activity) {
		FragmentClasificacion fragment = new FragmentClasificacion(activity);
		return fragment;
	}

	public FragmentClasificacion(Activity activity) {
		this.activity = activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_clasificacion, container, false);
		listViewClasificacion = (ListView) rootView.findViewById(R.id.listViewEquipos);

		admin = new SQLiteDesafioFutbol(activity);
		// Obtiene los datos de la clasficicacion
		listaClasificacion = admin.getClasificacion();

		if (listaClasificacion == null || listaClasificacion.size() == 0) {

			StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/clasificacion").append("?").append("auth_token").append("=")
					.append(DatosUsuario.getToken());

			// Request a string response
			JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url.toString(), new JSONObject(), new Response.Listener<JSONArray>() {
				@Override
				public void onResponse(JSONArray response) {
					parseClasificacionJson(response);
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

	public void parseClasificacionJson(final JSONArray jsonArray) {
		try {
			int length = jsonArray.length();
			if (jsonArray != null) {
				listaClasificacion = new ArrayList<UsuarioClasificacion>(length);

				for (int i = 0; i < length; i++) {
					JSONObject usuarioJson = (JSONObject) jsonArray.get(i);

					UsuarioClasificacion usuarioClasficicacion = new UsuarioClasificacion();
					usuarioClasficicacion.setId(usuarioJson.getInt("id"));
					usuarioClasficicacion.setNombre(usuarioJson.getString("nombre"));
					usuarioClasficicacion.setPuntos(usuarioJson.getInt("puntos"));
					usuarioClasficicacion.setUsuario(usuarioJson.getString("usuario"));
					usuarioClasficicacion.setValor(usuarioJson.getDouble("valor"));
					listaClasificacion.add(usuarioClasficicacion);
				}
				Thread thread = new Thread() {
					public void run() {
						admin.saveClasificacion(FragmentClasificacion.this.listaClasificacion);
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

	public void setData() {
		ClasificacionAdapter adapter = new ClasificacionAdapter(activity, listaClasificacion);
		listViewClasificacion.setAdapter(adapter);
	}
}
