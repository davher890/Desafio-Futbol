package app.android.desafiofutbol.entrenadores;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.android.desafiofutbol.R;

public class EntrenadoresAdapter extends ArrayAdapter<Entrenador> {

	private ArrayList<Entrenador> entrenadores = null;
	
	public EntrenadoresAdapter(Context context, ArrayList<Entrenador> entrenadores) {
		super(context, 0, entrenadores);
		this.entrenadores = entrenadores;
		 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		/*if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.clasificacion_list_item, parent, false);	          
		}
		Entrenador user = entrenadores.get(position);
		
	    ((TextView)convertView.findViewById(R.id.textViewUsuarioCla)).setText(user.getUsuario());
	    ((TextView) convertView.findViewById(R.id.textViewEquipoCla)).setText(user.getNombre());
	    
	    String number = String.valueOf(user.getValor());
		double valor = Double.parseDouble(number);
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###€");
	    
	    ((TextView) convertView.findViewById(R.id.textViewValorCla)).setText(formatter.format(valor));
	    ((TextView) convertView.findViewById(R.id.textViewUltimaCla)).setText(String.valueOf(user.getUltimaJornada()));
	    ((TextView) convertView.findViewById(R.id.textViewPuntosCla)).setText(String.valueOf(user.getPuntos()));
	    */
	    return convertView;
	}
}
