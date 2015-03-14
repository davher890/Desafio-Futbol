package app.android.desafiofutbol.entrenadores;

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
import android.widget.GridView;
import android.widget.Toast;
import app.android.desafiofutbol.GetDesafioFutbol;
import app.android.desafiofutbol.MainActivity;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.Entrenador;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;

public class FragmentEntrenadores extends Fragment {
		
	private View rootView;	
	LayoutInflater inflater;
	private SQLiteDesafioFutbol admin = null;
	
	private ArrayList<Entrenador> listaEntrenadores = null;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	
	public static FragmentEntrenadores newInstance() {
		FragmentEntrenadores fragment = new FragmentEntrenadores();
		return fragment;		
	}
	
	public FragmentEntrenadores() {		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_entrenadores, container,false);
		this.inflater = inflater;
		
		admin = new SQLiteDesafioFutbol(getActivity());
    	//Obtiene los datos de los entrenadores
    	listaEntrenadores = admin.getEntrenadores();
    	
		if (listaEntrenadores == null || listaEntrenadores.size() == 0){
			GetDesafioFutbol get = new GetDesafioFutbol("entrenadores", getActivity(), null, this, "parseEntrenadoresJson");
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
		((MainActivity) activity).onSectionAttached(3);
	}
	
	public void parseEntrenadoresJson (String json){
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(json);
			int length = jsonArray.length();
			if (jsonArray != null) {
				listaEntrenadores = new ArrayList<Entrenador>(length);
				
				for(int i=0; i<length;i++){
					JSONObject usuarioJson = (JSONObject) jsonArray.get(i);
					
					Entrenador entrenador = new Entrenador();
					entrenador.setId(usuarioJson.getInt("id_entrenador"));
					entrenador.setNombre(usuarioJson.getString("nombre"));
					entrenador.setEquipo(usuarioJson.getString("equipo"));
					entrenador.setSalario(usuarioJson.getDouble("salario"));
					entrenador.setPuntos(usuarioJson.getDouble("puntos"));					
					entrenador.setPropietario(usuarioJson.getString("propietario"));
					
					listaEntrenadores.add(entrenador);					
					
				}
				Thread thread = new Thread(){
		        	public void run(){
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
		GridView ll = (GridView) rootView.findViewById(R.id.gridViewEntrenadores);
		ll.setAdapter(new EntrenadoresGridAdapter(this, getActivity(), listaEntrenadores));		
	}
	
	public void actualizaEntrenador(String result, int idEnt){
		
		try {
			JSONArray resultJson;
			resultJson = new JSONArray(result);
			String tipoMensaje = ((JSONArray)resultJson.get(0)).getString(0); 
			if (tipoMensaje.equals("notice")){
				//Actualizar base de datos y refrescar tabla
				String value = null;
				
				admin.updateEntrenador(idEnt, value);
				
				listaEntrenadores = admin.getEntrenadores();
				setData();				
			}
			String mensaje = ((JSONArray)resultJson.get(0)).getString(1);			
			Toast.makeText(this.getActivity(), mensaje, Toast.LENGTH_LONG);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}