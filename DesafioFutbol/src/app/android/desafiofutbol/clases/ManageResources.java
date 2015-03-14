package app.android.desafiofutbol.clases;

import java.util.HashMap;

import android.graphics.Bitmap;
import app.android.desafiofutbol.R;

public final class ManageResources {
	
	private static HashMap<String, Integer> mapEquipoIconoAlargado;
	private static HashMap<String, Integer> mapEquipoIcono;
	private static HashMap<String, Bitmap> mapImagenJugador;
	

	public static int getDrawableFromString(String equipo){
		return mapEquipoIconoAlargado.get(equipo)==null ? -1 : mapEquipoIconoAlargado.get(equipo).intValue();
	}
	
	public static int getImageFromString(String equipo){
		return mapEquipoIcono.get(equipo)==null ? mapEquipoIcono.get("desconocido").intValue() : mapEquipoIcono.get(equipo).intValue();
	}
	
	public static Bitmap getImageJugadorFromUrl(String url){
		if (mapImagenJugador == null){
			mapImagenJugador = new HashMap<String, Bitmap>();
		}
		return mapImagenJugador.get(url)== null ? null : mapImagenJugador.get(url);
	}
	
	public static void addImagenJugador(String url, Bitmap bm){
		mapImagenJugador.put(url, bm);
	}

	public static void inicilalizaMapIconoAlargado(){
		mapEquipoIconoAlargado = new HashMap<String, Integer>();
		mapEquipoIconoAlargado.put("Almería",R.drawable.icono_alargado_almeria);			
		mapEquipoIconoAlargado.put("Athletic",R.drawable.icono_alargado_athletic);			
		mapEquipoIconoAlargado.put("Atlético",R.drawable.icono_alargado_atletico);			
		mapEquipoIconoAlargado.put("Barcelona",R.drawable.icono_alargado_barcelona);			
		mapEquipoIconoAlargado.put("Celta",R.drawable.icono_alargado_celta);			
		mapEquipoIconoAlargado.put("Córdoba",R.drawable.icono_alargado_cordoba);			
		mapEquipoIconoAlargado.put("Deportivo",R.drawable.icono_alargado_deportivo);			
		mapEquipoIconoAlargado.put("Eibar",R.drawable.icono_alargado_eibar);			
		mapEquipoIconoAlargado.put("Elche",R.drawable.icono_alargado_elche);			
		mapEquipoIconoAlargado.put("Espanyol",R.drawable.icono_alargado_espanyol);			
		mapEquipoIconoAlargado.put("Getafe",R.drawable.icono_alargado_getafe);			
		mapEquipoIconoAlargado.put("Granada",R.drawable.icono_alargado_granada);			
		mapEquipoIconoAlargado.put("Levante",R.drawable.icono_alargado_levante);			
		mapEquipoIconoAlargado.put("Málaga",R.drawable.icono_alargado_malaga);			
		mapEquipoIconoAlargado.put("Rayo Vallecano",R.drawable.icono_alargado_rayo);			
		mapEquipoIconoAlargado.put("Real Madrid",R.drawable.icono_alargado_madrid);			
		mapEquipoIconoAlargado.put("Real Sociedad",R.drawable.icono_alargado_real_sociedad);			
		mapEquipoIconoAlargado.put("Sevilla",R.drawable.icono_alargado_sevilla);			
		mapEquipoIconoAlargado.put("Valencia",R.drawable.icono_alargado_valencia);			
		mapEquipoIconoAlargado.put("Villarreal",R.drawable.icono_alargado_villareal);		
	}
	
	public static void inicilalizaMapIcono(){		
		mapEquipoIcono = new HashMap<String, Integer>();
		mapEquipoIcono.put("almeria",R.drawable.almeria);
		mapEquipoIcono.put("athletic",R.drawable.athletic);		
		mapEquipoIcono.put("atletico",R.drawable.atletico);		
		mapEquipoIcono.put("barcelona",R.drawable.barcelona);		
		mapEquipoIcono.put("celta",R.drawable.celta);		
		mapEquipoIcono.put("cordoba",R.drawable.cordoba);		
		mapEquipoIcono.put("deportivo",R.drawable.deportivo);		
		mapEquipoIcono.put("eibar",R.drawable.eibar);		
		mapEquipoIcono.put("elche",R.drawable.elche);		
		mapEquipoIcono.put("espanyol",R.drawable.espanyol);		
		mapEquipoIcono.put("getafe",R.drawable.getafe);		
		mapEquipoIcono.put("granada",R.drawable.granada);		
		mapEquipoIcono.put("levante",R.drawable.levante);		
		mapEquipoIcono.put("malaga",R.drawable.malaga);		
		mapEquipoIcono.put("rayo-vallecano",R.drawable.rayo_vallecano);		
		mapEquipoIcono.put("real-madrid",R.drawable.real_madrid);		
		mapEquipoIcono.put("real-sociedad",R.drawable.real_sociedad);		
		mapEquipoIcono.put("sevilla",R.drawable.sevilla);		
		mapEquipoIcono.put("valencia",R.drawable.valencia);		
		mapEquipoIcono.put("villarreal",R.drawable.villarreal);
		mapEquipoIcono.put("desconocido",R.drawable.base_player);
	}

	public static void inicializa() {
		inicilalizaMapIconoAlargado();
		inicilalizaMapIcono();		
	}
}
