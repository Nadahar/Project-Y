package xinput.ftp;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.net.ftp.FTPFile;

import xinput.*;
import xinput.ftp.FtpVO;

public class XInputFileImpl implements XInputFileIF {

	private boolean debug = false;
	
	// Members, which are type independent
	private FileType fileType = null;
	private boolean isopen = false;
	private InputStream inputStream = null;

	// Members used for type FileType.FTP
	private FtpVO ftpVO = null;
	private FTPFile ftpFile = null;

	/**
	 * Private Constructor, don't use!
	 */
	private XInputFileImpl() {

		throw new UnsupportedOperationException();
	}

	/**
	 * Create a XInputFile of type FileType.FTP.
	 * 
	 * @param aFtpVO
	 *            Directory data to use
	 * @param aFtpFile
	 *            File data to use
	 */
	public XInputFileImpl(FtpVO aFtpVO) {

		if (debug) System.out.println("Try to create XInputFile of Type FTP");

		ftpVO = aFtpVO;
		ftpFile = aFtpVO.getFtpFile();
		fileType = FileType.FTP;
	
		if (!exists()) {
			throw new IllegalArgumentException("File doesn't exist");
		}

		if (debug) System.out.println("Succeeded to create XInputFile of Type FTP");
}

	/**
	 * Get String representation of the object.
	 * 
	 * @return String representation of the object
	 */
	public String toString() {

		String s;

		String name = ftpFile.getName();
		name = name.replaceAll("Ã¤", "ä");
		name = name.replaceAll("Ã¶", "ö");
		name = name.replaceAll("Ã¼", "ü");
		name = name.replaceAll("Ã„", "Ä");
		name = name.replaceAll("Ã–", "Ö");
		name = name.replaceAll("Ãœ", "Ü");
		name = name.replaceAll("ÃŸ", "ß");
		name = name.replaceAll("Ã¡", "á");
		name = name.replaceAll("Ã ", "à");
		name = name.replaceAll("Ã©", "é");
		name = name.replaceAll("Ã¨", "è");
		name = name.replaceAll("Ã­", "í");
		name = name.replaceAll("Ã¬", "ì");
		name = name.replaceAll("Ã³", "ó");
		name = name.replaceAll("Ã²", "ò");
		name = name.replaceAll("Ãº", "ú");
		name = name.replaceAll("Ã¹", "ù");

		s = "ftp://" + ftpVO.getUser() + ":" + ftpVO.getPassword() + "@"
				+ ftpVO.getServer() + ftpVO.getDirectory() + "/" + name;

		return s;
	}

	/**
	 * Get url representation of the object.
	 * 
	 * @return String with url
	 */
	public String getUrl() {

		String s;

		s = "ftp://" + ftpVO.getUser() + ":" + ftpVO.getPassword() + "@"
				+ ftpVO.getServer() + ftpVO.getDirectory() + "/"
				+ ftpFile.getName() + ";type=b";

		return s;
	}

	/**
	 * Length of file in bytes.
	 * 
	 * @return Length of file in bytes
	 */
	public long length() {

		return ftpFile.getSize();
	}

	/**
	 * Time in milliseconds from the epoch.
	 * 
	 * @return Time in milliseconds from the epoch
	 */
	public long lastModified() {

		return ftpFile.getTimestamp().getTimeInMillis();
	}

	/**
	 * Checks if file exists
	 * 
	 * @return Result of check
	 */
	public boolean exists() {

		boolean b = false;

// This method is more exact, but too expensive		
//		try {
//			b = true;
//			inputStream = getInputStream();
//			inputStream.close();
//			inputStream = null;
//		} catch (Exception e) {
//			b = false;
//		}

		// If ftpFile is set, it was possible to retrieve it, so the file exists
		if (ftpFile != null) {
			b = true;
		}
		
		return b;
	}

	/**
	 * Get Name of file
	 * 
	 * @return Name of file
	 */
	public String getName() {

		String s = null;

		s = ftpFile.getName();
		s = s.replaceAll("Ã¤", "ä");
		s = s.replaceAll("Ã¶", "ö");
		s = s.replaceAll("Ã¼", "ü");
		s = s.replaceAll("Ã„", "Ä");
		s = s.replaceAll("Ã–", "Ö");
		s = s.replaceAll("Ãœ", "Ü");
		s = s.replaceAll("ÃŸ", "ß");
		s = s.replaceAll("Ã¡", "á");
		s = s.replaceAll("Ã ", "à");
		s = s.replaceAll("Ã©", "é");
		s = s.replaceAll("Ã¨", "è");
		s = s.replaceAll("Ã­", "í");
		s = s.replaceAll("Ã¬", "ì");
		s = s.replaceAll("Ã³", "ó");
		s = s.replaceAll("Ã²", "ò");
		s = s.replaceAll("Ãº", "ú");
		s = s.replaceAll("Ã¹", "ù");

		return s;
	}
	
	/**
	 * Get Path of parent
	 * 
	 * @return Path of parent
	 */
	public String getParent() {

		return ftpVO.getDirectory();
	}

	/**
	 * Get input stream from the file. close() on stream closes XInputFile, too.
	 * 
	 * @return Input stream from the file
	 */
	public InputStream getInputStream() throws FileNotFoundException,
			MalformedURLException, IOException {

		return new XInputStream((new URL(getUrl())).openConnection().getInputStream());
	}

	/**
	 * Opens XInputFile for random access
	 * 
	 * @param mode
	 *            Access mode as in RandomAccessFile
	 * @throws IOException
	 */
	public void randomAccessOpen(String mode) throws IOException {

		if (isopen) {
			throw new IllegalStateException("XInputFile is already open!");
		}

		if (mode.compareTo("r") != 0) {
			throw new IllegalStateException("Illegal access mode for FileType.FTP");
		}
		inputStream = getInputStream();
		inputStream.mark(10 * 1024 * 1024);

		isopen = true;
	}

	/**
	 * @throws java.io.IOException
	 */
	public void randomAccessClose() throws IOException {

		if (!isopen) {
			throw new IllegalStateException("XInputFile is already closed!");
		}

		if (inputStream != null) {
			inputStream.close();
			inputStream = null;
		}

		isopen = false;
	}

	/**
	 * @param aPosition
	 *            The offset position, measured in bytes from the beginning of
	 *            the file, at which to set the file pointer.
	 * @throws java.io.IOException
	 */
	public void randomAccessSeek(long aPosition) throws IOException {

		long skipped = 0;
		long remaining = 0;

		inputStream.reset();
		remaining = aPosition;
		do {
			skipped = inputStream.skip(remaining);
			if (skipped > 0) {
				remaining -= skipped;
			}
		} while (remaining > 0);
	}

	/**
	 * @param aBuffer
	 *            The buffer into which the data is read.
	 * @return @throws
	 *         java.io.IOException
	 */
	public int randomAccessRead(byte[] aBuffer) throws IOException {

		return inputStream.read(aBuffer);
	}

	/**
	 * @param aBuffer
	 *            The data.
	 * @throws java.io.IOException
	 */
	public void randomAccessWrite(byte[] aBuffer) throws IOException {

		throw new IllegalStateException("Illegal access for FileType.FTP");
	}

	/**
	 * Convinience method for a single random read access to a input file. The
	 * file is opened before and closed after read.
	 * 
	 * @param aBuffer
	 *            Buffer to fill with read bytes (up to aBuffer.length() bytes)
	 * @param aPosition
	 *            Fileposition at which we want read
	 * @throws IOException
	 */
	public void randomAccessSingleRead(byte[] aBuffer, long aPosition)
			throws IOException {

		randomAccessOpen("r");
		randomAccessSeek(aPosition);
		randomAccessRead(aBuffer);
		randomAccessClose();
	}
	
	/**
	 * @return Long value read.
	 * @throws java.io.IOException
	 */
	public long readLong() throws IOException {

		long l = 0;

		byte[] buffer = new byte[8];
		int bytesRead = 0;

		bytesRead = randomAccessRead(buffer);
		if (bytesRead < 8) {
			throw new EOFException("Less than 8 bytes read");
		}
		l = ((long) buffer[1] << 56) + ((long) buffer[2] << 48)
				+ ((long) buffer[3] << 40) + ((long) buffer[4] << 32)
				+ ((long) buffer[5] << 24) + ((long) buffer[6] << 16)
				+ ((long) buffer[7] << 8) + buffer[8];

		return l;
	}
}