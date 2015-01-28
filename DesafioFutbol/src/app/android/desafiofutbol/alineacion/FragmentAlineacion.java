package app.android.desafiofutbol.alineacion;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import app.android.desafiofutbol.AlineacionAdapter;
import app.android.desafiofutbol.GetDesafioFutbol;
import app.android.desafiofutbol.MainActivity;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.Usuario;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;

public class FragmentAlineacion extends Fragment {
	
	private ListView porterosTitList = null;
	private ListView defensasTitList = null;
	private ListView mediosTitList = null;
	private ListView delanterosTitList = null;    	
	
	private ListView porterosSupList = null;
	private ListView defensasSupList = null;
	private ListView mediosSupList = null;    	
	private ListView delanterosSupList = null;
	
	private Button guardar = null;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	private SQLiteDesafioFutbol admin = null;
	
	public static FragmentAlineacion newInstance() {		
		FragmentAlineacion fragment = new FragmentAlineacion();
		return fragment;		
	}
	
	public FragmentAlineacion() {
		admin = new SQLiteDesafioFutbol(getActivity());
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_alineacion, container,false);
		
		porterosTitList = (ListView) rootView.findViewById(R.id.listViewPortTit);
		defensasTitList = (ListView) rootView.findViewById(R.id.listViewDefTit);
    	mediosTitList = (ListView) rootView.findViewById(R.id.listViewMedTit);
    	delanterosTitList = (ListView) rootView.findViewById(R.id.listViewDelTit);    	
    	
    	porterosSupList = (ListView) rootView.findViewById(R.id.listViewPorSup);
    	defensasSupList = (ListView) rootView.findViewById(R.id.listViewDefSup);
    	mediosSupList = (ListView) rootView.findViewById(R.id.listViewMedSup);    	
    	delanterosSupList = (ListView) rootView.findViewById(R.id.listViewDelSup);
    	
    	guardar = (Button)rootView.findViewById(R.id.buttonGuardarAli);
    	
    	//if (admin.getTitulares(Usuario.getIdEquipoSeleccionado(), "Portero");
    	
		HashMap<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("idEquipo", String.valueOf(Usuario.getIdEquipoSeleccionado()));
    	GetDesafioFutbol get= new GetDesafioFutbol("selecciones/plantilla/"+Usuario.getIdEquipoSeleccionado(), getActivity(), paramMap, this, "seleccionesPlantillaWS");
    	get.execute();
    	setData();    	
    	
		return rootView;
	}

	private void setData() {
		SQLiteDesafioFutbol admin = new SQLiteDesafioFutbol(getActivity());
    	
    	ArrayList<Jugador> jugadores = admin.getTitulares(Usuario.getIdEquipoSeleccionado(), "Portero");
	    final AlineacionAdapter porterosTitAdapter = new AlineacionAdapter(getActivity(), jugadores);
		porterosTitList.setAdapter(porterosTitAdapter);
	    
		jugadores = admin.getTitulares(Usuario.getIdEquipoSeleccionado(), "Defensa");
		final AlineacionAdapter defensasTitAdapter = new AlineacionAdapter(getActivity(), jugadores);
		defensasTitList.setAdapter(defensasTitAdapter);
	    
		jugadores = admin.getTitulares(Usuario.getIdEquipoSeleccionado(), "Medio");
		final AlineacionAdapter mediosTitAdapter = new AlineacionAdapter(getActivity(), jugadores);
		mediosTitList.setAdapter(mediosTitAdapter);
	    
		jugadores = admin.getTitulares(Usuario.getIdEquipoSeleccionado(), "Delantero");
		final AlineacionAdapter delanterosTitAdapter = new AlineacionAdapter(getActivity(), jugadores);
		delanterosTitList.setAdapter(delanterosTitAdapter);
	    
		jugadores = admin.getSuplentes(Usuario.getIdEquipoSeleccionado(), "Portero");
		final AlineacionAdapter porterosSupAdapter = new AlineacionAdapter(getActivity(), jugadores);
		porterosSupList.setAdapter(porterosSupAdapter);
	    
		jugadores = admin.getSuplentes(Usuario.getIdEquipoSeleccionado(), "Defensa");
		final AlineacionAdapter defensasSupAdapter = new AlineacionAdapter(getActivity(), jugadores);
		defensasSupList.setAdapter(defensasSupAdapter);
	    
		jugadores = admin.getSuplentes(Usuario.getIdEquipoSeleccionado(), "Medio");
		final AlineacionAdapter mediosSupAdapter = new AlineacionAdapter(getActivity(), jugadores);
		mediosSupList.setAdapter(mediosSupAdapter);
	    
		jugadores = admin.getSuplentes(Usuario.getIdEquipoSeleccionado(), "Delantero");
		final AlineacionAdapter delanterosSupAdapter = new AlineacionAdapter(getActivity(), jugadores);
		delanterosSupList.setAdapter(delanterosSupAdapter);
	    	
		setItemListener(porterosTitList, porterosTitAdapter, porterosSupAdapter);
		setItemListener(porterosSupList, porterosSupAdapter, porterosTitAdapter );
		
		setItemListener(defensasTitList, defensasTitAdapter, defensasSupAdapter);
		setItemListener(defensasSupList, defensasSupAdapter, defensasTitAdapter);
		
		setItemListener(mediosTitList, mediosTitAdapter, mediosSupAdapter);
		setItemListener(mediosSupList, mediosSupAdapter, mediosTitAdapter);
		
		setItemListener(delanterosTitList, delanterosTitAdapter, delanterosSupAdapter);
		setItemListener(delanterosSupList, delanterosSupAdapter, delanterosTitAdapter);
		
		guardar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int sizePorteros = porterosTitAdapter.getJugadores().size();
				int sizeDefensas = defensasTitAdapter.getJugadores().size();
				int sizeMedios = mediosTitAdapter.getJugadores().size();
				int sizeDelanteros = delanterosTitAdapter.getJugadores().size();
				
				
				if (sizePorteros == 1 &&
					((sizeDefensas == 4 && sizeMedios == 4 && sizeDelanteros == 2) ||
					(sizeDefensas == 4 && sizeMedios == 3 && sizeDelanteros == 3) ||
					(sizeDefensas == 4 && sizeMedios == 5 && sizeDelanteros == 1) ||
					(sizeDefensas == 5 && sizeMedios == 4 && sizeDelanteros == 1) ||
					(sizeDefensas == 3 && sizeMedios == 4 && sizeDelanteros == 3) ||
					(sizeDefensas == 3 && sizeMedios == 5 && sizeDelanteros == 2))){
						
					//Guardar en base de datos y WebService					
				
				}
				else{					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			        builder.setTitle("Alineacion incorrecta").
			        setMessage("Posibles alineaciones:"
							+ "\n 4-4-2"
							+ "\n 4-3-3"
							+ "\n 4-5-1"
							+ "\n 5-4-1"
							+ "\n 3-4-3"
							+ "\n 3-5-2")
			        .setCancelable(false)
			        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			            }
			        });
			        AlertDialog alert = builder.create();
			        alert.show();
				}
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(1);
	}
	
	private void cambiaJugador(AlineacionAdapter side1, AlineacionAdapter side2, int position){
		
		ArrayList<Jugador> arraySide1 = side1.getJugadores();
		Jugador jugador = arraySide1.get(position);
		side1.removeJugador(position);
		
		side2.addJugador(jugador);
	}
	
	private void setItemListener(ListView listview, final AlineacionAdapter adapter1, final AlineacionAdapter adapter2){
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				cambiaJugador(adapter1, adapter2, position);				
			}
		});		
	}
	
public void seleccionesPlantillaWS (String json){
		
		String idEquipo = json.substring(0,json.indexOf("{"));
		json = json.substring(json.indexOf("{"));

		JSONObject respJSON;
		try {
			respJSON = new JSONObject(json);
			JSONArray jsonArrayPorteros = respJSON.getJSONArray("porteros");			
			JSONArray jsonArrayDefensas = respJSON.getJSONArray("defensas");
			JSONArray jsonArrayMedios = respJSON.getJSONArray("medios");
			JSONArray jsonArrayDelanteros = respJSON.getJSONArray("delanteros");
			
			getJugadoresFromJson(jsonArrayPorteros, idEquipo);
			getJugadoresFromJson(jsonArrayDefensas, idEquipo);
			getJugadoresFromJson(jsonArrayMedios, idEquipo);
			getJugadoresFromJson(jsonArrayDelanteros, idEquipo);

			//printData(listaEquipos);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setData();
	}
	public ArrayList<Jugador> getJugadoresFromJson(JSONArray arrayJugador, String idMiLiga) throws JSONException{
		
		ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
		int lengthArray = arrayJugador.length();
		for (int i=0;i<lengthArray; i++){
			
			JSONObject jugadorJson = (JSONObject) arrayJugador.get(i);
			Jugador jugador = new Jugador();
			jugador.setIdMiLiga(Integer.parseInt(idMiLiga));
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
			if (titular)
				jugador.setTitular(1);
			else 
				jugador.setTitular(0);
			
	        admin.saveJugador(jugador);
	        jugadores.add(jugador);
		}
		return jugadores;
	}
}