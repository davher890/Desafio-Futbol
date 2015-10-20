package app.android.desafiofutbol.ddbb;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import app.android.desafiofutbol.clases.Entrenador;
import app.android.desafiofutbol.clases.Jugador;
import app.android.desafiofutbol.clases.ManageResources;
import app.android.desafiofutbol.clasificacion.UsuarioClasificacion;

public class SQLiteDesafioFutbol extends SQLiteOpenHelper {

	private static final String SQL_DROP = "DROP TABLE IF EXISTS jugador, entrenador, fichaje, clasificacion";
	private static final String SQL_CREATE_JUGADOR = "CREATE TABLE jugador " + "(id INTEGER," + "equipo_id INTEGER," + "equipo_nombre TEXT,"
			+ "puntos INTEGER, " + "nombre TEXT, " + "apellidos TEXT," + "apodo TEXT," + "foto TEXT," + "precio INTEGER," + "posicion TEXT,"
			+ "nacion TEXT," + "nacion_logo TEXT," + "edad INTEGER," + "titular INTEGER,"// 0
																							// false,
																							// 1
																							// true
			+ "PRIMARY KEY (id))";

	private static final String SQL_CREATE_ENTRENADOR = "CREATE TABLE entrenador " + "(id INTEGER," + "nombre TEXT, " + "equipo TEXT,"
			+ "salario INTEGER," + "puntos INTEGER, " + "propietario TEXT, " + "PRIMARY KEY (id))";

	private static final String SQL_CREATE_FICHAJE = "CREATE TABLE fichaje " + "(id INTEGER," + "apodo TEXT," + "valor INTEGER," + "puntos INTEGER, "
			+ "propietario TEXT, " + "propietario_id INTEGER," + "equipo TEXT," + "posicion TEXT," + "url_imagen TEXT," + "mi_oferta INTEGER,"
			+ "id_mercado TEXT," + "PRIMARY KEY (id))";

	private static final String SQL_CREATE_CLASFICIACION = "CREATE TABLE clasificacion " + "(id INTEGER," + "usuario TEXT," + "usuario_id INTEGER,"
			+ "nombre TEXT," + "puntos INTEGER," + "valor INTEGER, " + "ultima_jornada INTEGER, " + "PRIMARY KEY (id))";

	private static final String SQL_SELECT_FICHAJE = "SELECT * FROM fichaje WHERE id = ";

	// Metodos de SQLiteOpenHelper
	public SQLiteDesafioFutbol(Context context) {
		super(context, "desafiofutbol", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_JUGADOR);
		db.execSQL(SQL_CREATE_ENTRENADOR);
		db.execSQL(SQL_CREATE_FICHAJE);
		db.execSQL(SQL_CREATE_CLASFICIACION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		onCreate(db);
		db.close();
	}

	public void restartDatabase() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(SQL_DROP);
		db.close();
	}

	// Metodos de AlmacenPuntuaciones
	public void saveJugadores(ArrayList<Jugador> jugadores) {
		SQLiteDatabase db = getWritableDatabase();

		for (int i = 0; i < jugadores.size(); i++) {
			Jugador jugador = jugadores.get(i);

			StringBuffer sb = new StringBuffer("INSERT INTO jugador VALUES (");
			sb.append(jugador.getId()).append(", ");
			sb.append(jugador.getEquipoId()).append(", ");
			sb.append("'").append(jugador.getEquipo()).append("', ");
			sb.append(jugador.getPuntos()).append(", ");
			sb.append("'").append(jugador.getNombre().replaceAll("'", "''")).append("', ");
			sb.append("'").append(jugador.getApellidos().replaceAll("'", "''")).append("', ");
			sb.append("'").append(jugador.getApodo().replaceAll("'", "''")).append("', ");
			sb.append("'").append(jugador.getUrlImagen()).append("', ");
			sb.append(jugador.getValor()).append(", ");
			sb.append("'").append(jugador.getPosicion()).append("', ");
			sb.append("'").append(jugador.getNacion()).append("', ");
			sb.append("'").append(jugador.getNacionLogo()).append("', ");
			sb.append(jugador.getEdad()).append(", ");
			sb.append(jugador.getTitular()).append(")");
			try {
				db.execSQL(sb.toString());
			} catch (SQLException e) {
				db.delete("jugador", null, null);
			}
		}
		db.close();
	}

	public ArrayList<Jugador> getJugadores() {
		ArrayList<Jugador> result = new ArrayList<Jugador>();
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT apodo, posicion, equipo_nombre, puntos, titular, nombre, apellidos, foto ,precio FROM jugador";

		Cursor cursor = db.rawQuery(query, null);
		Jugador j = null;
		while (cursor.moveToNext()) {
			j = new Jugador();
			j.setApodo(cursor.getString(0));
			j.setPosicion(cursor.getString(1));
			j.setEquipo(cursor.getString(2));
			j.setPuntos(cursor.getInt(3));
			j.setTitular(cursor.getInt(4));
			j.setNombre(cursor.getString(5));
			j.setApellidos(cursor.getString(6));
			j.setUrlImagen(cursor.getString(7));
			j.setValor(cursor.getInt(8));
			result.add(j);
		}
		cursor.close();
		db.close();
		return result;
	}

	public ArrayList<Entrenador> getEntrenadores() {

		ArrayList<Entrenador> result = new ArrayList<Entrenador>();
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT id, nombre, equipo, salario, puntos, propietario FROM entrenador";
		Cursor cursor = db.rawQuery(query, null);
		Entrenador j = null;
		while (cursor.moveToNext()) {
			j = new Entrenador();
			j.setId(cursor.getInt(0));
			j.setNombre(cursor.getString(1));
			j.setEquipo(cursor.getString(2));
			j.setSalario(cursor.getInt(3));
			j.setPuntos(cursor.getInt(4));
			j.setPropietario(cursor.getString(5));
			result.add(j);
		}
		cursor.close();
		db.close();
		return result;
	}

	public void saveFichajes(ArrayList<Jugador> fichajes) {
		SQLiteDatabase db = getWritableDatabase();

		for (int i = 0; i < fichajes.size(); i++) {

			Jugador jugador = fichajes.get(i);
			String apodo = jugador.getApodo().replaceAll("'", "''");
			String equipo = jugador.getEquipo().replaceAll("'", "''");
			String posicion = jugador.getPosicion().replaceAll("'", "''");
			String propiertarioNombre = jugador.getPropietarioNombre().replaceAll("'", "''");

			StringBuffer sb = new StringBuffer("INSERT INTO fichaje VALUES (");
			sb.append(jugador.getId()).append(", ");
			sb.append("'").append(apodo).append("', ");
			sb.append(jugador.getValor()).append(",");
			sb.append(jugador.getPuntos()).append(",");
			sb.append("'").append(propiertarioNombre).append("'").append(", ");
			sb.append("'").append(jugador.getPropietarioId()).append("'").append(", ");
			sb.append("'").append(equipo).append("'").append(", ");
			sb.append("'").append(posicion).append("'").append(", ");
			sb.append("'").append(jugador.getUrlImagen()).append("'").append(", ");
			sb.append(jugador.getMiOferta()).append(",");
			sb.append("'").append(jugador.getIdMercado()).append("'").append(")");

			try {
				db.execSQL(sb.toString());
			} catch (SQLException e) {
				db.delete("fichaje", null, null);
			}

		}
		db.close();
	}

	public ArrayList<Jugador> getFichajes() {

		ArrayList<Jugador> result = new ArrayList<Jugador>();
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT id, apodo, valor, puntos, propietario, propietario_id, equipo, posicion, url_imagen, mi_oferta, id_mercado FROM fichaje ORDER BY puntos desc";
		Cursor cursor = db.rawQuery(query, null);
		Jugador j = null;
		while (cursor.moveToNext()) {
			j = new Jugador();
			j.setId(cursor.getInt(0));
			j.setApodo(cursor.getString(1));
			j.setValor(cursor.getInt(2));
			j.setPuntos(cursor.getInt(3));
			j.setPropietarioNombre(cursor.getString(4));
			j.setPropietarioId(cursor.getInt(5));
			j.setEquipo(cursor.getString(6));
			j.setPosicion(cursor.getString(7));
			j.setUrlImagen(cursor.getString(8));
			j.setMiOferta(cursor.getInt(9));
			j.setIdMercado(cursor.getString(10));
			j.setDrawableEquipo(ManageResources.getDrawableFromString(j.getEquipo()));
			result.add(j);
		}
		cursor.close();
		db.close();
		return result;
	}

	public ArrayList<UsuarioClasificacion> getClasificacion() {

		ArrayList<UsuarioClasificacion> result = new ArrayList<UsuarioClasificacion>();
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT id, usuario, usuario_id, puntos, valor, ultima_jornada, nombre FROM clasificacion ORDER BY puntos desc";
		Cursor cursor = db.rawQuery(query, null);
		UsuarioClasificacion j = null;
		while (cursor.moveToNext()) {
			j = new UsuarioClasificacion();
			j.setId(cursor.getInt(0));
			j.setUsuario(cursor.getString(1));
			j.setUsuario_id(cursor.getInt(2));
			j.setPuntos(cursor.getInt(3));
			j.setValor(cursor.getInt(4));
			j.setUltimaJornada(cursor.getInt(5));
			j.setNombre(cursor.getString(6));
			result.add(j);
		}
		cursor.close();
		db.close();
		return result;
	}

	public void saveEntrenadores(ArrayList<Entrenador> entrenadores) {
		SQLiteDatabase db = getWritableDatabase();

		for (int i = 0; i < entrenadores.size(); i++) {

			Entrenador entrenador = entrenadores.get(i);

			StringBuffer sb = new StringBuffer("INSERT INTO entrenador VALUES (");
			sb.append(entrenador.getId()).append(", ");
			sb.append("'").append(entrenador.getNombre()).append("', ");
			sb.append("'").append(entrenador.getEquipo()).append("', ");
			sb.append(entrenador.getSalario()).append(",");
			sb.append(entrenador.getPuntos()).append(", ");
			sb.append("'").append(entrenador.getPropietario()).append("') ");

			try {
				db.execSQL(sb.toString());
			} catch (SQLException e) {
				db.delete("entrenador", null, null);
			}
		}
		db.close();
	}

	public int updateEntrenador(int idEntrenador, String value) {
		if (value == null)
			return 0;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put("propietario", value);
		int update = db.update("entrenador", args, "id=" + idEntrenador, null);
		db.close();
		return update;
	}

	public int updateFichaje(int idFichaje, int oferta) {
		// if (oferta < 1)
		// return 0;

		SQLiteDatabase db = getWritableDatabase();
		ContentValues args = new ContentValues();
		// if (oferta == -1) {
		// args.put("propietario", "null");
		// args.put("propietario_id", -1);
		// }
		// else {
		// args.put("propietario", DatosUsuario.getNombreEquipo());
		// args.put("propietario_id", DatosUsuario.getIdEquipoSeleccionado());
		// }
		args.put("mi_oferta", oferta);
		int update = db.update("fichaje", args, "id=" + idFichaje, null);
		db.close();
		return update;
	}

	public int createFichaje(Jugador jugador) {

		SQLiteDatabase db = getWritableDatabase();

		String apodo = jugador.getApodo().replaceAll("'", "''");
		String equipo = jugador.getEquipo().replaceAll("'", "''");
		String posicion = jugador.getPosicion().replaceAll("'", "''");
		String propiertarioNombre = jugador.getPropietarioNombre().replaceAll("'", "''");

		StringBuffer sb = new StringBuffer("INSERT INTO fichaje VALUES (");
		sb.append(jugador.getId()).append(", ");
		sb.append("'").append(apodo).append("', ");
		sb.append(jugador.getValor()).append(",");
		sb.append(jugador.getPuntos()).append(",");
		sb.append("'").append(propiertarioNombre).append("'").append(", ");
		sb.append("'").append(jugador.getPropietarioId()).append("'").append(", ");
		sb.append("'").append(equipo).append("'").append(", ");
		sb.append("'").append(posicion).append("'").append(", ");
		sb.append("'").append(jugador.getUrlImagen()).append("'").append(", ");
		sb.append(jugador.getMiOferta()).append(",");
		sb.append("'").append(jugador.getIdMercado()).append("'").append(")");
		db.execSQL(sb.toString());

		try {
			db.execSQL(sb.toString());
		} catch (SQLException e) {
			db.delete("fichaje", null, null);
		}

		db.close();
		return 0;
	}

	public int deleteFichaje(int idFichaje) {
		SQLiteDatabase db = getWritableDatabase();
		int delete = db.delete("fichaje", "id=" + idFichaje, null);
		db.close();
		return delete;
	}

	public int deleteJugador(int idJugador) {
		SQLiteDatabase db = getWritableDatabase();
		int delete = db.delete("jugador", "id=" + idJugador, null);
		db.close();
		return delete;
	}

	public void saveClasificacion(ArrayList<UsuarioClasificacion> usuariosClas) {
		SQLiteDatabase db = getWritableDatabase();

		for (int i = 0; i < usuariosClas.size(); i++) {

			UsuarioClasificacion usuario = usuariosClas.get(i);

			StringBuffer sb = new StringBuffer("INSERT INTO clasificacion VALUES (");
			sb.append(usuario.getId()).append(", ");
			sb.append("'").append(usuario.getUsuario()).append("', ");
			sb.append(usuario.getUsuario_id()).append(", ");
			sb.append("'").append(usuario.getNombre()).append("', ");
			sb.append(usuario.getPuntos()).append(",");
			sb.append(usuario.getValor()).append(",");
			sb.append(usuario.getUltimaJornada()).append(")");

			try {
				db.execSQL(sb.toString());
			} catch (SQLException e) {
				db.delete("clasificacion", null, null);
			}
		}
		db.close();
	}

	public boolean existeFichaje(int idJugador) {

		SQLiteDatabase db = getReadableDatabase();

		String queryString = SQL_SELECT_FICHAJE + idJugador;
		Cursor cursor = db.rawQuery(queryString, null);

		if (cursor.getCount() > 0) {
			return true;
		}
		return false;
	}

	public void cleanDatabase() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("entrenador", null, null);
			db.delete("jugador", null, null);
			db.delete("fichaje", null, null);
			db.delete("clasificacion", null, null);
			db.close();
		} catch (Exception e) {

		}
	}
}