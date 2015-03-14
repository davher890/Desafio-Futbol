package app.android.desafiofutbol;

import java.util.ArrayList;

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
import app.android.desafiofutbol.clases.Equipo;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;

public class MisEquiposActivity extends Activity {
	
	private ListView listViewEquipos = null;
	
	private ArrayList<Equipo> listaEquipos = null;	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final SQLiteDesafioFutbol admin = new SQLiteDesafioFutbol(this);
		Thread thread = new Thread(){
        	public void run(){
        		
        		admin.cleanDatabase();
        	}
        };
        thread.start();
		
		setContentView(R.layout.lista_equipos);	
		listViewEquipos = (ListView) findViewById(R.id.listViewEquipos);		
		listViewEquipos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub				
				DatosUsuario.setIdEquipoSeleccionado(listaEquipos.get(position).getId());
				DatosUsuario.setNombreEquipo(listaEquipos.get(position).getNombre());
				Intent i = new Intent(MisEquiposActivity.this, MainActivity.class);			
	            startActivityForResult(i, 1);				
			}
		});
        GetDesafioFutbol get = new GetDesafioFutbol("ligas/misligas", this, null, null, "ligasMisligasSW");
    	get.execute();
	}
	
	public void printData(ArrayList<Equipo> equipos){
		EquiposAdapter adapter = new EquiposAdapter(this, listaEquipos);
	    listViewEquipos.setAdapter(adapter);
	}

	public void ligasMisligasSW(String json){
		
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
			}
			printData(listaEquipos);
		} catch (JSONException e) {
			//Get of team score					
		}
	}
}