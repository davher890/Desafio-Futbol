package app.android.desafiofutbol.entrenadores;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import app.android.desafiofutbol.PostDesafioFutbol;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.clases.Entrenador;

//Define a DialogFragment that displays the error dialog
public class EntrenadorDialogFragment extends DialogFragment {

    // Global field to contain the error dialog
    private Dialog mDialog;
    private Entrenador entrenador = null;
    	       			   
    private TextView salario;
    private TextView puntos;
    private TextView numPartidos;
    private Spinner listPartidos;
    
    private FragmentEntrenadores fragment;
    
    //private ImageView imageFichaje;
    
    // Default constructor. Sets the dialog field to null
    public EntrenadorDialogFragment(Entrenador entrenador, FragmentEntrenadores fragment) {
        super();
        this.entrenador = entrenador;
        this.fragment = fragment;
        mDialog = null;
    }

    // Set the dialog to display
    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    // Return a Dialog to the DialogFragment.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }
    
    public AlertDialog createDialogLugar(final Context contexto, final View v){
    	    	
    	salario = (TextView)v.findViewById(R.id.DialogJugadorSalario);
    	puntos = (TextView)v.findViewById(R.id.DialogEntPuntos);
    	listPartidos  = (Spinner) v.findViewById(R.id.spinnerPartidosEnt);
    	numPartidos = (TextView) v.findViewById(R.id.textViewDialogEntPartidos);
    	
    	puntos.setText(String.valueOf(entrenador.getPuntos()));
    	
    	DecimalFormat formatterSalario = new DecimalFormat("###,###,###,###,###€");
        salario.setText(String.valueOf(formatterSalario.format(entrenador.getSalario()))+"/partido");
    	
    	final String opcionButton;
    	if (entrenador.getPropietario().equals(DatosUsuario.getNombreEquipo())){
    		opcionButton = "Despedir";
    		numPartidos.setVisibility(View.INVISIBLE);
    		listPartidos.setVisibility(View.INVISIBLE);
    	}
    	else{
    		opcionButton = "Contratar";
    		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(contexto,R.array.num_partidos,android.R.layout.simple_spinner_item);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		listPartidos.setAdapter(adapter);
    	}
 
    	AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
    	
		builder.setTitle(entrenador.getNombre())
        	   .setPositiveButton(opcionButton, new DialogInterface.OnClickListener() {
        		   public void onClick(DialogInterface dialog, int id) {
        			   if (opcionButton.equals("Despedir")){
							
							JSONObject json = new JSONObject();
			                try {
			                	json.put("delflag"+entrenador.getId(),"1");
			                	json.put("id", entrenador.getId());
							} catch (JSONException e) {
								e.printStackTrace();
							}							
							PostDesafioFutbol post = new PostDesafioFutbol("entrenadores/"+entrenador.getId()+"/contrato", fragment, json, "actualizaEntrenador");
							post.execute();
						}
						else{
							String num = listPartidos.getSelectedItem().toString();
							int numPartidos = Integer.parseInt(num);
							JSONObject json = new JSONObject();
			                try {
			                	json.put("jornadas",numPartidos);
			                	json.put("id", entrenador.getId());
							} catch (JSONException e) {
								e.printStackTrace();
							}
							PostDesafioFutbol post = new PostDesafioFutbol("entrenadores/"+entrenador.getId()+"/contrato", fragment, json, "actualizaEntrenador");
							post.execute();
						}
						dialog.cancel();
                   }
               })
               .setView(v);
        return builder.create();
    }
}