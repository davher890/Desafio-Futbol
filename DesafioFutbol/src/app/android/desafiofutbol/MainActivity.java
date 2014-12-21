package app.android.desafiofutbol;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import app.android.desafiofutbol.alineacion.FragmentAlineacion;
import app.android.desafiofutbol.clasificacion.FragmentClasificacion;
import app.android.desafiofutbol.entrenadores.FragmentEntrenadores;
import app.android.desafiofutbol.fichajes.FragmentFichajes;
import app.android.desafiofutbol.liga.FragmentLiga;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		// update the main content by replacing fragments
		if (position == 0) {
			fragmentManager.beginTransaction().replace(R.id.container, FragmentAlineacion.newInstance()).commit();			
		} else if (position == 1) {
			fragmentManager.beginTransaction().replace(R.id.container, FragmentFichajes.newInstance()).commit();			
		} else if (position == 2) {
			fragmentManager.beginTransaction().replace(R.id.container, FragmentEntrenadores.newInstance()).commit();
		} else if (position == 3) {
			fragmentManager.beginTransaction().replace(R.id.container, FragmentLiga.newInstance()).commit();			
		} else if (position == 4) {
			fragmentManager.beginTransaction().replace(R.id.container, FragmentClasificacion.newInstance()).commit();			
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.label_alineacion);
			break;
		case 2:
			mTitle = getString(R.string.label_fichajes);
			break;
		case 3:
			mTitle = getString(R.string.label_entrenadores);
			break;
		case 4:
			mTitle = getString(R.string.label_liga);
			break;
		case 5:
			mTitle = getString(R.string.label_clasificacion);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}