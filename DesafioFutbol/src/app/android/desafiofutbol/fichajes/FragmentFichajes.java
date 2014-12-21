package app.android.desafiofutbol.fichajes;

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
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.ManageResources;
import app.android.desafiofutbol.clases.Usuario;

public class FragmentFichajes extends Fragment {
	
	ListView listViewFichajes = null;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	
	public static FragmentFichajes newInstance() {
		FragmentFichajes fragment = new FragmentFichajes();
		return fragment;		
	}
	
	public FragmentFichajes() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_fichajes, container,false);
		listViewFichajes = (ListView) rootView.findViewById(R.id.listViewFichajes);		
	
		GetDesafioFutbol post = new GetDesafioFutbol("/mercado/"+Usuario.getIdEquipoSeleccionado()+"/index", getActivity(), null, this, "parseFichajesJson");
        post.execute();
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(2);
	}

	public void parseFichajesJson(String json) {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(json);
			int length = jsonArray.length();
			if (jsonArray != null) {
				ArrayList<Jugador> listaFichajes = new ArrayList<Jugador>(length);
				
				for(int i=0; i<length;i++){
					JSONObject usuarioJson = (JSONObject) jsonArray.get(i);
					
					Jugador jugador = new Jugador();
					jugador.setId(usuarioJson.getInt("id_jugador"));
					jugador.setApodo(usuarioJson.getString("apodo"));
					jugador.setValor(usuarioJson.getDouble("valor"));
					jugador.setPuntos(usuarioJson.getInt("puntos"));
					jugador.setPropietarioNombre(usuarioJson.getString("propietario"));
					jugador.setPropietarioId(usuarioJson.getString("propietario_id"));
					jugador.setEquipo(usuarioJson.getString("equipo"));
					jugador.setPosicion(usuarioJson.getString("posicion"));
					jugador.setUrlImagen(usuarioJson.getString("url_imagen"));
					jugador.setMiOferta(usuarioJson.getString("mioferta"));
					jugador.setDrawableEquipo(new ManageResources().getDrawableFromString(jugador.getEquipo()));
					
					listaFichajes.add(jugador);
				}
				FichajesAdapter adapter = new FichajesAdapter(getActivity(), listaFichajes);
			    listViewFichajes.setAdapter(adapter);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
