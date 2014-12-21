package app.android.desafiofutbol;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import app.android.desafiofutbol.clases.Equipo;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.Usuario;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;

public class MisEquiposActivity extends Activity {
	
	ListView listViewEquipos = null;
	Context c = null;
	
	ArrayList<Equipo> listaEquipos = null;
	ArrayList<String> listaJugadores = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.c = this;
		setContentView(R.layout.lista_equipos);	
		listViewEquipos = (ListView) findViewById(R.id.listViewEquipos);		
		listViewEquipos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Usuario.setIdEquipoSeleccionado(listaEquipos.get(position).getId());
				Intent i = new Intent(MisEquiposActivity.this, MainActivity.class);			
	            startActivityForResult(i, 1);				
			}
		});
		SQLiteDesafioFutbol admin = new SQLiteDesafioFutbol(this);
        admin.deleteJugador(); 
		GetDesafioFutbol get = new GetDesafioFutbol("ligas/misligas", this, null, null, "misEquiposSW");
        get.execute();
	}
	
	public void misEquiposSW(String json){
		
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(json);
			int length = jsonArray.length();
			if (jsonArray != null) {
				listaEquipos = new ArrayList<Equipo>(length);
				
				for(int i=0; i<length;i++){
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
				GetDesafioFutbol get = new GetDesafioFutbol("clasificacion", this, null, null, "parsePuntosJson");
		        get.execute();
			}			
		} catch (JSONException e) {
			//Get of team score					
		}
	}
	
	public void parsePuntosJson (String json){		
		JSONArray jsonArray = null;
		try{
			jsonArray = new JSONArray(json);
			int length = jsonArray.length();
			if (jsonArray != null) {				
				for(int i=0; i<length;i++){					
				
					JSONObject equipoJson = (JSONObject) jsonArray.get(i);
					int sizeEquipos = listaEquipos.size();
					for (int j=0; j<sizeEquipos;j++){						
						if (equipoJson.getInt("id") == listaEquipos.get(j).getId()){
							listaEquipos.get(j).setPuntos(equipoJson.getInt("puntos"));
						}
					}				
				}
			    int sizeEquipos = listaEquipos.size();
			    for(int i=0; i<sizeEquipos;i++){
			    	HashMap<String, String> paramMap = new HashMap<String, String>();
			    	paramMap.put("idEquipo", String.valueOf(listaEquipos.get(i).getId()));
			    	GetDesafioFutbol get= new GetDesafioFutbol("selecciones/plantilla/"+listaEquipos.get(i).getId(), this, paramMap, null, "parseJugadoresJson");
			    	get.execute();
			    }
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void parseJugadoresJson (String json){
		
		String idEquipo = json.substring(0,json.indexOf("{"));
		json = json.substring(json.indexOf("{"));
		
		ArrayList<Jugador> porteros = new ArrayList<Jugador>();
		ArrayList<Jugador> defensas = new ArrayList<Jugador>();
		ArrayList<Jugador> medios = new ArrayList<Jugador>();
		ArrayList<Jugador> delanteros = new ArrayList<Jugador>();
		JSONObject respJSON;
		try {
			respJSON = new JSONObject(json);
			JSONArray jsonArrayPorteros = respJSON.getJSONArray("porteros");			
			JSONArray jsonArrayDefensas = respJSON.getJSONArray("defensas");
			JSONArray jsonArrayMedios = respJSON.getJSONArray("medios");
			JSONArray jsonArrayDelanteros = respJSON.getJSONArray("delanteros");
			
			porteros = getJugadoresFromJson(jsonArrayPorteros, idEquipo);
			defensas = getJugadoresFromJson(jsonArrayDefensas, idEquipo);
			medios = getJugadoresFromJson(jsonArrayMedios, idEquipo);
			delanteros = getJugadoresFromJson(jsonArrayDelanteros, idEquipo);
			
			porteros.addAll(defensas);
			porteros.addAll(medios);
			porteros.addAll(delanteros);
			
			for (int i=0;i<listaEquipos.size();i++){
				if (listaEquipos.get(i).getId() == Integer.parseInt(idEquipo)){
					listaEquipos.get(i).setJugadores(porteros);
				}
			}
			EquiposAdapter adapter = new EquiposAdapter(c, listaEquipos);
		    listViewEquipos.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
			
			SQLiteDesafioFutbol admin = new SQLiteDesafioFutbol(this);
	        admin.saveJugador(jugador);
	        jugadores.add(jugador);
		}
		return jugadores;
	}
}