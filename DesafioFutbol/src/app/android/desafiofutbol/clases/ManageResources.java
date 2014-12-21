package app.android.desafiofutbol.clases;

import app.android.desafiofutbol.R;

public class ManageResources {

	public int getDrawableFromString(String equipo){
		
		int drawable = -1;
		switch (equipo) {
	    case "Almería":
			drawable = R.drawable.icono_alargado_almeria;
			break;
		case "Athletic":
			drawable = R.drawable.icono_alargado_athletic;
			break;
		case "Atlético":
			drawable = R.drawable.icono_alargado_atletico;
			break;
		case "Barcelona":
			drawable = R.drawable.icono_alargado_barcelona;
			break;
		case "Celta":
			drawable = R.drawable.icono_alargado_celta;
			break;
		case "Córdoba":
			drawable = R.drawable.icono_alargado_cordoba;
			break;
		case "Deportivo":
			drawable = R.drawable.icono_alargado_deportivo;
			break;
		case "Eibar":
			drawable = R.drawable.icono_alargado_eibar;
			break;
		case "Elche":
			drawable = R.drawable.icono_alargado_elche;
			break;
		case "Espanyol":
			drawable = R.drawable.icono_alargado_espanyol;
			break;
		case "Getafe":
			drawable = R.drawable.icono_alargado_getafe;
			break;
		case "Granada":
			drawable = R.drawable.icono_alargado_granada;
			break;
		case "Levante":
			drawable = R.drawable.icono_alargado_levante;
			break;
		case "Málaga":
			drawable = R.drawable.icono_alargado_malaga;
			break;
		case "Rayo Vallecano":
			drawable = R.drawable.icono_alargado_rayo;
			break;
		case "Real Madrid":
			drawable = R.drawable.icono_alargado_madrid;
			break;
		case "Real Sociedad":
			drawable = R.drawable.icono_alargado_real_sociedad;
			break;
		case "Sevilla":
			drawable = R.drawable.icono_alargado_sevilla;
			break;
		case "Valencia":
			drawable = R.drawable.icono_alargado_valencia;
			break;
		case "Villarreal":
			drawable = R.drawable.icono_alargado_villareal;
			break;
		default:
			drawable = -1;
			break;
		}		
		return drawable;
	}
	
public int getImageFromString(String equipo){
		
		int drawable = -1;
		switch (equipo) {
	    case "almeria":
			drawable = R.drawable.almeria;
			break;
		case "athletic":
			drawable = R.drawable.athletic;
			break;
		case "atletico":
			drawable = R.drawable.atletico;
			break;
		case "barcelona":
			drawable = R.drawable.barcelona;
			break;
		case "celta":
			drawable = R.drawable.celta;
			break;
		case "cordoba":
			drawable = R.drawable.cordoba;
			break;
		case "deportivo":
			drawable = R.drawable.deportivo;
			break;
		case "eibar":
			drawable = R.drawable.eibar;
			break;
		case "elche":
			drawable = R.drawable.elche;
			break;
		case "espanyol":
			drawable = R.drawable.espanyol;
			break;
		case "getafe":
			drawable = R.drawable.getafe;
			break;
		case "granada":
			drawable = R.drawable.granada;
			break;
		case "levante":
			drawable = R.drawable.levante;
			break;
		case "malaga":
			drawable = R.drawable.malaga;
			break;
		case "rayo-vallecano":
			drawable = R.drawable.rayo_vallecano;
			break;
		case "real-madrid":
			drawable = R.drawable.real_madrid;
			break;
		case "real-sociedad":
			drawable = R.drawable.real_sociedad;
			break;
		case "sevilla":
			drawable = R.drawable.sevilla;
			break;
		case "valencia":
			drawable = R.drawable.valencia;
			break;
		case "villarreal":
			drawable = R.drawable.villarreal;
			break;
		default:
			drawable = -1;
			break;
		}
		return drawable;
	}
}
