package xinput;

import java.io.File;
import java.io.FileFilter;

public class XInputDirectory {
	private FileType fileType = null;

	private File file = null;
	private String testMsg = null;

	private FtpVO ftpVO = null;
	private FtpServer ftpServer = null;

	/**
	 * Mustn't be used
	 * @throws UnsupportedOperationException Because it mustn't be used!
	 */
	private XInputDirectory() {
		throw new UnsupportedOperationException("Usage is not allowed!");
	}

	/**
	 * Create a XInputDirectory of type FileType.FILE_DIR.
	 * @param aFile Directory data to use
	 * @throws IllegalArgumentException If aFile is not a directory
	 */
	public XInputDirectory(File aFile) {
		if (!aFile.isDirectory()) {
			throw new IllegalArgumentException("aFile is not a directory!");
		}
		file = aFile;
		fileType = FileType.FILE_DIR;
	}

	/**
	 * Create a XInputDirectory of type FileType.FTP_DIR.
	 * @param aFtpVO Directory data to use
	 */
	public XInputDirectory(FtpVO aFtpVO) {
		ftpVO = aFtpVO;
		ftpServer = new FtpServer(ftpVO);
		fileType = FileType.FTP_DIR;
	}

	/**
	 * Create a XInputDirectory of type FileType.FILE_DIR.
	 * @param aFile Directory data to use
	 * @throws IllegalArgumentException If aFile is not a directory
	 */
	public XInputDirectory(String aFileIdentifier) {
		
		if (aFileIdentifier.startsWith("ftp://")) {
			int posColon = aFileIdentifier.indexOf(':', 6);
			int posAt = aFileIdentifier.indexOf('@', posColon);
			int posSlash = aFileIdentifier.indexOf('/', posAt);
			
			if (posAt == -1 || posColon == -1 || posSlash == -1) {
				throw new IllegalArgumentException("aFileIdentifier is a malformed ftp URL!");
			}
							
			String user      = aFileIdentifier.substring(6, posColon);
			String password  = aFileIdentifier.substring(posColon + 1, posAt);
			String server    = aFileIdentifier.substring(posAt + 1, posSlash);
			String directory = aFileIdentifier.substring(posSlash);

/*
			System.out.println("url       = " + aFileIdentifier);
			System.out.println("user      = " + user);
			System.out.println("password  = " + password);
			System.out.println("server    = " + server);
			System.out.println("directory = " + directory);
 */
 
			ftpVO = new FtpVO(server, user, password, directory);
			ftpServer = new FtpServer(ftpVO);
			fileType = FileType.FTP_DIR;

			if (!test()) {
				ftpVO = null;
				ftpServer = null;
				fileType = null;
				throw new IllegalArgumentException("aFileIdentifier is not a correct ftp URL!");
			}	
		} else {
			File f = new File(aFileIdentifier);
			if (f.isDirectory()) {
				fileType = FileType.FILE_DIR;
				file = f;
			} else {
				throw new IllegalArgumentException("'" + aFileIdentifier + "' is not a directory!");
			} 
		}
	}

	/**
	 * Get String representation of the object.
	 * @return String representation of the object
	 */
	public String toString() {

		String s = null;

		if (fileType == FileType.FILE_DIR) {
			s = file.getAbsolutePath();
		}

		if (fileType == FileType.FTP_DIR) {
			s = "ftp://" + ftpVO.getUser() + ":" + ftpVO.getPassword() + "@" + ftpVO.getServer() + ftpVO.getDirectory();
		}
		return s;
	}

	/**
	 * Get path of directory
	 * @return Path of directory
	 */
	public String getDirectory() {
		String s = null;

		if (fileType == FileType.FILE_DIR) {
			s = file.getParent();
		}

		if (fileType == FileType.FTP_DIR) {
			s = ftpVO.getDirectory();
		}
		return s;
	}

	/**
	 * Get password for the ftp server
	 * @return Password for the ftp server
	 * @throws IllegalStateException If file type of object is not FileType.FTP_DIR
	 */
	public String getPassword() {
		String s = null;

		if (fileType == FileType.FILE_DIR) {
			throw new IllegalStateException("fileType is not FileType.FTP_DIR!");
		}

		if (fileType == FileType.FTP_DIR) {
			s = ftpVO.getPassword();
		}
		return s;
	}

	/**
	 * Get name or ip address of the ftp server
	 * @return Name or ip address of the ftp server
	 * @throws IllegalStateException If file type of object is not FileType.FTP_DIR
	 */
	public String getServer() {
		String s = null;

		if (fileType == FileType.FILE_DIR) {
			throw new IllegalStateException("fileType is not FileType.FTP_DIR!");
		}

		if (fileType == FileType.FTP_DIR) {
			s = ftpVO.getServer();
		}
		return s;
	}

	/**
	 * Get user for the ftp server
	 * @return User for the ftp server
	 * @throws IllegalStateException If file type of object is not FileType.FTP_DIR
	 */
	public String getUser() {
		String s = null;

		if (fileType == FileType.FILE_DIR) {
			throw new IllegalStateException("fileType is not FileType.FTP_DIR!");
		}

		if (fileType == FileType.FTP_DIR) {
			s = ftpVO.getUser();
		}
		return s;
	}

	/**
	 * Get log of communication with ftp server.
	 * @return Log of communication with ftp server
	 * @throws IllegalStateException If file type of object is not FileType.FTP_DIR
	 */
	public String getLog() {
		String s = null;

		if (fileType == FileType.FILE_DIR) {
			throw new IllegalStateException("fileType is not FileType.FTP_DIR!");
		}

		if (fileType == FileType.FTP_DIR) {
			s = ftpServer.getLog();
		}
		return s;
	}

	/**
	 * Get files in the directory.
	 * @return files in the directory
	 */
	public XInputFile[] getFiles() {
		XInputFile[] xInputFiles = null;

		if (fileType == FileType.FILE_DIR) {
			class MyFileFilter implements FileFilter {
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			}
			File[] files = file.listFiles(new MyFileFilter());
			xInputFiles = new XInputFile[files.length];
			for (int i = 0; i < files.length; i++) {
				xInputFiles[i] = new XInputFile(files[i]);
			}
		}

		if (fileType == FileType.FTP_DIR) {
			ftpServer.open();
			xInputFiles = ftpServer.listFiles();
			ftpServer.close();
		}
		return xInputFiles;
	}

	/**
	 * Test if directory data is valid.
	 * @return Test successful or not
	 */
	public boolean test() {
		boolean b = false;

		if (fileType == FileType.FILE_DIR) {
			b = (file.exists() && file.isDirectory()) ? true : false;
			testMsg = b ? "Test succeeded" : "Test failed";
		}

		if (fileType == FileType.FTP_DIR) {
			b = ftpServer.test();
		}
		return b;
	}

	/**
	 * Get result message after test().
	 * @return result message after test()
	 */
	public String getTestMsg() {
		String s = null;

		if (fileType == FileType.FILE_DIR) {
			s = testMsg;
		}

		if (fileType == FileType.FTP_DIR) {
			s = ftpServer.getTestMsg();
		}
		return s;
	}

	/**
	 * @return
	 */
	public FileType getFileType() {
		return fileType;
	}

}
