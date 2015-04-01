package app.android.desafiofutbol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import app.android.desafiofutbol.clases.DatosUsuario;

public class GetDesafioFutbol extends AsyncTask<String, Context, String> {
	
	private static final String HTTP_WWW_DESAFIOFUTBOL_COM = "http://www.desafiofutbol.com/";
	private ProgressDialog dialog;
    private Context c = null;
    private String url = HTTP_WWW_DESAFIOFUTBOL_COM;
    private HashMap<String, String> mapParams = null;
    private Fragment fragment = null;
    
    private String methodCallback = null;    

    public GetDesafioFutbol(String url, Context c, HashMap<String, String> mapParams, Fragment fragment, String methodCallback){
    	this.url = new StringBuffer(HTTP_WWW_DESAFIOFUTBOL_COM).
    			append(url).append("?").append("auth_token").append("=").append(DatosUsuario.getToken()).toString();
    	this.mapParams = mapParams;
        this.c = (Context) c;
        this.fragment = fragment;
        this.methodCallback = methodCallback;
    }

    protected String doInBackground(String...params) {    	
    	// Create a new HttpClient and Post Header
    	String result = null;
        
	    try {
	    	HttpClient httpclient = new DefaultHttpClient();
	    	
	    	if (mapParams != null){
	    		StringBuffer urlParams = new StringBuffer(url);	    		
	    		for (String name : mapParams.keySet()){
					urlParams.append("&").append(name).append("=").append(mapParams.get(name));						    		
		    	}
	    		url = urlParams.toString();
	    	}
	    	HttpGet httpget = new HttpGet(url);

	    	httpget.setHeader("Accept", "application/json");
	    	httpget.setHeader("Content-Type", "application/json");
	    	httpget.setHeader("Accept-Charset", "utf-8");	    	
				    	
	        HttpResponse response = httpclient.execute(httpget);
	        result = EntityUtils.toString(response.getEntity());
	
	    } catch (IOException e) {
	    	e.printStackTrace();
	        // TODO Auto-generated catch block
	    }
        return result;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(c);
        dialog.setTitle(c.getResources().getString(R.string.app_name));
        dialog.setMessage("Descargando Datos...");
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    protected void onPostExecute(String result) {
    	try {
			Method method = null;
			if (fragment != null){
				method = fragment.getClass().getMethod(this.methodCallback, String.class);
				method.invoke(fragment, result);
			}
			else{
				method = c.getClass().getMethod(this.methodCallback, String.class);
				method.invoke(c, result);
			}
		} catch (IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    	dialog.cancel();
    }
}