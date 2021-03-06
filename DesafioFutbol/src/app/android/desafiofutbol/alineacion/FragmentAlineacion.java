package app.android.desafiofutbol.alineacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import app.android.desafiofutbol.Constants;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

public class FragmentAlineacion extends Fragment implements OnDragListener, OnLongClickListener {

	private ListView porterosTitList = null;
	private ListView defensasTitList = null;
	private ListView mediosTitList = null;
	private ListView delanterosTitList = null;

	private ListView porterosSupList = null;
	private ListView defensasSupList = null;
	private ListView mediosSupList = null;
	private ListView delanterosSupList = null;

	private Button guardar = null;
	private ArrayList<Jugador> jugadores = null;
	private SQLiteDesafioFutbol admin = null;

	private AlineacionAdapter porterosTitAdapter;
	private AlineacionAdapter defensasTitAdapter;
	private AlineacionAdapter mediosTitAdapter;
	private AlineacionAdapter delanterosTitAdapter;
	private AlineacionAdapter porterosSupAdapter;
	private AlineacionAdapter defensasSupAdapter;
	private AlineacionAdapter mediosSupAdapter;
	private AlineacionAdapter delanterosSupAdapter;

	private EquipoAlineacion equipo = null;
	JSONObject json = null;
	ArrayList<Integer> idArray = null;
	private Activity activity = null;
	private int capitan;

	public static FragmentAlineacion newInstance(Activity activity) {

		FragmentAlineacion fragment = new FragmentAlineacion(activity);
		return fragment;
	}

	public FragmentAlineacion(Activity activity) {
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_alineacion, container, false);

		porterosTitList = (ListView) rootView.findViewById(R.id.listViewPortTit);
		defensasTitList = (ListView) rootView.findViewById(R.id.listViewDefTit);
		mediosTitList = (ListView) rootView.findViewById(R.id.listViewMedTit);
		delanterosTitList = (ListView) rootView.findViewById(R.id.listViewDelTit);

		porterosSupList = (ListView) rootView.findViewById(R.id.listViewPorSup);
		defensasSupList = (ListView) rootView.findViewById(R.id.listViewDefSup);
		mediosSupList = (ListView) rootView.findViewById(R.id.listViewMedSup);
		delanterosSupList = (ListView) rootView.findViewById(R.id.listViewDelSup);

		guardar = (Button) rootView.findViewById(R.id.buttonGuardarAli);

		admin = new SQLiteDesafioFutbol(activity);
		// Obtiene los datos de los jugadores para el id de equipo seleccionado
		jugadores = admin.getJugadores();

		if (jugadores == null || jugadores.size() == 0) {

			JSONObject json = new JSONObject();
			try {
				json.put("idEquipo", String.valueOf(DatosUsuario.getIdEquipoSeleccionado()));
				// json.put("change_sele",
				// String.valueOf(DatosUsuario.getIdEquipoSeleccionado()));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/selecciones/plantilla/").append(DatosUsuario.getIdEquipoSeleccionado())
					.append("?change_sele=").append(DatosUsuario.getIdEquipoSeleccionado()).append("&auth_token=").append(DatosUsuario.getToken());

			// Request a string response
			JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), json, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					seleccionesPlantillaWS(response);
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
			;
			// Add the request to the queue
			VolleyRequest.getInstance(activity).addToRequestQueue(request);

		} else {
			equipo = new EquipoAlineacion(jugadores);
			setData();
		}
		return rootView;
	}

	public void seleccionesPlantillaWS(final JSONObject respJSON) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONArray jsonArrayPorteros = respJSON.getJSONArray(Posicion.porteros.name());
					JSONArray jsonArrayDefensas = respJSON.getJSONArray(Posicion.defensas.name());
					JSONArray jsonArrayMedios = respJSON.getJSONArray(Posicion.medios.name());
					JSONArray jsonArrayDelanteros = respJSON.getJSONArray(Posicion.delanteros.name());

					jugadores = getJugadoresFromJson(jsonArrayPorteros);
					jugadores.addAll(getJugadoresFromJson(jsonArrayDefensas));
					jugadores.addAll(getJugadoresFromJson(jsonArrayMedios));
					jugadores.addAll(getJugadoresFromJson(jsonArrayDelanteros));

					Thread thread = new Thread() {
						public void run() {
							admin.saveJugadores(FragmentAlineacion.this.jugadores);
						}
					};
					thread.start();

					equipo = new EquipoAlineacion(jugadores);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setData();
			}
		});
	}

	public ArrayList<Jugador> getJugadoresFromJson(JSONArray arrayJugador) throws JSONException {

		final ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
		int lengthArray = arrayJugador.length();
		for (int i = 0; i < lengthArray; i++) {

			JSONObject jugadorJson = (JSONObject) arrayJugador.get(i);
			Jugador jugador = new Jugador();
			jugador.setId(jugadorJson.getInt("id"));
			jugador.setApodo(jugadorJson.getString("apodo"));
			jugador.setNombre(jugadorJson.getString("nombre"));
			jugador.setApellidos(jugadorJson.getString("apellidos"));
			jugador.setValor(jugadorJson.getDouble("precio"));
			jugador.setPuntos(jugadorJson.getInt("puntos"));
			jugador.setEquipo(jugadorJson.getString("equipo_nombre"));
			jugador.setPosicion(jugadorJson.getString("posicion"));
			jugador.setUrlImagen(jugadorJson.getString("foto"));
			Boolean titular = jugadorJson.getBoolean("titular");

			if (jugadorJson.getBoolean("capitan")) {
				this.capitan = jugador.getId();
			}
			if (titular)
				jugador.setTitular(1);
			else
				jugador.setTitular(0);
			jugadores.add(jugador);
		}
		return jugadores;
	}

	private void setData() {

		porterosTitAdapter = new AlineacionAdapter(activity, equipo.getPorterosTitulares(), this, Posicion.Portero);
		porterosTitList.setAdapter(porterosTitAdapter);

		defensasTitAdapter = new AlineacionAdapter(activity, equipo.getDefensasTitulares(), this, Posicion.Defensa);
		defensasTitList.setAdapter(defensasTitAdapter);

		mediosTitAdapter = new AlineacionAdapter(activity, equipo.getMediosTitulares(), this, Posicion.Medio);
		mediosTitList.setAdapter(mediosTitAdapter);

		delanterosTitAdapter = new AlineacionAdapter(activity, equipo.getDelanterosTitulares(), this, Posicion.Delantero);
		delanterosTitList.setAdapter(delanterosTitAdapter);

		porterosSupAdapter = new AlineacionAdapter(activity, equipo.getPorterosSuplentes(), this, Posicion.Portero);
		porterosSupList.setAdapter(porterosSupAdapter);

		defensasSupAdapter = new AlineacionAdapter(activity, equipo.getDefensasSuplentes(), this, Posicion.Defensa);
		defensasSupList.setAdapter(defensasSupAdapter);

		mediosSupAdapter = new AlineacionAdapter(activity, equipo.getMediosSuplentes(), this, Posicion.Medio);
		mediosSupList.setAdapter(mediosSupAdapter);

		delanterosSupAdapter = new AlineacionAdapter(activity, equipo.getDelanterosSuplentes(), this, Posicion.Delantero);
		delanterosSupList.setAdapter(delanterosSupAdapter);

		guardar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				guardar.setEnabled(false);

				ArrayList<Jugador> porterosAli = porterosTitAdapter.getJugadores();
				ArrayList<Jugador> defensasAli = defensasTitAdapter.getJugadores();
				ArrayList<Jugador> mediosAli = mediosTitAdapter.getJugadores();
				ArrayList<Jugador> delanterosAli = delanterosTitAdapter.getJugadores();

				// int sizePorteros = porterosAli.size();
				int sizeDefensas = defensasAli.size();
				int sizeMedios = mediosAli.size();
				int sizeDelanteros = delanterosAli.size();

				// if (sizePorteros == 1
				// && ((sizeDefensas == 4 && sizeMedios == 4 && sizeDelanteros
				// == 2)
				// || (sizeDefensas == 4 && sizeMedios == 3 && sizeDelanteros ==
				// 3)
				// || (sizeDefensas == 4 && sizeMedios == 5 && sizeDelanteros ==
				// 1)
				// || (sizeDefensas == 5 && sizeMedios == 4 && sizeDelanteros ==
				// 1)
				// || (sizeDefensas == 3 && sizeMedios == 4 && sizeDelanteros ==
				// 3) || (sizeDefensas == 3 && sizeMedios == 5 && sizeDelanteros
				// == 2))) {

				// Guardar en base de datos y WebService
				StringBuffer tactica = new StringBuffer();
				tactica.append(String.valueOf(sizeDefensas)).append("_");
				tactica.append(String.valueOf(sizeMedios)).append("_");
				tactica.append(String.valueOf(sizeDelanteros));

				json = new JSONObject();
				idArray = new ArrayList<Integer>();

				ArrayList<Jugador> jugadoresAli = new ArrayList<Jugador>();
				jugadoresAli.addAll(porterosAli);
				jugadoresAli.addAll(defensasAli);
				jugadoresAli.addAll(mediosAli);
				jugadoresAli.addAll(delanterosAli);

				for (Jugador jugador : jugadoresAli) {

					if (jugador.getId() != -1) {
						idArray.add(jugador.getId());
					}
				}
				// PutDesafioFutbol putTactica = new PutDesafioFutbol(
				// "selecciones/cambiar_tactica/" + tactica.toString()
				// + "/"
				// + DatosUsuario.getIdEquipoSeleccionado(),
				// FragmentAlineacion.this, json, "gestionaAlineacion");
				// putTactica.execute();

				StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/selecciones/cambiar_tactica/").append(tactica.toString())
						.append("/").append(DatosUsuario.getIdEquipoSeleccionado()).append("?").append("auth_token").append("=")
						.append(DatosUsuario.getToken());
				// Request a string response
				StringRequest request = new StringRequest(Request.Method.PUT, url.toString(), new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						gestionaAlineacion(response);
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

				// } else {
				// AlertDialog.Builder builder = new
				// AlertDialog.Builder(activity);
				// builder.setTitle("Alineacion incorrecta")
				// .setMessage("Posibles alineaciones:" + "\n 4-4-2" +
				// "\n 4-3-3" + "\n 4-5-1" + "\n 5-4-1" + "\n 3-4-3" +
				// "\n 3-5-2")
				// .setCancelable(false).setNegativeButton("Close", new
				// DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog, int id) {
				// dialog.cancel();
				// }
				// });
				// AlertDialog alert = builder.create();
				// alert.show();
				// }
			}
		});
	}

	private boolean dropEventNotHandled(DragEvent dragEvent) {
		return !dragEvent.getResult();
	}

	private void checkForValidMove(ListView listViewDrag, ListView listViewView, View dragView) {
		cambiaJugador((AlineacionAdapter) listViewDrag.getAdapter(), (AlineacionAdapter) listViewView.getAdapter(),
				listViewDrag.getPositionForView(dragView));
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		// Dragged image
		View dragView = (View) event.getLocalState();

		// Drag action
		int dragAction = event.getAction();

		ListView listViewDrag = (ListView) dragView.getParent();
		if (listViewDrag == null) {
			return true;
		}

		if (dragAction == DragEvent.ACTION_DRAG_ENDED) {
			// Dragged image
			if (dropEventNotHandled(event)) {
				dragView.setVisibility(View.VISIBLE);
			}
		} else if (dragAction == DragEvent.ACTION_DROP) {
			ListView listViewView = (ListView) v.getParent();
			checkForValidMove(listViewDrag, listViewView, dragView);
			dragView.setVisibility(View.VISIBLE);
		}
		return true;
	}

	private void cambiaJugador(AlineacionAdapter side1, AlineacionAdapter side2, int position) {

		if (!side1.getPosicion().equals(side2.getPosicion())) {
			return;
		}

		ArrayList<Jugador> arraySide1 = side1.getJugadores();
		Jugador jugador = arraySide1.get(position);

		if (jugador.getApodo() != null) {
			side1.removeJugador(position);
			if (side1.getCount() == 0) {
				side1.addJugador(new Jugador());
			}

			if (side2.getJugadores().get(0).getApodo() == null) {
				side2.removeJugador(0);
			}
			side2.addJugador(jugador);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		ClipData clipData = ClipData.newPlainText("", "");
		View.DragShadowBuilder dsb = new View.DragShadowBuilder(v);
		v.startDrag(clipData, dsb, v, 0);
		v.setVisibility(View.INVISIBLE);
		return true;
	}

	public void gestionaAlineacion(String result) {

		try {
			json.put("id_titulares", idArray.toString());
			json.put("id_seleccion", DatosUsuario.getIdEquipoSeleccionado());
			json.put("id_capitan", capitan);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// PutDesafioFutbol post = new PutDesafioFutbol(
		// "selecciones/save_once_titular/", FragmentAlineacion.this,
		// json, "updateBBDD");
		// post.execute();

		StringBuffer url = new StringBuffer("http://www.desafiofutbol.com/selecciones/save_once_titular").append("?auth_token=").append(
				DatosUsuario.getToken());
		// Request a string response
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url.toString(), json, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				updateBBDD(response);
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
	}

	public void gestionaWS(JSONArray resultJson, Jugador jugador, String accion) {

		try {
			String tipoMensaje = ((JSONArray) resultJson.get(0)).getString(0);
			String mensaje = null;
			if (tipoMensaje.equals("notice")) {
				// Actualizar base de datos y refrescar tabla

				// Venta express
				if (accion.equals(Constants.VENTA_EXPRES)) {
					mensaje = ((JSONArray) resultJson.get(0)).getString(1);
					admin.deleteJugador(jugador.getId());
				}
				// Elimina del mercado
				else if (accion.equals(Constants.QUITAR_DEL_MERCADO)) {
					mensaje = "Oferta eliminada";
					admin.deleteFichaje(jugador.getId());
				}
				// Cambia/Crea oferta
				else if (accion.equals(Constants.PONER_A_LA_VENTA)) {
					mensaje = ((JSONArray) resultJson.get(0)).getString(1);
					admin.createFichaje(jugador);
				}

				equipo = new EquipoAlineacion(admin.getJugadores());
				setData();
			}
			Toast.makeText(this.activity, mensaje, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateBBDD(JSONArray response) {

		guardar.setEnabled(true);

		// Update ddbb
		JSONArray res = response;
	}

	public void gestionaWS(JSONObject json2, Jugador jugador, String ponerALaVenta) {
		// TODO Auto-generated method stub
		System.out.println();
	}

}