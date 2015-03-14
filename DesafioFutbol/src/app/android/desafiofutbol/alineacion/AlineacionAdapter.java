package app.android.desafiofutbol.alineacion;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.ManageResources;

public class AlineacionAdapter extends ArrayAdapter<Jugador> {
	
	private Posicion posicion;
	private ArrayList<Jugador> jugadores = null;
	private int max = 0;
	private int min = 0;
	private FragmentAlineacion context = null;
	
	public AlineacionAdapter(Context context, ArrayList<Jugador> jugadores, FragmentAlineacion context2, Posicion portero) {		
		super(context, 0, jugadores);
		this.context = context2;		
		this.jugadores = jugadores;
		this.posicion = portero;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.jug_alineacion_list_item, parent, false);	          
		}

		convertView.setOnDragListener(context);
		convertView.setOnLongClickListener(context);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Jugador jugador = jugadores.get(position);
		        
				LayoutInflater inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View vDialog = inflater.inflate(R.layout.dialog_jugador, null);
				
		        JugadorDialogFragment alert = new JugadorDialogFragment(jugador, context);
		        AlertDialog createDialogLugar = alert.createDialogLugar(context.getActivity(), vDialog);
		        createDialogLugar.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);			
				createDialogLugar.show();
				
			}
		});
		
		Jugador jugador = jugadores.get(position);
		
		ImageView imgEquipo = (ImageView)convertView.findViewById(R.id.imageViewEquipoAli);
		
		imgEquipo.setImageResource(ManageResources.getImageFromString(jugador.getEquipo()));
		
		TextView apodo = (TextView)convertView.findViewById(R.id.textViewApodoAli);
		apodo.setText(jugador.getApodo());
		
		TextView puntos = (TextView)convertView.findViewById(R.id.textViewPuntosAli);
		puntos.setText(jugador.getApodo()==null ? null : String.valueOf(jugador.getPuntos()));
	    
	    return convertView;
	}

	/**
	 * @return the posicion
	 */
	public Posicion getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}
}