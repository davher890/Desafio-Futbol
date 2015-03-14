package app.android.desafiofutbol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import app.android.desafiofutbol.alineacion.FragmentAlineacion;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.entrenadores.FragmentEntrenadores;
import app.android.desafiofutbol.fichajes.FragmentFichajes;

public class PostDesafioFutbol extends AsyncTask<String, Context, String> {
	
	private static final String HTTP_WWW_DESAFIOFUTBOL_COM = "http://www.desafiofutbol.com/";
	private ProgressDialog dialog;
    private LogInActivity log = null;
    private Context c = null;
    private String url = HTTP_WWW_DESAFIOFUTBOL_COM;
    private JSONObject json;
    private String methodCallback = null;
    private Fragment fragment = null;

    public PostDesafioFutbol(String url, LogInActivity c, JSONObject json, String methodCallback){
    	this.url = new StringBuffer(HTTP_WWW_DESAFIOFUTBOL_COM).append(url).toString();
        this.json = json;
        this.log = c;
        this.c = (Context) c;
        this.methodCallback = methodCallback;
    }
    
	public PostDesafioFutbol(String url, FragmentEntrenadores fragment, JSONObject json, String methodCallback) {
		this.url = new StringBuffer(HTTP_WWW_DESAFIOFUTBOL_COM).
    			append(url).append("?").append("auth_token").append("=").append(DatosUsuario.getToken()).toString();
        this.c = (Context) fragment.getActivity();
        this.fragment = fragment;
        this.json = json;
        this.methodCallback = methodCallback;
	}
	
	public PostDesafioFutbol(String url, FragmentFichajes fragment, JSONObject json, String methodCallback) {
		this.url = new StringBuffer(HTTP_WWW_DESAFIOFUTBOL_COM).
    			append(url).append("?").append("auth_token").append("=").append(DatosUsuario.getToken()).toString();
        this.c = (Context) fragment.getActivity();
        this.fragment = fragment;
        this.json = json;
        this.methodCallback = methodCallback;
	}
	
	public PostDesafioFutbol(String url, FragmentAlineacion fragment, JSONObject json, String methodCallback) {
		this.url = new StringBuffer(HTTP_WWW_DESAFIOFUTBOL_COM).
    			append(url).append("?").append("auth_token").append("=").append(DatosUsuario.getToken()).toString();
        this.c = (Context) fragment.getActivity();
        this.fragment = fragment;
        this.json = json;
        this.methodCallback = methodCallback;
	}

	protected String doInBackground(String...params) {    	
    	// Create a new HttpClient and Post Header
    	String result = null;
        
	    try {
	    	HttpClient httpclient = new DefaultHttpClient();	    	
	    	HttpPost httppost = new HttpPost(url);

	    	httppost.setHeader("Accept", "application/json");
	    	httppost.setHeader("Content-Type", "application/json");
	    	httppost.setHeader("Accept-Charset", "utf-8");
	    	
	    	if (json != null){
		    	StringEntity se = new StringEntity(json.toString());	    	
		        httppost.setEntity(se);
	    	}
	       
	        HttpResponse response = httpclient.execute(httppost);
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
        dialog = new ProgressDialog(c);
        dialog.setTitle(c.getResources().getString(R.string.app_name));
        dialog.setMessage("Descargando Datos...");
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    protected void onPostExecute(String result) {        
        if (log != null){        	
        	try {
        		Method method = c.getClass().getMethod(this.methodCallback, String.class);
				method.invoke(c, result);
			} catch (IllegalAccessException | IllegalArgumentException | 
					InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
        }
        else{
        	Method method = null;
			if (fragment != null){
				try {
					if (fragment instanceof FragmentEntrenadores){
						method = fragment.getClass().getMethod(this.methodCallback, String.class, int.class);
						method.invoke(fragment, result, json.getInt("id"));
					}
					else if (fragment instanceof FragmentFichajes){
						method = fragment.getClass().getMethod(this.methodCallback, String.class, int.class, int.class);
						method.invoke(fragment, result, json.getInt("id"), json.getInt("oferta"));
					}
					else{
						method = fragment.getClass().getMethod(this.methodCallback, String.class);
						method.invoke(fragment, result);
					}
					
				} catch (NoSuchMethodException | IllegalAccessException | 
						IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
        dialog.cancel();
    }
}