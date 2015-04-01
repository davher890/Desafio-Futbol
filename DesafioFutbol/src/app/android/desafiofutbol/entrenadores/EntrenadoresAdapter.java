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
import app.android.desafiofutbol.clases.Entrenador;

public class EntrenadoresAdapter extends ArrayAdapter<Entrenador> {
	 
	private ArrayList<Entrenador> entrenadores;
 
    //Constructor to initialize values
    public EntrenadoresAdapter(Context context, ArrayList<Entrenador> entrenadores) {
    	super(context, 0, entrenadores);
        this.entrenadores     = entrenadores;
    }
     
    @Override
    public int getCount() {
        return entrenadores.size();
    }
 
    @Override
    public Entrenador getItem(int position) {         
        return entrenadores.get(position);
    }
 
    @Override
    public long getItemId(int position) {         
        return 0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.entrenador_list_item, parent, false);	          
		}
        	
    	final Entrenador entrenador = getItem(position);
    	              
        TextView nombre = (TextView) convertView.findViewById(R.id.textViewNombreEnt);
        nombre.setText(entrenador.getNombre());
        TextView salario = (TextView) convertView.findViewById(R.id.textViewSalarioEnt);
        
        DecimalFormat formatterSalario = new DecimalFormat("###,###,###,###,### €/partido");
        
        salario.setText(String.valueOf(formatterSalario.format(entrenador.getSalario())));            
        TextView puntos = (TextView) convertView.findViewById(R.id.textViewPuntosEnt);
        puntos.setText(String.valueOf(entrenador.getPuntos()));
        
        TextView textViewEnt03 = (TextView) convertView.findViewById(R.id.textViewEnt03);
        TextView propietario = (TextView) convertView.findViewById(R.id.textViewPropietarioEnt);
        propietario.setText(entrenador.getPropietario());
        
        if (entrenador.getPropietario() == null || entrenador.getPropietario().equals("") || entrenador.getPropietario().equals("null")){
        	textViewEnt03.setVisibility(View.INVISIBLE);
			propietario.setVisibility(View.INVISIBLE);
		}
		else{
			propietario.setText(entrenador.getPropietario());
		}
        return convertView;
    }
}
