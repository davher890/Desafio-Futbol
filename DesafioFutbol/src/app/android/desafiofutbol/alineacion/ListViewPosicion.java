package app.android.desafiofutbol.alineacion;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewPosicion extends ListView {

	private AlineacionAdapter alineacionAdapter = null;

	public ListViewPosicion(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		this.alineacionAdapter = (AlineacionAdapter) adapter;
		super.setAdapter(adapter);
	}

	/**
	 * @return the alineacionAdapter
	 */
	public AlineacionAdapter getAlineacionAdapter() {
		return alineacionAdapter;
	}
}
