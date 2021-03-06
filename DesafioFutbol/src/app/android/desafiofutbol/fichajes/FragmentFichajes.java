package app.android.desafiofutbol.fichajes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import app.android.desafiofutbol.Constants;
import app.android.desafiofutbol.JugadoresComparator;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.ManageResources;
import app.android.desafiofutbol.clases.SortTypes;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

public class FragmentFichajes extends Fragment {

	private ListView listViewFichajes = null;
	private FichajesAdapter adapter = null;
	private SQLiteDesafioFutbol admin = null;
	private ArrayList<Jugador> fichajes = null;
	private Activity activity = null;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 * 
	 * @param mainActivity
	 */

	public static FragmentFichajes newInstance(Activity activity) {
		FragmentFichajes fragment = new FragmentFichajes(activity);
		return fragment;
	}

	public FragmentFichajes(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.menu_fichajes, menu);
		super.onCreateOptionsMenu(menu, inflater);
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		String orden = (String) item.getTitle();

		if (orden.equals("Ordenar")) {
			return false;
		}

		if (adapter != null) {
			setData(adapter.getListaFichajes(), orden.toLowerCase());
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_fichajes, container, false);
		listViewFichajes = (ListView) rootView.findViewById(R.id.listViewFichajes);

		listViewFichajes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Jugador jugador = adapter.getItem(position);

				LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.dialog_fichaje, null);

				FichajeDialogFragment alert = new FichajeDialogFragment();
				AlertDialog createDialogLugar = alert.createDialogLugar(activity, v, jugador, FragmentFichajes.this);
				createDialogLugar.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				createDialogLugar.show();
			}
		});

		admin = new SQLiteDesafioFutbol(activity);
		// Obtiene los datos de los jugadores para el id de equipo seleccionado
		fichajes = admin.getFichajes();

		if (fichajes == null || fichajes.size() == 0) {

			StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/mercado/").append(DatosUsuario.getIdEquipoSeleccionado())
					.append("/index?auth_token=").append(DatosUsuario.getToken());
			// Request a string response
			JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url.toString(), new JSONObject(), new Response.Listener<JSONArray>() {
				@Override
				public void onResponse(JSONArray response) {
					parseFichajesJson(response);
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
			setData(fichajes, SortTypes.puntos.name());
		}
		return rootView;
	}

	public void parseFichajesJson(JSONArray jsonArray) {
		try {
			int length = jsonArray.length();
			if (jsonArray != null) {
				final ArrayList<Jugador> listaFichajes = new ArrayList<Jugador>(length);

				for (int i = 0; i < length; i++) {
					JSONObject usuarioJson = (JSONObject) jsonArray.get(i);

					Jugador jugador = new Jugador();
					jugador.setId(usuarioJson.getInt("id_jugador"));
					jugador.setApodo(usuarioJson.getString("apodo"));
					jugador.setValor(usuarioJson.getDouble("valor"));
					jugador.setPuntos(usuarioJson.getInt("puntos"));
					jugador.setPropietarioNombre(usuarioJson.getString("propietario"));
					jugador.setPropietarioId(usuarioJson.getString("propietario_id").equals("null") ? -1 : usuarioJson.getInt("propietario_id"));
					jugador.setEquipo(usuarioJson.getString("equipo"));
					jugador.setPosicion(usuarioJson.getString("posicion"));
					jugador.setUrlImagen(usuarioJson.getString("url_imagen"));
					if (usuarioJson.getString("mioferta").equals("null")) {
						jugador.setMiOferta(-1);
					} else {
						JSONObject miOferta = usuarioJson.getJSONObject("mioferta");
						jugador.setMiOferta(miOferta.getInt("valor"));
					}
					jugador.setDrawableEquipo(ManageResources.getDrawableFromString(jugador.getEquipo()));
					JSONObject idMercado = usuarioJson.getJSONObject("id_mercado");
					String stringIdMercado = idMercado.getString("$oid");
					if (stringIdMercado == null || stringIdMercado.equals("") || stringIdMercado.equals("null")) {
						System.out.println("Id mercado null");
					}
					jugador.setIdMercado(stringIdMercado);

					listaFichajes.add(jugador);
				}
				Thread thread = new Thread() {
					public void run() {
						admin.saveFichajes(listaFichajes);
					}
				};
				thread.start();
				setData(listaFichajes, SortTypes.puntos.name());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void gestionaWS(JSONArray resultJson, Jugador jugador, int oferta, String accion) {

		try {
			String tipoMensaje = ((JSONArray) resultJson.get(0)).getString(0);
			String mensaje = null;
			if (tipoMensaje.equals("notice")) {
				// Actualizar base de datos y refrescar tabla

				// Clausula Pagada
				if (accion == Constants.PAGAR_CLÁUSULA) {
					mensaje = ((JSONArray) resultJson.get(0)).getString(1);
					admin.deleteFichaje(jugador.getId());
				}
				// Elimina oferta
				else if (accion == Constants.ELIMINAR_OFERTA) {
					mensaje = ((JSONArray) resultJson.get(0)).getString(1);
					admin.updateFichaje(jugador.getId(), oferta);
				}
				// Cambia/Crea oferta
				else if (accion == Constants.ENVIAR_OFERTA || accion == Constants.CAMBIAR_OFERTA) {
					mensaje = ((JSONArray) resultJson.get(0)).getString(1);
					admin.updateFichaje(jugador.getId(), oferta);
				}
				// Venta expres
				else if (accion == Constants.VENTA_EXPRES) {
					mensaje = ((JSONArray) resultJson.get(0)).getString(1);
					admin.deleteJugador(jugador.getId());
					admin.deleteFichaje(jugador.getId());
				}
				fichajes = admin.getFichajes();
				setData(fichajes, SortTypes.puntos.name());
			} else if (tipoMensaje.equals("error")) {
				mensaje = ((JSONArray) resultJson.get(0)).getString(1);
			}
			Toast.makeText(this.activity, mensaje, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setData(final ArrayList<Jugador> listaFichajes, String orden) {

		Collections.sort(listaFichajes, new JugadoresComparator(SortTypes.valueOf(orden)));

		adapter = new FichajesAdapter(activity, listaFichajes);
		listViewFichajes.setAdapter(adapter);
	}

	public void gestionaWS(JSONObject response, Jugador jugador, int oferta, String accion) {
		// TODO Auto-generated method stub
		System.out.println();
	}
}