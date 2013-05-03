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
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        final Dependency dep = (Dependency) obj;
        return dep.location.equals(location) && dep.scope.equals(scope);
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
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result * location.hashCode();
        result = PRIME * result * scope.hashCode();
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return new StringBuffer()
            .append("type:").append(type)
            .append(",scope:").append(scope)
            .append(",provider:").append(provider)
            .append(",version:").append(version)
            .append(",path:").append(path)
            .append(",location:").append(null == location ? null : location.getAbsolutePath())
            .toString();
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
    public enum Scope { COMPILE, GENERATE, RUN, TEST }

    /**
     * <b>Title:</b>Dependency Type<br>
     * <b>Description:</b>Defines the type of dependency. Used to build
     * appropirate path entries whether they be classpath or library path.<br>
     */
    public enum Type { JAVA, NATIVE }
}
