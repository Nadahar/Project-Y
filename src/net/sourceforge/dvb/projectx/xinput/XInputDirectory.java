package net.sourceforge.dvb.projectx.xinput;

import java.util.Iterator;

public class XInputDirectory implements XInputDirectoryIF {

	// Implementation class
	private XInputDirectoryIF impl = null;

	private boolean debug = true;

	/**
	 * Private Constructor, don't use!
	 */
	private XInputDirectory() {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	public XInputDirectory(Object aVO) {
		if (debug) System.out.println("Enter XInputDirectory(Object '" + aVO + "')");

		Class[] parameterTypes = { aVO.getClass() };
		Object[] parameterValues = { aVO };
		retrieveImplementation(parameterTypes, parameterValues);
		
		if (debug) System.out.println("Leave XInputDirectory(Object '" + aVO + "')");
	}

	/**
	 */
	private void retrieveImplementation(Class[] parameterTypes, Object[] parameterValues) {
		if (debug) System.out.println("Enter XInputDirectory.retrieveImplementation(Class[] parameterTypes, Object[] parameterValues)");

		DirType dirType = null;
		
		for (Iterator dirTypes = DirType.getDirTypes().iterator(); dirTypes.hasNext();) {
			dirType = (DirType) dirTypes.next();

			if (dirType.equals(DirType.DEFAULT)) {
				continue;
			}

			try {
				if (debug) System.out.println("Try DirType '" + dirType.getName() + "'");
				impl = (XInputDirectoryIF) dirType.getImplementation().getConstructor(parameterTypes).newInstance(
						parameterValues);
				if (debug) System.out.println("Use DirType '" + dirType.getName() + "' for file '" + impl.toString() + "'");
				if (debug) System.out.println("Leave XInputDirectory.retrieveImplementation(Class[] parameterTypes, Object[] parameterValues)");
				return;
			} catch (Exception e) {
				// Failed, try next type
				impl = null;
			}
		}
		try {
			dirType = DirType.DEFAULT;
			if (debug) System.out.println("Try default DirType '" + dirType.getName() + "'");
			impl = (XInputDirectoryIF) dirType.getImplementation().getConstructor(parameterTypes)
					.newInstance(parameterValues);
			if (debug) System.out.println("Use default DirType '" + dirType.getName() + "' for file '" + impl.toString() + "'");
			if (debug) System.out.println("Leave XInputDirectory.retrieveImplementation(Class[] parameterTypes, Object[] parameterValues)");
			return;
		} catch (Exception e) {
			// Failed, no type left, so this is final failure
			impl = null;
			if (debug) System.out.println("No matching DirType found or directory doesn't exist");
			if (debug) System.out.println("XInputDirectory.Leave retrieveImplementation(Class[] parameterTypes, Object[] parameterValues)");
			throw new IllegalArgumentException("No matching DirType found or directory doesn't exist");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object aObj) {
		if (debug) System.out.println("Enter XInputDirectory.equals(Object '" + aObj + "')");

		if (!(aObj instanceof XInputDirectory)) {
			if (debug) System.out.println("Leave XInputDirectory.equals(Object '" + aObj + "') returning false");
			return false;
		}
		XInputDirectory other = (XInputDirectory)aObj;
		if (other.getDirType().equals(getDirType()) && other.toString().equals(toString())) {
			if (debug) System.out.println("Leave XInputDirectory.equals(Object '" + aObj + "') returning true");
			return true;
		} else {
			if (debug) System.out.println("Leave XInputDirectory.equals(Object '" + aObj + "') returning false");
			return false;
		}
	}

	/**
	 * @return
	 */
	public String getDirectory() {
		if (debug) System.out.println("Enter XInputDirectory.getDirectory()");
		String s = impl.getDirectory();
		if (debug) System.out.println("Leave XInputDirectory.getDirectory() returning " + s);
		return s;
	}

	/**
	 * @return
	 */
	public DirType getDirType() {
		if (debug) System.out.println("Enter XInputDirectory.getDirType()");
		DirType dt = impl.getDirType();
		if (debug) System.out.println("Leave XInputDirectory.getDirType() returning " + dt);
		return dt;
	}

	/**
	 * @return
	 */
	public XInputFile[] getFiles() {
		if (debug) System.out.println("Enter XInputDirectory.getFiles()");
		XInputFile[] xInputFiles = impl.getFiles();
		if (debug) System.out.println("Leave XInputDirectory.getFiles() returning " + xInputFiles.length + " xInputFiles");
		return xInputFiles;
	}

	/**
	 * @return
	 */
	public String getLog() {
		if (debug) System.out.println("Enter XInputDirectory.getLog()");
		String s = impl.getLog();
		if (debug) System.out.println("Leave XInputDirectory.getLog() returning " + s);
		return s;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		if (debug) System.out.println("Enter XInputDirectory.getPassword()");
		String s = impl.getPassword();
		if (debug) System.out.println("Leave XInputDirectory.getPassword() returning " + s);
		return s;
	}

	/**
	 * @return
	 */
	public String getServer() {
		if (debug) System.out.println("Enter XInputDirectory.getServer()");
		String s = impl.getServer();
		if (debug) System.out.println("Leave XInputDirectory.getServer() returning " + s);
		return s;
	}

	/**
	 * @return
	 */
	public String getTestMsg() {
		if (debug) System.out.println("Enter XInputDirectory.getTestMsg()");
		String s = impl.getTestMsg();
		if (debug) System.out.println("Leave XInputDirectory.getTestMsg() returning " + s);
		return s;
	}

	/**
	 * @return
	 */
	public String getUser() {
		if (debug) System.out.println("Enter XInputDirectory.getUser()");
		String s = impl.getUser();
		if (debug) System.out.println("Leave XInputDirectory.getUser() returning " + s);
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (debug) System.out.println("Enter XInputDirectory.hashCode()");
		int i = impl.hashCode();
		if (debug) System.out.println("Leave XInputDirectory.hashCode() returning " + i);
		return i;
	}

	/**
	 * @return
	 */
	public boolean test() {
		if (debug) System.out.println("Enter XInputDirectory.getDirectory()");
		boolean b = impl.test();
		if (debug) System.out.println("Leave XInputDirectory.getDirectory() returning " + (new Boolean(b)));
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (debug) System.out.println("Enter XInputDirectory.toString()");
		String s = impl.toString();
		if (debug) System.out.println("Leave XInputDirectory.toString() returning " + s);
		return s;
	}
}