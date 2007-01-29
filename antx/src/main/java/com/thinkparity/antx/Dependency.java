/*
 * Created On:  26-Jan-07 6:00:00 PM
 */
package com.thinkparity.antx;

import java.io.File;
import java.io.FileFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;
import org.apache.tools.ant.types.selectors.FilenameSelector;

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
     * @see com.thinkparity.antx.AbstractTask#doExecute()
     *
     */
    @Override
    protected void doExecute() throws BuildException {
        final File file = getFile();
        if (!file.exists())
            locate();
        if (!file.exists())
            throw panic("Dependency {0} does not exist and cannot be found.",
                    file.getAbsolutePath());
        // validate type/path combination
        switch (type) {
        case JAVA:
            if (!file.isFile())
                throw panic("Dependency path for {0} must be a file.", path);
            break;
        case NATIVE:
            if (!file.isDirectory())
                throw panic("Dependency path for {0} must be a directory.", path);
            break;
        default:
            throw panic("Unknown type {0}", type.name());
        }
        addPathElements(file);
    }

    /**
     * Validate the task.  Check for path; scope and version.
     *
     */
    @Override
    protected void validate() throws BuildException {
        if (null == path)
            throw panic("Dependency path is not specified.");
        if (null == provider)
            throw panic("Dependency provider for {0} is not specified.", path);
        if (null == scope)
            throw panic("Dependency scope for {0} is not specified.", path);
        if (null == type)
            throw panic("Dependency type for {0} is not specified.", path);
        if (null == version)
            throw panic("Dependency version for {0} is not specified.", path);
        // validate scope/type combination
        if (Type.NATIVE == type)
            if (scope == Scope.COMPILE)
                throw panic("Dependency type {0} for scope {1} is invalid.",
                        type.name(), scope.name());
        validateFileProperty(getProject(), PROPERTY_NAME_TARGET_CLASSES_DIR);
        validateFileProperty(getProject(), PROPERTY_NAME_TARGET_TEST_CLASSES_DIR);
    }

    /**
     * Obtain the path specified for the dependency.
     * 
     * @return A path <code>String</code>.
     */
    String getPath() {
        log("path:" + path, Project.MSG_DEBUG);
        return path;
    }

    /**
     * Obtain the provider.
     *
     * @return The provider <code>String</code>.
     */
    String getProvider() {
        log("provider:" + provider, Project.MSG_DEBUG);
        return provider;
    }

    /**
     * Obtain the version specified for the dependency.
     * 
     * @return A dependency version <code>String</code>.
     */
    String getVersion() {
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
        final String classPathId = getClassPathId(scope);
        Path classPath = (Path) getProject().getReference(classPathId);
        if (null == classPath) {
            // create a new class path
            classPath = createClassPath(scope);
        }
        // add the location
        final PathElement referencePath = classPath.new PathElement();
        referencePath.setLocation(location);
        classPath.add(referencePath);
        // save the path
        getProject().addReference(classPathId, classPath);
    }

    /**
     * Add a fileset element for a location of a dependency type in a dependency
     * scope.
     * 
     * @param type
     *            A dependency <code>Type</code>.
     * @param scope
     *            A dependency <code>Scope</code>.
     * @param location
     *            A dependency location <code>File</code>.
     */
    private void addFilesetLocation(final Type type, final Scope scope, final File location) {
        final String fileSetId = getFileSetId(type, scope);
        FileSet fileSet = (FileSet) getProject().getReference(fileSetId);
        if (null == fileSet) {
            fileSet = createFileSet(type, scope);
        }

        FilenameSelector filenameSelector;
        switch (type) {
        case JAVA:
            filenameSelector = new FilenameSelector();
            filenameSelector.setName("**/*" + location.getName());
            fileSet.addFilename(filenameSelector);
            break;
        case NATIVE:
            final File[] nativeLocations = location.listFiles(new FileFilter() {
                public boolean accept(final File pathname) {
                    return pathname.isFile();
                }
            });
            for (final File nativeLocation : nativeLocations) {
                filenameSelector = new FilenameSelector();
                filenameSelector.setName("**/*" + nativeLocation.getName());
                fileSet.addFilename(filenameSelector);
            }
            break;
        default:
            throw panic("Unknown type for location {0}", location.getAbsolutePath());
        }
        getProject().addReference(fileSetId, fileSet);
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
            existingPath = createLibraryPath(scope);
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

                addFilesetLocation(Type.JAVA, Scope.RUNTIME, location);
                addFilesetLocation(Type.JAVA, Scope.TEST, location);
                break;
            case RUNTIME:
                addClassPathElement(Scope.RUNTIME, location);
                addClassPathElement(Scope.TEST, location);

                addFilesetLocation(Type.JAVA, Scope.RUNTIME, location);
                addFilesetLocation(Type.JAVA, Scope.TEST, location);
                break;
            case TEST:
                addClassPathElement(Scope.TEST, location);

                addFilesetLocation(Type.JAVA, Scope.TEST, location);
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

                addFilesetLocation(Type.NATIVE, Scope.RUNTIME, location);
                addFilesetLocation(Type.NATIVE, Scope.TEST, location);
                break;
            case TEST:
                addLibraryPathElement(Scope.TEST, location);

                addFilesetLocation(Type.NATIVE, Scope.TEST, location);
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

        final Path classPath = new Path(project);
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
     * Create a file set.
     * 
     * @param type
     *            A dependency <code>Type</code>.
     * @param scope
     *            A dependency <code>Scope</code>.
     * @return A <code>FileSet</code>.
     */
    private FileSet createFileSet(final Type type, final Scope scope) {
        final FileSet fileSet = new FileSet();
        fileSet.setDir(getVendorRootDirectory());
        return fileSet;
    }

    /**
     * Create a library path for a given scope.
     * 
     * @param scope
     *            The <code>Scope<code>.
     * @return The new library <code>Path</code>.
     */
    private Path createLibraryPath(final Scope scope) {
        final Project project = getProject();
        return new Path(project);
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
        return new File(getVendorRootDirectory(), getPath());
    }

    /**
     * Obtain a file set id for the type and scope.
     * 
     * @param type
     *            A dependency <code>Type</code>.
     * @param scope
     *            A dependncy <code>Scope</code>.
     * @return A file set id <code>String</code>.
     */
    private String getFileSetId(final Type type, final Scope scope) {
        return MessageFormat.format("{0}.dependencies-{1}",
                scope.name().toLowerCase(), type.name().toLowerCase());
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
        log("scope:" + scope, Project.MSG_DEBUG);
        return scope;
    }

    /**
     * Obtain the type specified for the dependency.
     * 
     * @return A dependency <code>Type</code>.
     */
    private Type getType() {
        log("type:" + type, Project.MSG_DEBUG);
        return type;
    }

    /**
     * Obtain the vendor root directory.
     * 
     * @return A directory <code>File</code>.
     */
    private File getVendorRootDirectory() {
        return new File(getProject().getBaseDir(), "vendor");
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
