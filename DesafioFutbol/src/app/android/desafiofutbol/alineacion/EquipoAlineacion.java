package app.android.desafiofutbol.alineacion;

import java.util.ArrayList;

import app.android.desafiofutbol.clases.Jugador;

public class EquipoAlineacion {

	private ArrayList<Jugador> porterosTitulares = null;
	private ArrayList<Jugador> defensasTitulares = null;
	private ArrayList<Jugador> mediosTitulares = null;
	private ArrayList<Jugador> delanterosTitulares = null;
	private ArrayList<Jugador> porterosSuplentes = null;
	private ArrayList<Jugador> defensasSuplentes = null;
	private ArrayList<Jugador> mediosSuplentes = null;
	private ArrayList<Jugador> delanterosSuplentes = null;

	private String tactica = null;

	public EquipoAlineacion(final ArrayList<Jugador> jugadores) {

		/*
		 * Thread thread = new Thread(){ public void run(){
		 */
		creaEquipo(jugadores);
		/*
		 * }}; thread.start();
		 */
	}

	private void creaEquipo(ArrayList<Jugador> jugadores) {

		// Jugadores titulares
		porterosTitulares = new ArrayList<Jugador>();
		defensasTitulares = new ArrayList<Jugador>();
		mediosTitulares = new ArrayList<Jugador>();
		delanterosTitulares = new ArrayList<Jugador>();

		// Jugadores suplentes
		porterosSuplentes = new ArrayList<Jugador>();
		defensasSuplentes = new ArrayList<Jugador>();
		mediosSuplentes = new ArrayList<Jugador>();
		delanterosSuplentes = new ArrayList<Jugador>();

		for (Jugador jugador : jugadores) {

			if (jugador.isTitular()) {
				if (jugador.getPosicion().equals("Portero")) {
					porterosTitulares.add(jugador);
				} else if (jugador.getPosicion().equals("Defensa")) {
					defensasTitulares.add(jugador);
				} else if (jugador.getPosicion().equals("Medio")) {
					mediosTitulares.add(jugador);
				} else if (jugador.getPosicion().equals("Delantero")) {
					delanterosTitulares.add(jugador);
				}
			} else if (!jugador.isTitular()) {
				if (jugador.getPosicion().equals("Portero")) {
					porterosSuplentes.add(jugador);
				} else if (jugador.getPosicion().equals("Defensa")) {
					defensasSuplentes.add(jugador);
				} else if (jugador.getPosicion().equals("Medio")) {
					mediosSuplentes.add(jugador);
				} else if (jugador.getPosicion().equals("Delantero")) {
					delanterosSuplentes.add(jugador);
				}
			}
		}
		if (porterosTitulares.size() == 0) {
			porterosTitulares.add(new Jugador());
		}
		if (defensasTitulares.size() == 0) {
			defensasTitulares.add(new Jugador());
		}
		if (mediosTitulares.size() == 0) {
			mediosTitulares.add(new Jugador());
		}
		if (delanterosTitulares.size() == 0) {
			delanterosTitulares.add(new Jugador());
		}
		if (porterosSuplentes.size() == 0) {
			porterosSuplentes.add(new Jugador());
		}
		if (defensasSuplentes.size() == 0) {
			defensasSuplentes.add(new Jugador());
		}
		if (mediosSuplentes.size() == 0) {
			mediosSuplentes.add(new Jugador());
		}
		if (delanterosSuplentes.size() == 0) {
			delanterosSuplentes.add(new Jugador());
		}

		setTactica(new StringBuffer().append(defensasTitulares.size()).append(mediosTitulares.size()).append(delanterosTitulares.size()).toString());
	}

	/**
	 * @return the porterosTitulares
	 */
	public ArrayList<Jugador> getPorterosTitulares() {
		return porterosTitulares;
	}

	/**
	 * @return the defensasTitulares
	 */
	public ArrayList<Jugador> getDefensasTitulares() {
		return defensasTitulares;
	}

	/**
	 * @return the mediosTitulares
	 */
	public ArrayList<Jugador> getMediosTitulares() {
		return mediosTitulares;
	}

	/**
	 * @return the delanterosTitulares
	 */
	public ArrayList<Jugador> getDelanterosTitulares() {
		return delanterosTitulares;
	}

	/**
	 * @return the porterosSuplentes
	 */
	public ArrayList<Jugador> getPorterosSuplentes() {
		return porterosSuplentes;
	}

	/**
	 * @return the defensasSuplentes
	 */
	public ArrayList<Jugador> getDefensasSuplentes() {
		return defensasSuplentes;
	}

	/**
	 * @return the mediosSuplentes
	 */
	public ArrayList<Jugador> getMediosSuplentes() {
		return mediosSuplentes;
	}

	/**
	 * @return the delanterosSuplentes
	 */
	public ArrayList<Jugador> getDelanterosSuplentes() {
		return delanterosSuplentes;
	}

	/**
	 * @return the tactica
	 */
	public String getTactica() {
		return tactica;
	}

	/**
	 * @param tactica the tactica to set
	 */
	public void setTactica(String tactica) {
		this.tactica = tactica;
	}
}