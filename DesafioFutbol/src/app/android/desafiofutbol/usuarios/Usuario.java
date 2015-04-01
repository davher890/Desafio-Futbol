package app.android.desafiofutbol.usuarios;

public class Usuario {
	
	String nombre = null;
	int id = -1;
	String avatar = null;
	int numEquipos = -1;
	int ranking = -1;

	public Usuario(String nombre, int id, String avatar, int numEquipos,
			int ranking) {
		super();
		this.nombre = nombre;
		this.id = id;
		this.avatar = avatar;
		this.numEquipos = numEquipos;
		this.ranking = ranking;
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
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the numEquipos
	 */
	public int getNumEquipos() {
		return numEquipos;
	}

	/**
	 * @param numEquipos the numEquipos to set
	 */
	public void setNumEquipos(int numEquipos) {
		this.numEquipos = numEquipos;
	}

	/**
	 * @return the ranking
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
}
