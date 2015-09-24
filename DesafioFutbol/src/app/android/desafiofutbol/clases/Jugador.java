package app.android.desafiofutbol.clases;

public class Jugador {

	private int id = -1;
	private String apodo = null;
	private String nombre = null;
	private String apellidos = null;
	private int valor = -1;
	private int puntos = -1;
	private String propietarioNombre = null;
	private int propietarioId = -1;
	private String equipo = null;
	private int equipoId = -1;
	private String posicion = null;
	private String urlImagen = null;
	private int miOferta = -1;
	private int drawableEquipo = -1;
	private String nacion = null;
	private String nacionLogo = null;
	private int edad = 0;
	private int titular = 0;
	private String idMercado = null;

	public Jugador() {

	}

	public void clear() {
		apodo = null;
		nombre = null;
		apellidos = null;
		valor = -1;
		puntos = -1;
		propietarioNombre = null;
		propietarioId = -1;
		equipo = null;
		equipoId = -1;
		posicion = null;
		urlImagen = null;
		miOferta = -1;
		drawableEquipo = -1;
		nacion = null;
		nacionLogo = null;
		edad = 0;
		titular = 0;
		idMercado = null;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the apodo
	 */
	public String getApodo() {
		return apodo;
	}

	/**
	 * @param apodo
	 *            the apodo to set
	 */
	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	/**
	 * @return the valor
	 */
	public int getValor() {
		return valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(double valor) {
		this.valor = (int) Math.round(valor);
	}

	/**
	 * @return the puntos
	 */
	public int getPuntos() {
		return puntos;
	}

	/**
	 * @param puntos
	 *            the puntos to set
	 */
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	/**
	 * @return the propietarioNombre
	 */
	public String getPropietarioNombre() {
		return propietarioNombre;
	}

	/**
	 * @param propietarioNombre
	 *            the propietarioNombre to set
	 */
	public void setPropietarioNombre(String propietarioNombre) {
		this.propietarioNombre = propietarioNombre;
	}

	/**
	 * @return the propietarioId
	 */
	public int getPropietarioId() {
		return propietarioId;
	}

	/**
	 * @param propietarioId
	 *            the propietarioId to set
	 */
	public void setPropietarioId(int propietarioId) {
		this.propietarioId = propietarioId;
	}

	/**
	 * @return the equipo
	 */
	public String getEquipo() {
		return equipo;
	}

	/**
	 * @param equipo
	 *            the equipo to set
	 */
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	/**
	 * @return the posicion
	 */
	public String getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion
	 *            the posicion to set
	 */
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the urlImagen
	 */
	public String getUrlImagen() {
		return urlImagen;
	}

	/**
	 * @param urlImagen
	 *            the urlImagen to set
	 */
	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	/**
	 * @return the miOferta
	 */
	public int getMiOferta() {
		return miOferta;
	}

	/**
	 * @param miOferta
	 *            the miOferta to set
	 */
	public void setMiOferta(int miOferta) {
		this.miOferta = miOferta;
		// this.miOferta = (int)Math.round(miOferta);
	}

	/**
	 * @return the drawableEquipo
	 */
	public int getDrawableEquipo() {
		return drawableEquipo;
	}

	/**
	 * @param drawableEquipo
	 *            the drawableEquipo to set
	 */
	public void setDrawableEquipo(int drawableEquipo) {
		this.drawableEquipo = drawableEquipo;
	}

	/**
	 * @return the equipoId
	 */
	public int getEquipoId() {
		return equipoId;
	}

	/**
	 * @param equipoId
	 *            the equipoId to set
	 */
	public void setEquipoId(int equipoId) {
		this.equipoId = equipoId;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * @param apellidos
	 *            the apellidos to set
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @return the nacion
	 */
	public String getNacion() {
		return nacion;
	}

	/**
	 * @param nacion
	 *            the nacion to set
	 */
	public void setNacion(String nacion) {
		this.nacion = nacion;
	}

	/**
	 * @return the nacionLogo
	 */
	public String getNacionLogo() {
		return nacionLogo;
	}

	/**
	 * @param nacionLogo
	 *            the nacionLogo to set
	 */
	public void setNacionLogo(String nacionLogo) {
		this.nacionLogo = nacionLogo;
	}

	/**
	 * @return the edad
	 */
	public int getEdad() {
		return edad;
	}

	/**
	 * @param edad
	 *            the edad to set
	 */
	public void setEdad(int edad) {
		this.edad = edad;
	}

	/**
	 * @return the titular
	 */
	public int getTitular() {
		return titular;
	}

	/**
	 * @param titular
	 *            the titular to set
	 */
	public void setTitular(int titular) {
		this.titular = titular;
	}

	/**
	 * @return the idMercado
	 */
	public String getIdMercado() {
		return idMercado;
	}

	/**
	 * @param idMercado
	 *            the idMercado to set
	 */
	public void setIdMercado(String idMercado) {
		this.idMercado = idMercado;
	}

	public boolean isTitular() {
		if (this.titular == 1) {
			return true;
		} else {
			return false;
		}
	}
}