package app.android.desafiofutbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Equipo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

public class MisEquiposActivity extends Activity {

	private ListView listViewEquipos = null;

	private ArrayList<Equipo> listaEquipos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.lista_equipos);
		listViewEquipos = (ListView) findViewById(R.id.listViewEquipos);
		listViewEquipos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DatosUsuario.setIdEquipoSeleccionado(listaEquipos.get(position)
						.getId());
				DatosUsuario.setNombreEquipo(listaEquipos.get(position)
						.getNombre());
				Intent i = new Intent(MisEquiposActivity.this,
						MainActivity.class);
				startActivityForResult(i, 1);
			}
		});

		StringBuffer url = new StringBuffer(
				"http://www.desafiofutbol.com/ligas/misligas").append("?")
				.append("auth_token").append("=")
				.append(DatosUsuario.getToken());

		// Request a string response
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
				url.toString(), null, new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray json) {
						ligasMisligasSW(json.toString());

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
		Volley.newRequestQueue(this).add(request);
	}

	public void printData(ArrayList<Equipo> equipos) {
		EquiposAdapter adapter = new EquiposAdapter(this, listaEquipos);
		listViewEquipos.setAdapter(adapter);
	}

	public void ligasMisligasSW(String json) {

		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(json);
			int length = jsonArray.length();
			if (jsonArray != null) {
				listaEquipos = new ArrayList<Equipo>(length);

				for (int i = 0; i < length; i++) {
					JSONObject equipoJson = (JSONObject) jsonArray.get(i);

					Equipo equipo = new Equipo();
					equipo.setNombre(equipoJson.getString("nombre"));
					equipo.setId(equipoJson.getInt("id"));
					equipo.setActiva(equipoJson.getBoolean("activa"));
					equipo.setSaldo((equipoJson.getInt("saldo")));
					equipo.setValor(equipoJson.getDouble("valor"));

					JSONObject ligaJson = equipoJson.getJSONObject("liga");
					equipo.setLigaNombre(ligaJson.getString("nombre"));
					equipo.setLigaId(ligaJson.getInt("id"));
					listaEquipos.add(equipo);
				}
			}
			printData(listaEquipos);
		} catch (JSONException e) {
			// Get of team score
		}
	}
}