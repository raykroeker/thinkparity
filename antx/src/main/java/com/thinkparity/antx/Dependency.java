/*
 * Created On:  26-Jan-07 6:00:00 PM
 */
package com.thinkparity.antx;

import java.io.File;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Dependency {

    /** The dependency location <code>File</code>. */
    private File location;

    /** The dependency path <code>String</code>. */
    private String path;

    /** The dependency provider <code>String</code>. */
    private String provider;

    /** The dependency <code>Scope</code>. */
    private Scope scope;

    /** The dependency <code>Type</code>. */
    private Type type;

    /** The dependency version <code>String</code>. */
    private String version;

    /**
     * Create Dependency.
     *
     */
    Dependency() {
        super();
    }

    /**
     * Obtain location.
     *
     * @return A File.
     */
    public File getLocation() {
        return location;
    }

    /**
     * Obtain path.
     *
     * @return A String.
     */
    public String getPath() {
        return path;
    }

    /**
     * Obtain provider.
     *
     * @return A String.
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Obtain scope.
     *
     * @return A Scope.
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Obtain type.
     *
     * @return A Type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Obtain version.
     *
     * @return A String.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set location.
     *
     * @param location
     *		A File.
     */
    void setLocation(final File location) {
        this.location = location;
    }

    /**
     * Set path.
     *
     * @param path
     *		A String.
     */
    void setPath(final String path) {
        this.path = path;
    }

    /**
     * Set provider.
     *
     * @param provider
     *		A String.
     */
    void setProvider(final String provider) {
        this.provider = provider;
    }

    /**
     * Set scope.
     *
     * @param scope
     *		A Scope.
     */
    void setScope(final Scope scope) {
        this.scope = scope;
    }

    /**
     * Set type.
     *
     * @param type
     *		A Type.
     */
    void setType(final Type type) {
        this.type = type;
    }

    /**
     * Set version.
     *
     * @param version
     *		A String.
     */
    void setVersion(final String version) {
        this.version = version;
    }

    /**
     * <b>Title:</b>Dependency Scope<br>
     * <b>Description:</b>Defines the scope of the depenency. Used to build
     * appropriate path references for compilation runtime and testing.<br>
     */
    public enum Scope { COMPILE, RUNTIME, TEST }

    /**
     * <b>Title:</b>Dependency Type<br>
     * <b>Description:</b>Defines the type of dependency. Used to build
     * appropirate path entries whether they be classpath or library path.<br>
     */
    public enum Type { JAVA, NATIVE }
}
