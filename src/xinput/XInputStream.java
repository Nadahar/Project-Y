package xinput;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class XInputStream extends FilterInputStream {

	/**
	 * Create stream, which is able to handle special needs of a XInputFile
	 * @param aIs InputStream to XInputFile
	 * @see java.io.InputStream#XInputStream(InputStream in)
	 */
	public XInputStream(InputStream aIs) {
		super(aIs);
	}
	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		byte[] buffer = new byte[1];
		int result = 0;

		result = read(buffer, 0, buffer.length);

		return (int)buffer[0];
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[])
	 */
	public int read(byte[] aBuffer) throws IOException {
		int result = 0;

		result = read(aBuffer, 0, aBuffer.length);

		return result;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(byte[] aBuffer, int off, int len) throws IOException {
		int result = 0;
		long read = 0;
		long remaining = 0;
		byte[] streamBuffer = new byte[len];

		remaining = len;
		do {
			read = super.read(streamBuffer, 0, (int)remaining);
			if (read > 0) {
				System.arraycopy(streamBuffer, 0, aBuffer, (int) (len - remaining + off), (int)read);
				remaining -= read;
			}
		} while ((remaining > 0) && (read != -1));
		result = (int) (len - remaining);

		if ((read == -1) && (result == 0)) {
			return -1;
		} else {
			return result;
		}
	}
}
