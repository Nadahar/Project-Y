package xinput;

class FtpVO {
	private String server = null;
	private String user = null;
	private String password = null;
	private String directory = null;

	public FtpVO(String server, String user, String password, String directory) {
		this.server = server;
		this.user = user;
		this.password = password;
		this.directory = directory;
	}

	public void reset() {
		server = null;
		user = null;
		password = null;
		directory = null;
	}

	public String toString() {
		return "ftp://|" + server + "|" + directory + "|" + user + "|" + password;
	}

	public void fromString(String string) {
		String[] tokens = string.split("|");
		server = tokens[1];
		directory = tokens[2];
		user = tokens[3];
		password = tokens[4];
	}

	/**
	 * @return Returns the directory.
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * @param directory The directory to set.
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the server.
	 */
	public String getServer() {
		return server;
	}
	/**
	 * @param server The server to set.
	 */
	public void setServer(String server) {
		this.server = server;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
}