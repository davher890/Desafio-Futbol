package app.android.desafiofutbol.usuarios;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.android.desafiofutbol.R;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

	private ArrayList<Usuario> usuarios = null;

	public UsuarioAdapter(Context context, ArrayList<Usuario> usuarios) {
		super(context, 0, usuarios);
		this.usuarios = usuarios;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.usuario_list_item, parent, false);
		}
		Usuario usuario = usuarios.get(position);

		TextView nombreUsu = (TextView) convertView.findViewById(R.id.textViewNombreUsuario);
		TextView rankingUsu = (TextView) convertView.findViewById(R.id.textViewRankingUsuario);
		TextView numEquiposUsu = (TextView) convertView.findViewById(R.id.textViewNumEquiposUsuario);

		nombreUsu.setText(usuario.getNombre());
		rankingUsu.setText(String.valueOf(usuario.getRanking()));
		numEquiposUsu.setText(String.valueOf(usuario.getNumEquipos()));

		return convertView;
	}
}