package app.android.desafiofutbol.usuarios;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import app.android.desafiofutbol.R;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class FragmentUsuario extends Fragment {

	private ListView usuariosList = null;
	private ArrayList<Usuario> seguidores = new ArrayList<Usuario>();
	private ArrayList<Usuario> siguiendos = new ArrayList<Usuario>();
	private String tipoUsuarios;
	private UsuarioAdapter adapter;

	public static FragmentUsuario newInstance() {
		FragmentUsuario fragment = new FragmentUsuario();
		return fragment;
	}

	private FragmentUsuario() {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_usuarios, menu);
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		tipoUsuarios = (String) item.getTitle();

		if (tipoUsuarios.equals("Siguiendo")) {
			item.setTitle("Seguidores");
			setData(siguiendos);
		}
		if (tipoUsuarios.equals("Seguidores")) {
			item.setTitle("Siguiendo");
			setData(seguidores);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_usuarios, container, false);

		usuariosList = (ListView) rootView.findViewById(R.id.listViewUsuarios);

		// GetDesafioFutbol get= new GetDesafioFutbol("usuarios", getActivity(),
		// null, this, "gestionaWS");
		// get.execute();
		String url = "http://www.desafiofutbol.com/usuarios";
		// Request a string response
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				gestionaWS(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Error handling
				System.out.println("Something went wrong!");
				error.printStackTrace();
			}
		});
		// Add the request to the queue
		Volley.newRequestQueue(getActivity()).add(stringRequest);
		return rootView;
	}

	private void setData(final ArrayList<Usuario> listaUsuarios) {
		adapter = new UsuarioAdapter(getActivity(), listaUsuarios);
		usuariosList.setAdapter(adapter);
	}

	public void gestionaWS(String result) {

		try {
			JSONObject json = new JSONObject(result);
			JSONArray jsonSeguidores = json.getJSONArray("seguidores");
			JSONArray jsonSiguiendo = json.getJSONArray("siguiendo");

			int length = jsonSeguidores.length();
			for (int i = 0; i < length; i++) {
				JSONObject usuarioJson = (JSONObject) jsonSeguidores.get(i);

				Usuario seguidor = new Usuario(usuarioJson.getString("username"), usuarioJson.getInt("user_id"), usuarioJson.getString("avatar"),
						usuarioJson.getInt("numequipos"), usuarioJson.getInt("p_rank"));

				seguidores.add(seguidor);
			}

			length = jsonSiguiendo.length();
			for (int i = 0; i < length; i++) {
				JSONObject usuarioJson = (JSONObject) jsonSiguiendo.get(i);

				Usuario siguiendo = new Usuario(usuarioJson.getString("username"), usuarioJson.getInt("user_id"), usuarioJson.getString("avatar"),
						usuarioJson.getInt("numequipos"), usuarioJson.getInt("p_rank"));

				siguiendos.add(siguiendo);
			}

			setData(seguidores);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}