package app.android.desafiofutbol.liga;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.android.desafiofutbol.MainActivity;
import app.android.desafiofutbol.R;

public class FragmentLiga extends Fragment {
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	
	public static FragmentLiga newInstance() {
		FragmentLiga fragment = new FragmentLiga();
		return fragment;		
	}
	
	public FragmentLiga() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_liga, container,false);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(4);
	}

}
