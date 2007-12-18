/*
 * Aug 6, 2005
 */
package com.thinkparity.bootstrap;

/**
 * <b>Title:</b>thinkParity Operating System<br>
 * <b>Description:</b>Represents enumerated operating systems; their platforms
 * and their versions.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum OS {

	LINUX(Platform.LINUX), MAC_OSX(Platform.UNIX), WINDOWS_VISTA(Platform.WIN32),
    WINDOWS_XP(Platform.WIN32);

    /** The <code>Platform</code>. */
    private final Platform platform;

	/** The version <code>String</code>. */
	private final String version;

    /**
     * Create OS.
     * 
     * @param platform
     *            A <code>Platform</code>.
     */
    private OS(final Platform platform) {
        this.platform = platform;
        this.version = System.getProperty("os.version");
    }

    /**
     * Obtain the platform of the os.
     * 
     * @return A <code>Platform</code>.
     */
    public Platform getPlatform() {
        return platform;
    }

	/**
	 * Obtain the version of the os.
	 * 
	 * @return The version of the os.
	 */
	public String getVersion() {
        return version;
    }
}
