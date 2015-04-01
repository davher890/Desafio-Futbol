package app.android.desafiofutbol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import app.android.desafiofutbol.alineacion.FragmentAlineacion;
import app.android.desafiofutbol.clases.DatosUsuario;

public class PutDesafioFutbol extends AsyncTask<String, Context, String> {
	
	private static final String HTTP_WWW_DESAFIOFUTBOL_COM = "http://www.desafiofutbol.com/";
	private ProgressDialog dialog;
    private FragmentAlineacion fragment = null;
    private String url = HTTP_WWW_DESAFIOFUTBOL_COM;
    private JSONObject json;
    private String methodCallback = null;

    public PutDesafioFutbol(String url, FragmentAlineacion fragment, JSONObject json, String methodCallback){
    	this.url = new StringBuffer(HTTP_WWW_DESAFIOFUTBOL_COM).append(url).append("?").append("auth_token").append("=").append(DatosUsuario.getToken()).toString();
        this.json = json;
        this.fragment = fragment;
        this.methodCallback = methodCallback;
    }
    
	protected String doInBackground(String...params) {    	
    	// Create a new HttpClient and Post Header
    	String result = null;
        
	    try {
	    	HttpClient httpclient = new DefaultHttpClient();	    	
	    	HttpPut httpput = new HttpPut(url);

	    	httpput.setHeader("Accept", "application/json");
	    	httpput.setHeader("Content-Type", "application/json");
	    	httpput.setHeader("Accept-Charset", "utf-8");
	    	
	    	if (json != null){
		    	StringEntity se = new StringEntity(json.toString());	    	
		        httpput.setEntity(se);
	    	}
	       
	        HttpResponse response = httpclient.execute(httpput);
	        result = EntityUtils.toString(response.getEntity());
	
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	    	e.printStackTrace();
	        // TODO Auto-generated catch block
	    } catch(Exception ex){  
	    	ex.printStackTrace();
        	// TODO Auto-generated catch block          
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(fragment.getActivity());
        dialog.setTitle(fragment.getResources().getString(R.string.app_name));
        dialog.setMessage("Descargando Datos...");
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    protected void onPostExecute(String result) {        
        if (fragment != null){
        	dialog.cancel();
        	
        	Method method = null;
			try {
				method = fragment.getClass().getMethod(this.methodCallback, String.class);
				method.invoke(fragment, result);
				
			} catch (NoSuchMethodException | IllegalAccessException | 
					IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
        }        
    }
}
