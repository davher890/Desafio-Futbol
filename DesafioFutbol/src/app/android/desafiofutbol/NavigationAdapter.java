/**
 * 
 */
package app.android.desafiofutbol;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author usuario
 *
 */
public class NavigationAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ItemObject> arrayItems;

	/**
	 * @param activity
	 * @param arrayItems
	 */
	public NavigationAdapter(Activity activity, ArrayList<ItemObject> arrayItems) {
		super();
		this.activity = activity;
		this.arrayItems = arrayItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return arrayItems.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return arrayItems.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	// Declaramos clase estatica la cual representa a la fila
	public static class Fila {
		TextView titulo_itm;
		ImageView icono;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Fila view;
		LayoutInflater inflator = activity.getLayoutInflater();
		if (convertView == null) {
			view = new Fila();
			// Creo objeto item y lo obtengo del array
			ItemObject itm = arrayItems.get(position);
			convertView = inflator.inflate(R.layout.itm, null);
			// Titulo
			view.titulo_itm = (TextView) convertView.findViewById(R.id.title_item);
			// Seteo en el campo titulo el nombre correspondiente obtenido del
			// objeto
			view.titulo_itm.setText(itm.getTitulo());
			// Icono
			view.icono = (ImageView) convertView.findViewById(R.id.icon);
			// Seteo el icono
			view.icono.setImageResource(itm.getIcono());
			convertView.setTag(view);
		} else {
			view = (Fila) convertView.getTag();
		}
		return convertView;
	}
}
