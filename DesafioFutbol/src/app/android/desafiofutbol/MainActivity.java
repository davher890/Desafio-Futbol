package app.android.desafiofutbol;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import app.android.desafiofutbol.alineacion.FragmentAlineacion;
import app.android.desafiofutbol.clasificacion.FragmentClasificacion;
import app.android.desafiofutbol.entrenadores.FragmentEntrenadores;
import app.android.desafiofutbol.fichajes.FragmentFichajes;

public class MainActivity extends AppCompatActivity {

	private String[] titulos;
	private ArrayList<ItemObject> navItems;
	private DrawerLayout mDrawerLayout;
	private ListView navList;
	private TypedArray navIcons;

	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navList = (ListView) findViewById(R.id.left_drawer);
		titulos = getResources().getStringArray(R.array.nav_options);

		View header = getLayoutInflater().inflate(R.layout.header, null);
		navList.addHeaderView(header);
		navIcons = getResources().obtainTypedArray(R.array.nav_icons);
		navItems = new ArrayList<ItemObject>();
		for (int i = 0; i < titulos.length; i++) {
			navItems.add(new ItemObject(titulos[i], navIcons.getResourceId(i, -1)));
		}

		// Set the adapter for the list view
		navList.setAdapter(new NavigationAdapter(this, navItems));

		// Set the list's click listener
		navList.setOnItemClickListener(new DrawerItemClickListener());

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				MainActivity.this.setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				MainActivity.this.setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		selectItem(1);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		// getActionBar().setHomeButtonEnabled(true);

		// mNavigationDrawerFragment = (NavigationDrawerFragment)
		// getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		// mTitle = getTitle();
		//
		// // Set up the drawer.
		// mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
		// (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);

		// ///////
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		// ///////
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on
		// position

		Fragment fragment = null;
		if (position == 1) {
			fragment = FragmentAlineacion.newInstance(this);
		} else if (position == 2) {
			fragment = FragmentClasificacion.newInstance(this);
		} else if (position == 3) {
			fragment = FragmentEntrenadores.newInstance(this);
		} else if (position == 4) {
			fragment = FragmentFichajes.newInstance(this);
		}

		if (fragment != null) {

			// Insert the fragment by replacing any existing fragment
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			navList.setItemChecked(position - 1, true);
			setTitle(titulos[position - 1]);
			mDrawerLayout.closeDrawer(navList);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}