package app.android.desafiofutbol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import app.android.desafiofutbol.clases.ManageResources;


public class RetreiveFeedTask extends AsyncTask<String, DialogFragment, String> {

	String img = null;
	DialogFragment c = null;
	private String methodCallback = null;
	
	public RetreiveFeedTask(DialogFragment c, String imagen, String methodCallbak) {
		this.img = imagen;
		this.c = (DialogFragment)c;
		this.methodCallback = methodCallbak;
	}

	@Override
	public String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		URL url;
		try {
			img = img.replace("small", "medium");
			
			Bitmap imagenBm = ManageResources.getImageJugadorFromUrl(img);
			if (imagenBm == null){				
				url = new URL(img);
	        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        	conn.connect();
	        	imagenBm = BitmapFactory.decodeStream(conn.getInputStream());
	        	ManageResources.addImagenJugador(img, imagenBm);
			}
			 
	    	if (imagenBm != null){
	    		Method method = null;
				if (c != null){
					try {
						method = c.getClass().getMethod(this.methodCallback, Bitmap.class);
						method.invoke(c, imagenBm);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
	    	}
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
}
