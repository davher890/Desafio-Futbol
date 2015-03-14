package app.android.desafiofutbol.clasificacion;

import java.util.ArrayList;

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
import app.android.desafiofutbol.GetDesafioFutbol;
import app.android.desafiofutbol.MainActivity;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;

public class FragmentClasificacion extends Fragment {
	
	private ListView listViewClasificacion = null;
	private SQLiteDesafioFutbol admin = null;
	private ArrayList<UsuarioClasificacion> listaClasificacion = null;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	
	public static FragmentClasificacion newInstance() {
		FragmentClasificacion fragment = new FragmentClasificacion();
		return fragment;		
	}
	
	public FragmentClasificacion() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_clasificacion, container,false);
		listViewClasificacion = (ListView) rootView.findViewById(R.id.listViewEquipos);		
	
		admin = new SQLiteDesafioFutbol(getActivity());
    	//Obtiene los datos de la clasficicacion
    	listaClasificacion = admin.getClasificacion();
    	
    	if (listaClasificacion == null || listaClasificacion.size() == 0){
    		GetDesafioFutbol get = new GetDesafioFutbol("clasificacion", getActivity(), null, this, "parseClasificacionJson");
            get.execute();
    	}
    	else{
    		setData();
    	}
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(5);
	}
	
	public void parseClasificacionJson (String json){
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(json);
			int length = jsonArray.length();
			if (jsonArray != null) {
				listaClasificacion = new ArrayList<UsuarioClasificacion>(length);
				
				for(int i=0; i<length;i++){
					JSONObject usuarioJson = (JSONObject) jsonArray.get(i);
					
					UsuarioClasificacion usuarioClasficicacion = new UsuarioClasificacion();
					usuarioClasficicacion.setId(usuarioJson.getInt("id"));
					usuarioClasficicacion.setNombre(usuarioJson.getString("nombre"));
					usuarioClasficicacion.setPuntos(usuarioJson.getInt("puntos"));
					usuarioClasficicacion.setUsuario(usuarioJson.getString("usuario"));
					usuarioClasficicacion.setValor(usuarioJson.getDouble("valor"));
					
					JSONObject jornadasJson = usuarioJson.getJSONObject("jornadas");
					JSONArray names = jornadasJson.names();
					int ultimaJornada = -1;
					
					if (names != null){
						int lengthJornadas = names.length();
						for (int k=0;k<lengthJornadas;k++){
												
							String jornada = (String) names.get(k);
							int index = jornada.indexOf("_");
							int jornadaAux = Integer.parseInt(jornada.substring(index+1));
							if (jornadaAux > ultimaJornada)
								ultimaJornada = jornadaAux;
						}
					}
					if (ultimaJornada != -1){
						//while (ultimaJornada )
						int puntosUltimaJornada = 0;
						while (puntosUltimaJornada == 0 && ultimaJornada > 0){
							if (!jornadasJson.getString("jornada_"+ultimaJornada).equals("null")){
								puntosUltimaJornada = jornadasJson.getInt("jornada_"+ultimaJornada);
							}
							ultimaJornada--;
						}
						ultimaJornada = puntosUltimaJornada;
						
					}
					else{
						ultimaJornada = 0;
					}
					usuarioClasficicacion.setUltimaJornada(ultimaJornada);
					listaClasificacion.add(usuarioClasficicacion);				
				}
				Thread thread = new Thread(){
		        	public void run(){
		    	        admin.saveClasificacion(FragmentClasificacion.this.listaClasificacion);
		        }};
		        thread.start();
		        setData();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setData(){
		ClasificacionAdapter adapter = new ClasificacionAdapter(getActivity(), listaClasificacion);
	    listViewClasificacion.setAdapter(adapter);
	}
}
