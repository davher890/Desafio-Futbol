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
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import app.android.desafiofutbol.GetDesafioFutbol;
import app.android.desafiofutbol.MainActivity;
import app.android.desafiofutbol.R;

public class FragmentEntrenadores extends Fragment {
	
	private ListView listViewEntrenadores;
	
	private View rootView;
	
	LayoutInflater inflater;
	
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
		GetDesafioFutbol get = new GetDesafioFutbol("entrenadores", getActivity(), null, this, "parseEntrenadoresJson");
        get.execute();
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
				
				GridLayout grid = (GridLayout)rootView.findViewById(R.id.gridLayoutEntrenadores);
				grid.setColumnCount(2);
				
				int size = listaEntrenadores.size();
				grid.setRowCount((int)Math.ceil(size/2));

				View child = null;
				for (int i=0;i<size;i++){
					
					child = inflater.inflate(R.layout.entrenador_list_item, null);
					
					Entrenador entrenador = listaEntrenadores.get(i);
					((TextView)child.findViewById(R.id.textViewNombreEnt)).setText(entrenador.getNombre());
					((TextView)child.findViewById(R.id.textViewSalarioEnt)).setText(String.valueOf(entrenador.getSalario()));
					((TextView)child.findViewById(R.id.textViewPuntosEnt)).setText(String.valueOf(entrenador.getPuntos()));
					
					if (entrenador.getPropietario() != null){
						((TextView)child.findViewById(R.id.textViewEnt03)).setText("Contratado por: ");
						((TextView)child.findViewById(R.id.textViewPropietarioEnt)).setText(entrenador.getPropietario());
						
					}
					else{
						((TextView)child.findViewById(R.id.textViewEnt03)).setText("Número Partidos ");
						((TextView)child.findViewById(R.id.textViewPropietarioEnt)).setVisibility(View.INVISIBLE);
					}
				
					
					grid.addView(child);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
