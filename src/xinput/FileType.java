package xinput;

public class FileType {
	/**
	 *  File in a normal filesystem
	 */
	public final static FileType FILE = new FileType(1, "FILE");
	/**
	 *  File on a ftp server
	 */
	public final static FileType FTP = new FileType(2, "FTP");
	/**
	 *  Directory in a normal filesystem
	 */
	public final static FileType FILE_DIR = new FileType(3, "FILE_DIR");
	/**
	 *  Directory on a ftp server
	 */
	public final static FileType FTP_DIR = new FileType(4, "FTP_DIR");

	private int type;
	private String name;

	private FileType(int aType, String aName) {
		type = aType;
		name = aName;
	}

	/**
	 * Get type name
	 * @return type name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get type value
	 * @return type value
	 */
	public int getType() {
		return type;
	}

}
