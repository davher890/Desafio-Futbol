package app.android.desafiofutbol;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.android.desafiofutbol.clases.Equipo;
import app.android.desafiofutbol.clases.Jugador;

public class EquiposAdapter extends ArrayAdapter<Equipo> {
	
	private ArrayList<Equipo> equipos = null;
	
	public EquiposAdapter(Context context, ArrayList<Equipo> equipos) {
		super(context, 0, equipos);
		this.equipos = equipos;		 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.equipo_list_item, parent, false);	          
		}
		Equipo equipo = equipos.get(position);
		
		String number = String.valueOf(equipo.getValor());
		double valor = Double.parseDouble(number);
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###€");
		
		((TextView)convertView.findViewById(R.id.textViewNombreEq)).setText(equipo.getNombre());
	    ((TextView)convertView.findViewById(R.id.textViewValorEq)).setText(formatter.format(valor));
	    ((TextView)convertView.findViewById(R.id.textViewPuntosEq)).setText(String.valueOf(equipo.getPuntos()));
	    
	    if (equipo.getJugadores() != null){
	    	int size = equipo.getJugadores().size();
			((TextView)convertView.findViewById(R.id.TextViewJugadoresEq)).setText(String.valueOf(size));
	    	
	    	LinearLayout layoutJugadores = (LinearLayout) convertView.findViewById(R.id.layoutJugadoresEq);
	    	layoutJugadores.removeAllViews();
	    	TextView labelUltima = new TextView(getContext());
	    	labelUltima.setText("Última alineación:");
	    	layoutJugadores.addView(labelUltima);
	    	TextView jug = null;
	    	for(int i=0;i<size;i++){
	    		Jugador jugador = equipo.getJugadores().get(i);
	    		if (jugador.getTitular() == 1){
		    		jug = new TextView(getContext());
		    		
					jug.setText(jugador.getApodo());
		    		layoutJugadores.addView(jug);
	    		}
	    	}
	    	
	    }
	    
	    return convertView;
	}
}
