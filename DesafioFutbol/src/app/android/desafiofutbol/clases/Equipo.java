package app.android.desafiofutbol.clases;


public class Equipo {
	
	private int id = -1;
	private boolean activa = false;
	private String nombre = null;
	private int saldo = 0;
	private int valor = 0;
	
	private int ligaId = -1;
	private String ligaNombre = null;
	
	public Equipo(int id, String nombre, int ligaId, String ligaNombre,
			int saldo, int valor) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ligaId = ligaId;
		this.ligaNombre = ligaNombre;
		this.saldo = saldo;
		this.valor = valor;
	}
	
	public Equipo(){
		
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
	 * @return the ligaId
	 */
	public int getLigaId() {
		return ligaId;
	}

	/**
	 * @param ligaId the ligaId to set
	 */
	public void setLigaId(int ligaId) {
		this.ligaId = ligaId;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	/**
	 * @return the ligaNombre
	 */
	public String getLigaNombre() {
		return ligaNombre;
	}

	/**
	 * @param ligaNombre the ligaNombre to set
	 */
	public void setLigaNombre(String ligaNombre) {
		this.ligaNombre = ligaNombre;
	}

	/**
	 * @return the saldo
	 */
	public int getSaldo() {
		return saldo;
	}

	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(int saldo) {
		this.saldo = saldo;
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
}
