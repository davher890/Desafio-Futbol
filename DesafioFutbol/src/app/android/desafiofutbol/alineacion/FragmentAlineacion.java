package app.android.desafiofutbol.alineacion;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.android.desafiofutbol.MainActivity;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.ManageResources;
import app.android.desafiofutbol.clases.Usuario;
import app.android.desafiofutbol.ddbb.SQLiteDesafioFutbol;

public class FragmentAlineacion extends Fragment {
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	
	public static FragmentAlineacion newInstance() {
		FragmentAlineacion fragment = new FragmentAlineacion();
		return fragment;		
	}
	
	public FragmentAlineacion() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_alineacion, container,false);
		
		LinearLayout porterosTit = (LinearLayout) rootView.findViewById(R.id.layoutPorterosTit);
    	LinearLayout defensasTit = (LinearLayout) rootView.findViewById(R.id.layoutDefensasTit);
    	LinearLayout mediosTit = (LinearLayout) rootView.findViewById(R.id.layoutMediosTit);
    	LinearLayout delanterosTit = (LinearLayout) rootView.findViewById(R.id.layoutDelanterosTit);
    	
    	LinearLayout porterosSup = (LinearLayout) rootView.findViewById(R.id.layoutPorterosSup);
    	LinearLayout defensasSup = (LinearLayout) rootView.findViewById(R.id.layoutDefensasSup);
    	LinearLayout mediosSup = (LinearLayout) rootView.findViewById(R.id.layoutMediosSup);    	
    	LinearLayout delanterosSup = (LinearLayout) rootView.findViewById(R.id.layoutDelanterosSup);
    	
    	SQLiteDesafioFutbol admin = new SQLiteDesafioFutbol(getActivity());
    	
    	ArrayList<Jugador> titulares = admin.getTitulares(Usuario.getIdEquipoSeleccionado());
    	ArrayList<Jugador> suplentes = admin.getSuplentes(Usuario.getIdEquipoSeleccionado());
    	
    	int size = titulares.size();
    	TextView jug = null;
    	TextView imgEquipo = null;
    	
    	for(int i=0;i<size;i++){
    		Jugador jugador = titulares.get(i);
    		jug = new TextView(getActivity());    		
    		imgEquipo = new TextView(getActivity());
    		imgEquipo.setBackgroundResource(new ManageResources().getImageFromString(jugador.getEquipo()));
    		imgEquipo.setWidth(20);
    		imgEquipo.setHeight(20);
			jug.setText(jugador.getApodo());
			if (jugador.getPosicion().equals("Portero")){
				porterosTit.addView(imgEquipo);
				porterosTit.addView(jug);
			}
			else if (jugador.getPosicion().equals("Defensa")){
				defensasTit.addView(imgEquipo);
				defensasTit.addView(jug);				
			}
			else if (jugador.getPosicion().equals("Medio")){
				mediosTit.addView(imgEquipo);
				mediosTit.addView(jug);				
			}
			else if (jugador.getPosicion().equals("Delantero")){
				delanterosTit.addView(imgEquipo);
				delanterosTit.addView(jug);				
			}
    	}
    	size = suplentes.size();
    	for(int i=0;i<size;i++){
    		Jugador jugador = suplentes.get(i);
    		jug = new TextView(getActivity());
    		imgEquipo = new TextView(getActivity());
    		imgEquipo.setBackgroundResource(new ManageResources().getImageFromString(jugador.getEquipo()));
    		imgEquipo.setWidth(20);
    		imgEquipo.setHeight(20);
			jug.setText(jugador.getApodo());
			if (jugador.getPosicion().equals("Portero")){
				porterosSup.addView(imgEquipo);
				porterosSup.addView(jug);
			}
			else if (jugador.getPosicion().equals("Defensa")){
				defensasSup.addView(imgEquipo);
				defensasSup.addView(jug);				
			}
			else if (jugador.getPosicion().equals("Medio")){
				mediosSup.addView(imgEquipo);
				mediosSup.addView(jug);				
			}
			else if (jugador.getPosicion().equals("Delantero")){
				delanterosSup.addView(imgEquipo);
				delanterosSup.addView(jug);				
			}
    	}
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(1);
	}

}
