/*
 * Aug 6, 2005
 */
package com.thinkparity.codebase;

/**
 * OS
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum OS {

	LINUX, OSX, WINDOWS_2000, WINDOWS_XP;

    /** The <code>Platform</code>. */
    private Platform platform;

	/** The version <code>String</code>. */
	private String version;

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
	public String getVersion() { return version; }

	/**
     * Set the platform of the os.
     * 
     * @param platform
     *            The <code>Platform</code>.
     */
    void setPlatform(final Platform platform) {
        this.platform = platform;
    }

    /**
	 * Set the version of the os.
	 * 
	 * @param version
	 *            The version of the os.
	 */
	void setVersion(final String version) { this.version = version; }

}
