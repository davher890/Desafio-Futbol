package app.android.desafiofutbol;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import app.android.desafiofutbol.clases.Usuario;

/**
 * Created by david on 8/06/13.
 */
public class LogInActivity extends Activity {

	private Button bsign;
	private EditText email;
	private EditText pwd;
	private LogInActivity c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);        
        c = this;
        bsign   = (Button)findViewById(R.id.buttonLogIn);
        email   = (EditText)findViewById(R.id.editTextUsuario);
        pwd     = (EditText)findViewById(R.id.editTextPwd);

        bsign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            	String em = email.getText().toString();
            	String pw = pwd.getText().toString();

            	JSONObject json = new JSONObject();
                try {
                	json.put("login","davher890");
					json.put("password", "hypsyfzj0468");
					//json.put("login",em);
					//json.put("password", pw);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                PostDesafioFutbol post = new PostDesafioFutbol("api/token", c, json, "gestionaWS");
		        post.execute();
            }
        });
    }
    
    public void gestionaWS(String json){
    	JSONObject respJSON;
		try {
			respJSON = new JSONObject(json);
			Usuario.setToken(respJSON.getString("token"));
			Usuario.setUserId(Integer.parseInt(respJSON.getString("user_id")));
			Usuario.setUserName(respJSON.getString("username"));
			Usuario.setAvatar(respJSON.getString("avatar"));
			Usuario.setP_rank(Integer.parseInt(respJSON.getString("p_rank")));
			
			Intent i = new Intent(LogInActivity.this, MisEquiposActivity.class);
            startActivityForResult(i, 1);
			
		} catch (JSONException e) {
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Email o contraseña incorrectos");
			alertDialog.setButton(RESULT_OK, "Aceptar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub		
					alertDialog.cancel();
				}
			});
			alertDialog.show();
		}		
		//Recuperar datos Usuario
    }
}
