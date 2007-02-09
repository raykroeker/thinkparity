/*
 * Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import junit.framework.Assert;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;

/**
 * @author raykroeker@gmail.com
 */
public class TestSession {

	/**  The test session's output directory. */
    private File outputDirectory;

	/** The session's data. */
	private final Map<Object,Object> sessionData;

	/** A synchronization lock for the session data. */
	private final Object sessionDataLock;

	/** The test session's root directory. */
	private final File sessionDirectory;

	/** The test session id. */
	private final String sessionId;

	/**
     * Create TestSession.
     * 
     * @param rootDirectory
     *            The test session's parent.
     */
	TestSession(final File parent) throws IOException {
		super();
		// TIME - This a local timestamp.
		this.sessionId = String.valueOf(System.currentTimeMillis());
		this.sessionData = new Hashtable<Object,Object>(20, 0.75F);
		this.sessionDataLock = new Object();
		this.sessionDirectory = createSessionDirectory(parent);
	}

	/**
	 * Obtain session data.
	 * 
	 * @param key
	 *            The data item key.
	 * @return The data value.
	 */
	public Object getData(final String key) {
		synchronized(sessionDataLock) {
			return sessionData.get(key);
		}
	}

    /**
     * Obtain the session output directory.
     * 
     * @return An output directory <code>File</code>.
     */
    public File getOutputDirectory() {
        if (null == outputDirectory) {
            outputDirectory = new File(sessionDirectory, JUnitX.getShortName() + "Output");
            Assert.assertTrue(outputDirectory.mkdir());
        }
        return outputDirectory;
    }

    /**
	 * Obtain the session id.
	 * 
	 * @return The session id.
	 */
	public String getSessionId() { return sessionId; }

    /**
	 * Set session data.
	 * 
	 * @param key
	 *            The data item key.
	 * @param value
	 *            The data item value.
	 * @return The previous value of the data item.
	 */
	public Object setData(final String key, final Object value) {
		synchronized(sessionDataLock) {
			return sessionData.put(key, value); 
		}
	}

	/**
	 * Clear the session data.
	 *
	 */
	void clearData() {
		synchronized(sessionDataLock) { sessionData.clear(); }
	}

	/**
	 * Obtain the session sessionDirectory.
	 * 
	 * @return The session sessionDirectory.
	 */
	File getSessionDirectory() { return sessionDirectory; }

	/**
     * Create the session directory. The session directory will use a static
     * name. It will write the session id to a file within the directory so that
     * previous session directories can be renamed when encountered.
     * 
     * @return The session directory <code>File</code>.
     */
	private File createSessionDirectory(final File parent) throws IOException {
		final File sessionDirectory = new File(parent, JUnitX.getShortName());
        if (sessionDirectory.exists()) {
            final FileSystem fileSystem = new FileSystem(sessionDirectory);
            final String sessionFileName = JUnitX.getShortName() + ".session";

            final File sessionFile = fileSystem.findFile("/" + sessionFileName);
            final String sessionId = FileUtil.readString(sessionFile);
            final Calendar sessionDate = DateUtil.getInstance(Long.valueOf(sessionId));

            final File newSessionDirectory = new File(parent,
                    MessageFormat.format("{0} {1,date,yyyy-MM-dd HH.mm.ss.SSS}",
                            JUnitX.getShortName(), sessionDate.getTime()));
            Assert.assertTrue("Cannot rename old session directory.",
                    sessionDirectory.renameTo(newSessionDirectory));
            final File newSessionFile = new File(newSessionDirectory, sessionFileName);
            Assert.assertTrue("Cannot delete session file.", newSessionFile.delete());
        }
		Assert.assertTrue(sessionDirectory.mkdir());
        FileUtil.writeBytes(
                new File(sessionDirectory, JUnitX.getShortName() + ".session"),
                this.sessionId.getBytes());
		return sessionDirectory;
	}
}
