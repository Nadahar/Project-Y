package xinput;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class XInputStream extends FilterInputStream {

	boolean debug = false;
	FileWriter fw = null;

	/**
	 * Create stream, which is able to handle special needs of a XInputFile
	 * 
	 * @param aIs InputStream to XInputFile
	 * @see java.io.FilterInputStream#FilterInputStream(InputStream in)
	 */
	public XInputStream(InputStream aIs) {
		super(aIs);

		if (debug) {
			try {
				fw = new FileWriter("XInputStream.log", true);
				fw.write("InputStream opened!\n");
			} catch (IOException e) {
			}
		}
	}

	/**
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		byte[] buffer = new byte[1];
		int result = 0;

		result = read(buffer, 0, buffer.length);

		return (int) buffer[0];
	}

	/**
	 * @see java.io.InputStream#read(byte[])
	 */
	public int read(byte[] aBuffer) throws IOException {
		int result = 0;

		result = read(aBuffer, 0, aBuffer.length);

		return result;
	}

	/**
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(byte[] aBuffer, int off, int len) throws IOException {
		int result = 0;
		long read = 0;
		long readBytes = 0;
		long remaining = 0;
		byte[] streamBuffer = new byte[len];

		try {
			remaining = len;
			do {
				read = super.read(streamBuffer, 0, (int) remaining);
				if (read > 0) {
					readBytes += read;
					System.arraycopy(streamBuffer, 0, aBuffer, (int) (len
							- remaining + off), (int) read);
					remaining -= read;
				}
			} while ((remaining > 0) && (read != -1));
			result = (int) (len - remaining);

			if ((read == -1) && (result == 0)) {
				return -1;
			} else {
				return result;
			}
		} finally {
			if (debug) {
				if (readBytes != len) {
					fw.write("Wanted: " + len + ", Read: " + readBytes
							+ ", Difference: " + (len - readBytes) + "\n");
				} else {
					//fw.write("Wanted: " + len + ", Read: " + readBytes + "\n");
				}
			}
		}
	}

	/**
	 * @see java.io.InputStream#close()
	 */
	public void close() throws IOException {
		super.close();
		if (debug) {
			fw.write("InputStream closed!\n");
			fw.close();
		}
	}
}