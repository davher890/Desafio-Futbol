/**
 * 
 */
package app.android.desafiofutbol;

/**
 * @author usuario
 *
 */
public class ItemObject {

	private String titulo;
	private int icono;

	/**
	 * @param titulo
	 * @param icono
	 */
	public ItemObject(String titulo, int icono) {
		super();
		this.titulo = titulo;
		this.icono = icono;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo
	 *            the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the icono
	 */
	public int getIcono() {
		return icono;
	}

	/**
	 * @param icono
	 *            the icono to set
	 */
	public void setIcono(int icono) {
		this.icono = icono;
	}
}
