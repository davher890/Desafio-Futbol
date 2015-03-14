package app.android.desafiofutbol.clasificacion;

import java.util.HashMap;

public class UsuarioClasificacion {
	
	private String usuario = null;
	private int usuario_id = -1;
	private String nombre = null;
	private int id = -1;
	private int valor = 0;
	private int puntos = 0;
	private HashMap<Integer, String> jornadas = null;
	private int ultimaJornada = 0; 
	
	public UsuarioClasificacion(String usuario, int usuario_id,
			String nombre, int id, int valor, int puntos) {
		super();
		this.usuario = usuario;
		this.usuario_id = usuario_id;
		this.nombre = nombre;
		this.id = id;
		this.valor = valor;
		this.puntos = puntos;
	}

	public UsuarioClasificacion() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the usuario_id
	 */
	public int getUsuario_id() {
		return usuario_id;
	}

	/**
	 * @param usuario_id the usuario_id to set
	 */
	public void setUsuario_id(int usuario_id) {
		this.usuario_id = usuario_id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the valor
	 */
	public int getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = (int)Math.round(valor);
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	/**
	 * @return the jornadas
	 */
	public HashMap<Integer, String> getJornadas() {
		return jornadas;
	}

	/**
	 * @param jornadas the jornadas to set
	 */
	public void setJornadas(HashMap<Integer, String> jornadas) {
		this.jornadas = jornadas;
	}

	public int getUltimaJornada() {
		return ultimaJornada;
	}

	public void setUltimaJornada(int ultimaJornada) {
		this.ultimaJornada = ultimaJornada;
	}
}
