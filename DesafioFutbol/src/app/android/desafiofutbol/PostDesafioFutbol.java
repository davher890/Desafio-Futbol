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
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class PostDesafioFutbol extends AsyncTask<String, Context, String> {
	
	private static final String HTTP_WWW_DESAFIOFUTBOL_COM = "http://www.desafiofutbol.com/";
	private ProgressDialog dialog;
    private LogInActivity log = null;
    private Context c = null;
    private String url = HTTP_WWW_DESAFIOFUTBOL_COM;
    private JSONObject json;
    private String methodCallback = null;

    public PostDesafioFutbol(String url, LogInActivity c, JSONObject json, String methodCallback){
    	this.url = new StringBuffer(HTTP_WWW_DESAFIOFUTBOL_COM).append(url).toString();
        this.json = json;
        this.log = c;
        this.c = (Context) c;
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
	    	
	    	StringEntity se = new StringEntity(json.toString());
	    	
	        httppost.setEntity(se);
	       
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
        System.out.println(result);        
        if (log != null){
        	dialog.cancel();
        	log.gestionaWS(result);        	
        	
        	try {
        		Method method = c.getClass().getMethod(this.methodCallback, String.class);
				method.invoke(c, result);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }        
    }
}
