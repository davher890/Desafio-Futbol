package app.android.desafiofutbol;

import java.util.Comparator;

import app.android.desafiofutbol.clases.Jugador;

public class JugadoresComparator implements Comparator<Jugador> {

	private SortTypes compare;

	public JugadoresComparator(SortTypes puntos) {
		super();
		this.compare = puntos;
	}

	@Override
	public int compare(Jugador jugador1, Jugador jugador2) {

		int sort;
		switch (compare) {
		case nombre:
			sort = jugador1.getApodo().compareTo(jugador2.getApodo());
			break;
		case posicion:
			sort = jugador2.getPosicion().compareTo(jugador1.getPosicion());
			break;
		case equipo:
			sort = jugador1.getEquipo().compareTo(jugador2.getEquipo());
			break;
		case valor:
			sort = jugador2.getValor() - jugador1.getValor();
			break;
		case puntos:
			sort = jugador2.getPuntos() - jugador1.getPuntos();
			break;
		default:
			sort = 0;
			break;
		}
		return sort;
	}
}