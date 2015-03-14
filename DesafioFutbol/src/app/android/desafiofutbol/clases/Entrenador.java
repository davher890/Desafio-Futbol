package app.android.desafiofutbol.clases;


public class Entrenador{
	
	private int id = -1;
	private String nombre = null;
	private String equipo = null;
	private int salario = -1;
	private int puntos = 0;
	private String propietario = null;
	
	public Entrenador(int id, String nombre, String equipo, int salario,
			int puntos, String propietario) {
		this.id = id;
		this.nombre = nombre;
		this.equipo = equipo;
		this.salario = salario;
		this.puntos = puntos;
		this.propietario = propietario;
	}
	
	public Entrenador(){		
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
	 * @return the equipo
	 */
	public String getEquipo() {
		return equipo;
	}

	/**
	 * @param equipo the equipo to set
	 */
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	/**
	 * @return the salario
	 */
	public int getSalario() {
		return salario;
	}

	/**
	 * @param salario the salario to set
	 */
	public void setSalario(double salario) {
		this.salario = (int)Math.round(salario);
	}

	/**
	 * @return the puntos
	 */
	public int getPuntos() {
		return puntos;
	}

	/**
	 * @param puntos the puntos to set
	 */
	public void setPuntos(double puntos) {
		this.puntos = (int)Math.round(puntos);
	}

	/**
	 * @return the propietario
	 */
	public String getPropietario() {
		return propietario;
	}

	/**
	 * @param propietario the propietario to set
	 */
	public void setPropietario(String propietario) {
		this.propietario = propietario;
	} 
	
	
}
