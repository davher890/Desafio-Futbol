package app.android.desafiofutbol.entrenadores;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import app.android.desafiofutbol.GetDesafioFutbol;
import app.android.desafiofutbol.MainActivity;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.Entrenador;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;

public class FragmentEntrenadores extends Fragment {
			
	LayoutInflater inflater;
	private ListView listViewEntrenadores = null;
	private EntrenadoresAdapter adapter  = null;
	
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
		View rootView = inflater.inflate(R.layout.fragment_entrenadores, container,false);
		this.inflater = inflater;
		
		listViewEntrenadores = (ListView) rootView.findViewById(R.id.listViewEntrenadores);
		
		listViewEntrenadores.setOnItemClickListener(new OnItemClickListener() {	   
        	
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Entrenador entrenador = adapter.getItem(position);
	    		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.dialog_entrenador, null);
				
		        EntrenadorDialogFragment alert = new EntrenadorDialogFragment(entrenador, FragmentEntrenadores.this);
		        final AlertDialog createDialogLugar = alert.createDialogLugar(getActivity(), v);
		        createDialogLugar.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);			
				createDialogLugar.show();
				final Button okButton = createDialogLugar.getButton(Dialog.BUTTON_POSITIVE);
		        okButton.setTypeface(null, Typeface.BOLD);
			}
		});
		
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
		adapter = new EntrenadoresAdapter(getActivity(), listaEntrenadores);
		listViewEntrenadores.setAdapter(adapter);		
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