package xinput;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import org.apache.commons.net.ftp.FTPFile;

import xinput.file.*;
import xinput.ftp.*;

public class XInputFile implements XInputFileIF {

	// Implementation class
	private XInputFileIF impl = null;

	private boolean debug = true;

	/**
	 * Private Constructor, don't use!
	 */
	private XInputFile() {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	public XInputFile(Object aVO) {

		FileType fileType = null;
		Class[] parameterTypes = { aVO.getClass() };
		Object[] parameterValues = { aVO };

		for (Iterator fileTypes = FileType.getFileTypes().iterator(); fileTypes.hasNext();) {
			fileType = (FileType) fileTypes.next();

			if (fileType.equals(FileType.DEFAULT)) {
				continue;
			}

			try {
				if (debug) System.out.println("Try FileType '" + fileType.getName() + "'");
				impl = (XInputFileIF) fileType.getImplementation().getConstructor(parameterTypes).newInstance(parameterValues);
				if (debug) System.out.println("Use FileType '" + fileType.getName() + "' for file '" + impl.toString() + "'");
				return;
			} catch (Exception e) {
				// Failed, try next type
				impl = null;
			}
		}
		try {
			fileType = FileType.DEFAULT;
			if (debug) System.out.println("Try default FileType '" + fileType.getName() + "'");
			impl = (XInputFileIF) fileType.getImplementation().getConstructor(parameterTypes).newInstance(parameterValues);
			if (debug) System.out.println("Use default FileType '" + fileType.getName() + "' for file '" + impl.toString() + "'");
			return;
		} catch (Exception e) {
			// Failed, no type left, so this is final failure
			impl = null;
			String s = "No matching FileType found or file doesn't exist";
			if (debug) System.out.println(s);
			throw new IllegalArgumentException(s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object aObj) {
		return impl.equals(aObj);
	}

	/**
	 * @return
	 */
	public boolean exists() {
		return impl.exists();
	}

	/**
	 * @return @throws
	 *         FileNotFoundException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStream() throws FileNotFoundException, MalformedURLException, IOException {
		return impl.getInputStream();
	}

	/**
	 * @return
	 */
	public String getName() {
		return impl.getName();
	}

	/**
	 * @return
	 */
	public String getParent() {
		return impl.getParent();
	}

	/**
	 * @return
	 */
	public String getUrl() {
		return impl.getUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return impl.hashCode();
	}

	/**
	 * @return
	 */
	public long lastModified() {
		return impl.lastModified();
	}

	/**
	 * @return
	 */
	public long length() {
		return impl.length();
	}

	/**
	 * @throws IOException
	 */
	public void randomAccessClose() throws IOException {
		impl.randomAccessClose();
	}

	/**
	 * @param aMode
	 * @throws IOException
	 */
	public void randomAccessOpen(String aMode) throws IOException {
		impl.randomAccessOpen(aMode);
	}

	/**
	 * @param aBuffer
	 * @return @throws
	 *         IOException
	 */
	public int randomAccessRead(byte[] aBuffer) throws IOException {
		return impl.randomAccessRead(aBuffer);
	}

	/**
	 * @param aPosition
	 * @throws IOException
	 */
	public void randomAccessSeek(long aPosition) throws IOException {
		impl.randomAccessSeek(aPosition);
	}

	/**
	 * @param aBuffer
	 * @param aPosition
	 * @throws IOException
	 */
	public void randomAccessSingleRead(byte[] aBuffer, long aPosition) throws IOException {
		impl.randomAccessSingleRead(aBuffer, aPosition);
	}

	/**
	 * @param aBuffer
	 * @throws IOException
	 */
	public void randomAccessWrite(byte[] aBuffer) throws IOException {
		impl.randomAccessWrite(aBuffer);
	}

	/**
	 * @return @throws
	 *         IOException
	 */
	public long readLong() throws IOException {
		return impl.readLong();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return impl.toString();
	}
}