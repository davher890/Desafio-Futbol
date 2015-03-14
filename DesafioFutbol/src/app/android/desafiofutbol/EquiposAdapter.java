package app.android.desafiofutbol;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.android.desafiofutbol.clases.Equipo;

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
	    ((TextView)convertView.findViewById(R.id.textViewLigaEq)).setText(equipo.getLigaNombre());	    
	    return convertView;
	}
}
