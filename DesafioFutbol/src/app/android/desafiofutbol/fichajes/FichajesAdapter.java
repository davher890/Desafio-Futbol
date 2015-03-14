package app.android.desafiofutbol.fichajes;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.Jugador;

public class FichajesAdapter extends ArrayAdapter<Jugador> {

	private ArrayList<Jugador> listaFichajes = null;

	public FichajesAdapter(Context context, ArrayList<Jugador> listaFichajes) {
		super(context, 0, listaFichajes);
		this.listaFichajes = listaFichajes;
		 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.fichajes_list_item, parent, false);	          
		}
		Jugador jugador = listaFichajes.get(position);
	    
	    String number = String.valueOf(jugador.getValor());
		double precio = Double.parseDouble(number);
		number = String.valueOf(jugador.getPuntos());
		double puntos = Double.parseDouble(number);
		DecimalFormat formatterValor = new DecimalFormat("###,###,###,###,###€");
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
	    
	    ((TextView) convertView.findViewById(R.id.textViewNombreJug)).setText(jugador.getApodo());
	    ((TextView) convertView.findViewById(R.id.textViewPosicionJug)).setText(jugador.getPosicion());
	    ((TextView) convertView.findViewById(R.id.textViewEquipoJug)).setText(jugador.getEquipo());
	    ((TextView) convertView.findViewById(R.id.textViewPrecioJug)).setText(formatterValor.format(precio));
	    ((TextView) convertView.findViewById(R.id.textViewPuntosJug)).setText(formatter.format(puntos));
	    
	    convertView.setBackgroundResource(jugador.getDrawableEquipo());
	    return convertView;
	}
	
	/**
	 * @return the listaFichajes
	 */
	public ArrayList<Jugador> getListaFichajes() {
		return listaFichajes;
	}

	/**
	 * @param listaFichajes the listaFichajes to set
	 */
	public void setListaFichajes(ArrayList<Jugador> listaFichajes) {
		this.listaFichajes = listaFichajes;
	}
}
