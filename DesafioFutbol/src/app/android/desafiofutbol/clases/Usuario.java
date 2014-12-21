package app.android.desafiofutbol.clases;

public class Usuario {
	
	private static String token = null;
	private static String userName = null;
	private static int userId = -1;
	private static String avatar = null;
	private static int p_rank = -1;
	private static int idEquipoSeleccionado = -1;
	
	public Usuario(){
	}

	/**
	 * @return the token
	 */
	public static String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public static void setToken(String token) {
		Usuario.token = token;
	}

	/**
	 * @return the userName
	 */
	public static String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public static void setUserName(String userName) {
		Usuario.userName = userName;
	}

	/**
	 * @return the userId
	 */
	public static int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public static void setUserId(int userId) {
		Usuario.userId = userId;
	}

	/**
	 * @return the avatar
	 */
	public static String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public static void setAvatar(String avatar) {
		Usuario.avatar = avatar;
	}

	public static int getP_rank() {
		return p_rank;
	}

	public static void setP_rank(int p_rank) {
		Usuario.p_rank = p_rank;
	}

	public static int getIdEquipoSeleccionado() {
		return idEquipoSeleccionado;
	}

	public static void setIdEquipoSeleccionado(int idEquipoSeleccionado) {
		Usuario.idEquipoSeleccionado = idEquipoSeleccionado;
	}

	

}
