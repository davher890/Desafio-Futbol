package app.android.desafiofutbol.clases;

public class DatosUsuario {

	private static String token = null;
	private static String userName = null;
	private static String nombreEquipo = null;
	private static int userId = -1;
	private static String avatar = null;
	private static int p_rank = -1;
	private static int idEquipoSeleccionado = -1;

	public DatosUsuario() {
	}

	/**
	 * @return the token
	 */
	public static String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public static void setToken(String token) {
		DatosUsuario.token = token;
	}

	/**
	 * @return the userName
	 */
	public static String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public static void setUserName(String userName) {
		DatosUsuario.userName = userName;
	}

	/**
	 * @return the userId
	 */
	public static int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public static void setUserId(int userId) {
		DatosUsuario.userId = userId;
	}

	/**
	 * @return the avatar
	 */
	public static String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public static void setAvatar(String avatar) {
		DatosUsuario.avatar = avatar;
	}

	public static int getP_rank() {
		return p_rank;
	}

	public static void setP_rank(int p_rank) {
		DatosUsuario.p_rank = p_rank;
	}

	public static int getIdEquipoSeleccionado() {
		return idEquipoSeleccionado;
	}

	public static void setIdEquipoSeleccionado(int idEquipoSeleccionado) {
		DatosUsuario.idEquipoSeleccionado = idEquipoSeleccionado;
	}

	/**
	 * @return the nombreEquipo
	 */
	public static String getNombreEquipo() {
		return nombreEquipo;
	}

	/**
	 * @param nombreEquipo
	 *            the nombreEquipo to set
	 */
	public static void setNombreEquipo(String nombreEquipo) {
		DatosUsuario.nombreEquipo = nombreEquipo;
	}

	public static boolean checkData() {

		if (DatosUsuario.getToken() != null && DatosUsuario.getUserName() != null && DatosUsuario.getAvatar() != null
				&& DatosUsuario.getP_rank() != -1 && DatosUsuario.getUserId() != -1) {
			return true;
		}
		return false;
	}
}
