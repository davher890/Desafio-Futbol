package app.android.desafiofutbol;

import java.util.HashMap;
import java.util.Map;

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
import android.widget.Toast;
import app.android.desafiofutbol.clases.DatosUsuario;
import app.android.desafiofutbol.webservices.VolleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by david on 8/06/13.
 */
public class LogInActivity extends Activity {

	private Button bsign;
	private EditText email;
	private EditText pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		bsign = (Button) findViewById(R.id.buttonLogIn);
		email = (EditText) findViewById(R.id.editTextUsuario);
		pwd = (EditText) findViewById(R.id.editTextPwd);

		bsign.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				String em = email.getText().toString();
				String pw = pwd.getText().toString();

				JSONObject json = new JSONObject();
				try {
					json.put("login", em);
					json.put("password", pw);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String url = "http://www.desafiofutbol.com/api/token";

				// Request a string response
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject json) {
						gestionaWS(json.toString());

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						gestionaError(error);
					}
				}) {
					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("Accept", "application/json");
						map.put("Content-Type", "application/json");
						map.put("Accept-Charset", "utf-8");

						return map;
					}
				};
				bsign.setEnabled(false);
				// Add the request to the queue
				VolleyRequest.getInstance(LogInActivity.this).addToRequestQueue(request);
			}
		});
	}

	public void gestionaWS(String json) {
		JSONObject respJSON;
		try {
			respJSON = new JSONObject(json);
			DatosUsuario.setToken(respJSON.getString("token"));
			DatosUsuario.setUserId(Integer.parseInt(respJSON.getString("user_id")));
			DatosUsuario.setUserName(respJSON.getString("username"));
			DatosUsuario.setAvatar(respJSON.getString("avatar"));
			DatosUsuario.setP_rank(Integer.parseInt(respJSON.getString("p_rank")));

			if (DatosUsuario.checkData()) {
				Intent i = new Intent(LogInActivity.this, MisEquiposActivity.class);
				startActivityForResult(i, 1);
			}
			bsign.setEnabled(true);
		} catch (JSONException e) {
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Email o contrasena incorrectos");
			alertDialog.setButton(RESULT_OK, "Aceptar", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
				}
			});
			alertDialog.show();
		}
	}

	private void gestionaError(VolleyError error) {
		bsign.setEnabled(true);
		Toast.makeText(LogInActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

	}
}
