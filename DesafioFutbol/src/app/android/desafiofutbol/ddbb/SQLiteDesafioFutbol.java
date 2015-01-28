package app.android.desafiofutbol.ddbb;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import app.android.desafiofutbol.clases.Jugador;

public class SQLiteDesafioFutbol extends SQLiteOpenHelper {
    
	private static final String SQL_DROP = "DROP TABLE IF EXISTS jugador";
	private static final String SQL_CREATE = "CREATE TABLE jugador "
			+ "(id_liga INTEGER,"
			+ "id INTEGER,"
			+ "equipo_id INTEGER,"
			+ "equipo_nombre TEXT,"
			+ "puntos INTEGER, "
			+ "nombre TEXT, "
			+ "apellidos TEXT,"
			+ "apodo TEXT,"
			+ "foto TEXT,"
			+ "precio INTEGER,"
			+ "posicion TEXT,"
			+ "nacion TEXT,"
			+ "nacion_logo TEXT,"
			+ "edad INTEGER,"
			+ "titular INTEGER,"//0 false, 1 true
			+ "PRIMARY KEY (id_liga, id))";

	//Métodos de SQLiteOpenHelper
    public SQLiteDesafioFutbol(Context context) {
    	super(context, "desafiofutbol", null, 1);
    }

    @Override 
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(SQL_CREATE);
    }

    @Override    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	db.execSQL(SQL_DROP);
    	onCreate(db);
    }
    
    public void deleteTableJugador(){
    	SQLiteDatabase db = getWritableDatabase();
    	db.delete("jugador", null, null);
    }

    //Métodos de AlmacenPuntuaciones
    public void saveJugador(Jugador jugador) {
          SQLiteDatabase db = getWritableDatabase();
          
          StringBuffer sb = new StringBuffer("INSERT INTO jugador VALUES (");
          sb.append(jugador.getIdMiLiga()).append(", ");
          sb.append(jugador.getId()).append(", ");
          sb.append(jugador.getEquipoId()).append(", ");
          sb.append("'").append(jugador.getEquipo()).append("', ");
          sb.append(jugador.getPuntos()).append(", ");
          sb.append("'").append(jugador.getNombre().replaceAll("'","''")).append("', ");
          sb.append("'").append(jugador.getApellidos().replaceAll("'","''")).append("', ");
          sb.append("'").append(jugador.getApodo().replaceAll("'","''")).append("', ");
          sb.append("'").append(jugador.getUrlImagen()).append("', ");
          sb.append(jugador.getValor()).append(", ");
          sb.append("'").append(jugador.getPosicion()).append("', ");
          sb.append("'").append(jugador.getNacion()).append("', ");
          sb.append("'").append(jugador.getNacionLogo()).append("', ");
          sb.append(jugador.getEdad()).append(", ");
          sb.append(jugador.getTitular()).append(")");
          db.execSQL(sb.toString());
          
          db.close();
    }
    
    public void restartDatabase(){
    	SQLiteDatabase db = getWritableDatabase();
    	db.execSQL(SQL_DROP);
    }
   
    public ArrayList<Jugador> getTitulares(int equipoId, String posicion) {
    	return getJugadores(equipoId, 1, posicion);
    }
    
    public ArrayList<Jugador> getSuplentes(int equipoId, String posicion) {
    	return getJugadores(equipoId, 0, posicion);
    }

	private ArrayList<Jugador> getJugadores(int equipoId, int titular, String posicion) {
		ArrayList<Jugador> result = new ArrayList<Jugador>();
    	SQLiteDatabase db = getReadableDatabase();
    	String query = null;
    	if (posicion == null){
    		query = "SELECT apodo, posicion, equipo_nombre, puntos FROM jugador WHERE titular="+titular+" and id_liga="+equipoId;
    	}
    	else{
    		query = "SELECT apodo, posicion, equipo_nombre, puntos FROM jugador WHERE titular="+titular+" and id_liga="+equipoId+" and posicion='"+posicion+"'";
    	}
    	
    	Cursor cursor = db.rawQuery(query, null);
    	Jugador j = null;
    	while (cursor.moveToNext()){
    		j = new Jugador();
    		j.setApodo(cursor.getString(0));
    		j.setPosicion(cursor.getString(1));
    		j.setEquipo(cursor.getString(2));
    		j.setPuntos(cursor.getInt(3));
    		result.add(j);
    	}
    	cursor.close();
    	db.close();
    	return result;
	}
}