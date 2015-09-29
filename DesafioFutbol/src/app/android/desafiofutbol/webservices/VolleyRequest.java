/**
 * 
 */
package app.android.desafiofutbol.webservices;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author usuario
 *
 */
public class VolleyRequest {

	private RequestQueue mRequestQueue;
	private static VolleyRequest instance;
	private static Context mCtx;

	private VolleyRequest(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();
	}

	public static synchronized VolleyRequest getInstance(Context context) {
		if (instance == null) {
			instance = new VolleyRequest(context);
		}
		return instance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		getRequestQueue().add(req);
	}
}
