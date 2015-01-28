package app.android.desafiofutbol;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.ManageResources;

public class AlineacionAdapter extends ArrayAdapter<Jugador> {
	
	private ArrayList<Jugador> jugadores = null;
	private int max = 0;
	private int min = 0;
	
	public AlineacionAdapter(Context context, ArrayList<Jugador> jugadores) {
		super(context, 0, jugadores);
		this.jugadores = jugadores;		 
	}
	
	/**
	 * @return the jugadores
	 */
	public ArrayList<Jugador> getJugadores() {
		return jugadores;
	}

	/**
	 * @param jugadores the jugadores to set
	 */
	public void setJugadores(ArrayList<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(int min) {
		this.min = min;
	}
	
	public void removeJugador(int posicion){
		jugadores.remove(posicion);
		this.notifyDataSetChanged();		
	}
	
	public void addJugador(Jugador jugador){
		jugadores.add(jugador);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.jug_alineacion_list_item, parent, false);	          
		}
		Jugador jugador = jugadores.get(position);
		
		ImageView imgEquipo = (ImageView)convertView.findViewById(R.id.imageViewEquipoAli);
		imgEquipo.setImageResource(new ManageResources().getImageFromString(jugador.getEquipo()));
		
		TextView apodo = (TextView)convertView.findViewById(R.id.textViewApodoAli);
		apodo.setText(jugador.getApodo());
		
		TextView puntos = (TextView)convertView.findViewById(R.id.textViewPuntosAli);
		puntos.setText(String.valueOf(jugador.getPuntos()));
	    
	    return convertView;
	}
	
}
