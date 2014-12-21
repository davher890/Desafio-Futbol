package app.android.desafiofutbol;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


class RetreiveFeedTask extends AsyncTask<String, Context, String> {

	String img = null;
	Context c = null;
	ProgressDialog dialog;
	AlertDialog.Builder alert;
	public RetreiveFeedTask(Context c, String imagen, ProgressDialog dialog) {
		this.img = imagen;
		this.c = c;
		this.dialog = dialog;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		URL url;
		try {
			url = new URL(img);
		
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        	conn.connect();
        	Bitmap imagenBm = BitmapFactory.decodeStream(conn.getInputStream());
			
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			/*View v = inflater.inflate(R.layout.dialog_image, null);
	    	ImageView iv = (ImageView) v.findViewById(R.id.imagen);
	    	String titulo = "Código recibido"; 
	    	if (imagenBm != null){
	    		iv.setImageBitmap(imagenBm);
	    		titulo = "Imagen recibida";	    		
	    	}*/
	    		
	    	alert = new AlertDialog.Builder(c);  
	        /*alert.setTitle(titulo);  
	        alert.setView(v);*/
			
			return null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(String result) {
		dialog.cancel();
		alert.show();
	}	
}
