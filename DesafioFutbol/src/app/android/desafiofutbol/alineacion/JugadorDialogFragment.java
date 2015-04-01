package app.android.desafiofutbol.alineacion;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import app.android.desafiofutbol.PostDesafioFutbol;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.RetreiveFeedTask;
import app.android.desafiofutbol.clases.Jugador;

//Define a DialogFragment that displays the error dialog
public class JugadorDialogFragment extends DialogFragment {

    // Global field to contain the error dialog
    private Dialog mDialog;
    private Jugador jugador = null;
    	
    private FragmentAlineacion fragment;
    
    private TextView nombre;
    private TextView equipo;
    private TextView posicion;
    private TextView puntos;
    private TextView valor;
    private ImageView imageJugador;
    
    // Default constructor. Sets the dialog field to null
    public JugadorDialogFragment( Jugador jugador, FragmentAlineacion fragment, int id) {
        super();
        this.jugador = jugador;
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
    
    public AlertDialog createDialogJugador(final Context contexto, final View v){
    	    	
    	nombre = (TextView)v.findViewById(R.id.DialogJugadorNombreApellidos);
    	equipo = (TextView)v.findViewById(R.id.DialogJugadorEquipo);
    	posicion  = (TextView) v.findViewById(R.id.DialogJugadorPosicion);
    	puntos = (TextView) v.findViewById(R.id.DialogJugadorPuntos);
    	valor = (TextView) v.findViewById(R.id.DialogJugadorValor);
    	imageJugador = (ImageView) v.findViewById(R.id.imageViewDialogJugador);
    	
    	nombre.setText(jugador.getNombre() + " " + jugador.getApellidos());
    	
    	StringBuffer nombreEquipo = new StringBuffer(jugador.getEquipo().substring(0, 1).toUpperCase()).append(jugador.getEquipo().substring(1));
    	
    	equipo.setText(nombreEquipo.toString().replaceAll("-", " "));
    	posicion.setText(jugador.getPosicion());
    	puntos.setText(String.valueOf(jugador.getPuntos()));
    	
    	DecimalFormat formatterSalario = new DecimalFormat("###,###,###,###,###€");
        valor.setText(String.valueOf(formatterSalario.format(jugador.getValor())));

        RetreiveFeedTask aus = new RetreiveFeedTask(this, jugador.getUrlImagen(), "actualizaImagen");
    	aus.execute();
 
    	AlertDialog.Builder builder = new AlertDialog.Builder(contexto);    	
		builder.setTitle(jugador.getApodo())
        	   .setPositiveButton("Poner a la Venta", new DialogInterface.OnClickListener() {
        		   @Override
        		   public void onClick(DialogInterface dialog, int which) {
        			   JSONObject json = new JSONObject();
        			   try {
        				   json.put("jugador_id", JugadorDialogFragment.this.jugador.getId());
        			   } catch (JSONException e) {
        				   e.printStackTrace();
        			   }
        			   PostDesafioFutbol post = new PostDesafioFutbol("mercado", fragment, json, "gestionaWS");
	     			   post.execute();
	     			   dialog.cancel();
        		   }
        	   })
               .setNegativeButton("Venta Expres", new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						JSONObject json = new JSONObject();
						try {
							json.put("jugador", JugadorDialogFragment.this.jugador.getId());                    	   
						} catch (JSONException e) {
							e.printStackTrace();                    	   
						}
						PostDesafioFutbol post = new PostDesafioFutbol("mercado/0/clausula_pagar", fragment, json, "gestionaWS");
						post.execute();
						dialog.cancel();
					}			
               })
               .setView(v);
        return builder.create();
    }
    
	
    public void actualizaImagen(final Bitmap bm){
    	fragment.getActivity().runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		    	 imageJugador.setImageBitmap(bm);		     
		     }
		});
    }
}