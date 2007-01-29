/*
 * Created On:  26-Jan-07 6:00:00 PM
 */
package com.thinkparity.ant;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Dependency extends AbstractTask {

    /** A list of the dependencies already added. */
    private static final List<File> LOCATIONS;

    private static final String PROPERTY_NAME_TARGET_CLASSES_DIR;

    private static final String PROPERTY_NAME_TARGET_TEST_CLASSES_DIR;

    static {
        LOCATIONS = new ArrayList<File>(25);
        PROPERTY_NAME_TARGET_CLASSES_DIR = "target.classes.dir";
        PROPERTY_NAME_TARGET_TEST_CLASSES_DIR = "target.test-classes.dir";
    }

    /** A dependency path <code>String</code>. */
    private String path;

    /** A dependency provider <code>String</code>. */
    private String provider;

    /** A dependency <code>Scope</code>. */
    private Scope scope;

    /** A dependency <code>Type</code>. */
    private Type type;

    /** A dependency version <code>String</code>. */
    private String version;

    /**
     * Create Dependency.
     *
     */
    public Dependency() {
        super();
    }

    /**
     * Set resource.
     *
     * @param path
     *		A path <code>String</code>.
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Set the provider.
     *
     * @param provider
     *      A provider <code>String</code>.
     */
    public void setProvider(final String provider) {
        this.provider = provider;
    }

    /**
     * Set scope.
     *
     * @param scope
     *      A String.
     */
    public void setScope(final String scope) {
        this.scope = Scope.valueOf(scope.toUpperCase());
    }

    /**
     * Set type.
     *
     * @param type
     *      A String.
     */
    public void setType(final String type) {
        this.type = Type.valueOf(type.toUpperCase());
    }

    /**
     * Set version.
     *
     * @param version
     *		A String.
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Debug the state.
     *
     */
    @Override
    protected void debug() {
        final StringBuffer message = new StringBuffer();
        Object reference;
        for (final Scope scope : Scope.values()) {
            message.setLength(0);
            message.append(scope.toString().toLowerCase())
                .append(":");
            reference = getProject().getReference(getClassPathId(scope));
            if (null == reference)
                message.append("Not specified.");
            else
                message.append(reference);
            log(message.toString(), Project.MSG_DEBUG);
        }
    }

    /**
     * @see com.thinkparity.ant.AbstractTask#doExecute()
     *
     */
    @Override
    protected void doExecute() {
        final File file = getFile();
        if (!file.exists())
            locate();
        if (!file.exists())
            throw panic("Dependency {0} does not exist and cannot be found.",
                    file.getAbsolutePath());
        addPathElements(file);
    }

    /**
     * Validate the task.  Check for path; scope and version.
     *
     */
    protected void validate() {
        if (null == path)
            throw panic("Dependency path is not specified.");
        if (null == provider)
            throw panic("Dependency provider is not specified.");
        if (null == scope)
            throw panic("Dependency scope is not specified.");
        if (null == type)
            throw panic("Dependency type is not specified.");
        if (null == version)
            throw panic("Dependency version is not specified.");
        validateFileProperty(getProject(), PROPERTY_NAME_TARGET_CLASSES_DIR);
        validateFileProperty(getProject(), PROPERTY_NAME_TARGET_TEST_CLASSES_DIR);
    }

    /**
     * Obtain the path specified for the dependency.
     * 
     * @return A path <code>String</code>.
     */
    String getPath() {
        validate();
        log("path:" + path, Project.MSG_DEBUG);
        return path;
    }

    /**
     * Obtain the provider.
     *
     * @return The provider <code>String</code>.
     */
    String getProvider() {
        validate();
        log("provider:" + provider, Project.MSG_DEBUG);
        return provider;
    }

    /**
     * Obtain the version specified for the dependency.
     * 
     * @return A dependency version <code>String</code>.
     */
    String getVersion() {
        validate();
        log("version:" + version, Project.MSG_DEBUG);
        return version;
    }

    /**
     * Add a path element for a given path id. A path element is created and
     * appended to the existing path for the given path id. If the existing path
     * does not exist; it is created.
     * 
     * @param scope
     *            A <code>Scope</code>.
     * @param location
     *            A location <code>File</code>.
     */
    private void addClassPathElement(final Scope scope, final File location) {
        // obtain the existing class path
        final String pathId = getClassPathId(scope);
        Path existingPath = (Path) getProject().getReference(pathId);
        if (null == existingPath) {
            // create a new class path
            existingPath = createClassPath(scope);
        }
        // add the location
        final PathElement referencePath = existingPath.new PathElement();
        referencePath.setLocation(location);
        existingPath.add(referencePath);
        // save the path
        getProject().addReference(pathId, existingPath);
    }

    /**
     * Add a library path element for a given scope and location. A path element
     * is created and appended to the existing path for the given path id. If
     * the existing path does not exist; it is created.
     * 
     * @param scope
     *            A <code>Scope</code>.
     * @param location
     *            A location <code>File</code>.
     */
    private void addLibraryPathElement(final Scope scope, final File location) {
        // obtain the existing class path
        final String pathId = getLibraryPathId(scope);
        Path existingPath = (Path) getProject().getReference(pathId);
        if (null == existingPath) {
            // create a new class path
            existingPath = createClassPath(scope);
        }
        // add the location
        final PathElement referencePath = existingPath.new PathElement();
        referencePath.setLocation(location);
        existingPath.add(referencePath);
        // save the path
        getProject().addReference(pathId, existingPath);
    }

    /**
     * Add path elements for a given location. Based upon the type and scope of
     * the dependency the location will be added as a class or library path
     * within one or more scopes.
     * 
     * @param location
     *            A location <code>File</code>.
     */
    private void addPathElements(final File location) {
        if (LOCATIONS.contains(location))
            return;
        switch (getType()) {
        case JAVA:
            switch (getScope()) {
            case COMPILE:
                addClassPathElement(Scope.COMPILE, location);
                addClassPathElement(Scope.RUNTIME, location);
                addClassPathElement(Scope.TEST, location);
                break;
            case RUNTIME:
                addClassPathElement(Scope.RUNTIME, location);
                addClassPathElement(Scope.TEST, location);
                break;
            case TEST:
                addClassPathElement(Scope.TEST, location);
                break;
            default:
                throw panic("Unknown scope {0}", scope.name());
            }
            break;
        case NATIVE:
            switch (getScope()) {
            case COMPILE:
                break;
            case RUNTIME:
                addLibraryPathElement(Scope.RUNTIME, location);
                addLibraryPathElement(Scope.TEST, location);
                break;
            case TEST:
                addLibraryPathElement(Scope.TEST, location);
                break;
            default:
                throw panic("Unknown scope {0}", scope.name());
            }
            break;
        default:
            throw panic("Unknown type {0}", type.name());
        }
    }

    /**
     * Create a new class path for a given scope. If the scope is compile or
     * runtime the default output directory will be added; if the scope is test,
     * the default output directory as well as the test output directory are
     * added.
     * 
     * @param scope
     *            The <code>Scope<code>.
     * @return The new clas <code>Path</code>.
     */
    private Path createClassPath(final Scope scope) {
        final Project project = getProject();
        final String classPathId = getClassPathId(scope);

        final Path classPath = new Path(project, classPathId);
        PathElement target;
        switch (scope) {
        case COMPILE:
            target = classPath.new PathElement();
            target.setLocation(getFileProperty(project, PROPERTY_NAME_TARGET_CLASSES_DIR));
            classPath.add(target);
            break;
        case RUNTIME:
            target = classPath.new PathElement();
            target.setLocation(getFileProperty(project, PROPERTY_NAME_TARGET_CLASSES_DIR));
            classPath.add(target);
            break;
        case TEST:
            target = classPath.new PathElement();
            target.setLocation(getFileProperty(project, PROPERTY_NAME_TARGET_CLASSES_DIR));
            classPath.add(target);

            target = classPath.new PathElement();
            target.setLocation(getFileProperty(project, PROPERTY_NAME_TARGET_TEST_CLASSES_DIR));
            classPath.add(target);
            break;
        default:
            throw panic("Unknown scope {0}", scope.name());
        }
        return classPath;
    }

    /**
     * Obtain a class path id for the scope.
     * 
     * @param scope
     *            An enumerated dependency <code>Scope</code>.
     * @return A class path id <code>String</code>.
     */
    private String getClassPathId(final Scope scope) {
        return MessageFormat.format("{0}.classpath",
                scope.toString().toLowerCase());
    }

    /**
     * Obtain the file based upon the path.
     * 
     * @return A <code>File</code>.
     */
    private File getFile() {
        final File vendor = new File(getProject().getBaseDir(), "vendor");
        return new File(vendor, getPath());
    }

    /**
     * Obtain a library path id for the scope.
     * 
     * @param scope
     *            An enumerated dependency <code>Scope</code>.
     * @return A library path id <code>String</code>.
     */
    private String getLibraryPathId(final Scope scope) {
        return MessageFormat.format("{0}.librarypath",
                scope.toString().toLowerCase());
    }

    /**
     * Obtain the scope specified for the dependency.
     * 
     * @return A dependency <code>Scope</code>.
     */
    private Scope getScope() {
        validate();
        log("scope:" + scope, Project.MSG_DEBUG);
        return scope;
    }

    /**
     * Obtain the type specified for the dependency.
     * 
     * @return A dependency <code>Type</code>.
     */
    private Type getType() {
        validate();
        log("type:" + type, Project.MSG_DEBUG);
        return type;
    }

    /**
     * Attempt to locate the dependency.
     *
     */
    private void locate() {
        new CvsLocator().locate(this);
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
