/*
 * Created On:  26-Jan-07 6:00:00 PM
 */
package com.thinkparity.antx;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;

import com.thinkparity.antx.Dependency.Scope;
import com.thinkparity.antx.Dependency.Type;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DependencyTask extends AntXTask {

    /** A list of the dependencies already added. */
    private static final List<Dependency> DEPENDENCIES;

    private static final String PROPERTY_NAME_TARGET_CLASSES_DIR;

    private static final String PROPERTY_NAME_TARGET_TEST_CLASSES_DIR;

    static {
        DEPENDENCIES = new ArrayList<Dependency>(25);
        PROPERTY_NAME_TARGET_CLASSES_DIR = "target.classes.dir";
        PROPERTY_NAME_TARGET_TEST_CLASSES_DIR = "target.test-classes.dir";
    }

    /**
     * Obtain a class path id for the scope.
     * 
     * @param scope
     *            An enumerated dependency <code>Scope</code>.
     * @return A class path id <code>String</code>.
     */
    private static final String getClassPathId(final Scope scope) {
        return MessageFormat.format("{0}.classpath",
                scope.toString().toLowerCase());
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
    private static final String getFileSetId(final Type type, final Scope scope) {
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
    private static final String getLibraryPathId(final Scope scope) {
        return MessageFormat.format("{0}.librarypath",
                scope.toString().toLowerCase());
    }

    /** A cvs <code>Locator</code>. */
    private Locator cvsLocator;

    /** A <code>Dependency</code>. */
    private Dependency dependency;

    /**
     * Create Dependency.
     *
     */
    public DependencyTask() {
        super();
    }

    /**
     * Set resource.
     *
     * @param path
     *		A path <code>String</code>.
     */
    public void setPath(final String path) {
        initialize();
        this.dependency.setPath(path);
    }

    /**
     * Set the provider.
     *
     * @param provider
     *      A provider <code>String</code>.
     */
    public void setProvider(final String provider) {
        initialize();
        this.dependency.setProvider(provider);
    }

    /**
     * Set scope.
     *
     * @param scope
     *      A String.
     */
    public void setScope(final String scope) {
        initialize();
        this.dependency.setScope(Scope.valueOf(scope.toUpperCase()));
    }

    /**
     * Set type.
     *
     * @param type
     *      A String.
     */
    public void setType(final String type) {
        initialize();
        this.dependency.setType(Type.valueOf(type.toUpperCase()));
    }

    /**
     * Set version.
     *
     * @param version
     *		A String.
     */
    public void setVersion(final String version) {
        initialize();
        this.dependency.setVersion(version);
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
        addPathElements();
    }

    /**
     * Validate the task.  Check for path; scope and version.
     *
     */
    @Override
    protected void validate() throws BuildException {
        if (null == dependency)
            throw panic("Dependency is not specified.");
        if (null == dependency.getPath())
            throw panic("Dependency path for {0} is not specified.", dependency);
        if (null == dependency.getScope())
            throw panic("Dependency scope for {0} is not specified.", dependency);
        if (null == dependency.getType())
            throw panic("Dependency type for {0} is not specified.", dependency);
        if (null == dependency.getVersion())
            throw panic("Dependency version for {0} is not specified.", dependency);
        // validate scope/type combination
        if (Type.NATIVE == dependency.getType())
            if (Scope.COMPILE == dependency.getScope())
                throw panic("Dependency type {0} for scope {1} is invalid for {0}.",
                        dependency.getType().name(),
                        dependency.getScope().name(), dependency);
        // resolve the dependency's location
        dependency.setLocation(new File(getVendorRootDirectory(), dependency.getPath()));
        if (!dependency.getLocation().exists())
            locate(dependency);
        if (!dependency.getLocation().exists())
            throw panic("Dependency {0} does not exist and cannot be found.", dependency);
        // validate type/path combination
        switch (dependency.getType()) {
        case JAVA:
            if (!dependency.getLocation().isFile())
                throw panic("Dependency path for {0} must be a file.", dependency);
            break;
        case NATIVE:
            if (!dependency.getLocation().isDirectory())
                throw panic("Dependency path for {0} must be a directory.", dependency);
            break;
        default:
            throw panic("Unknown type {0}", dependency.getType().name());
        }
        validateFileProperty(getProject(), PROPERTY_NAME_TARGET_CLASSES_DIR);
        validateFileProperty(getProject(), PROPERTY_NAME_TARGET_TEST_CLASSES_DIR);
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
    private void addClassPathElement(final Scope scope) {
        // obtain the existing class path
        final String classPathId = getClassPathId(scope);
        Path classPath = (Path) getProject().getReference(classPathId);
        if (null == classPath) {
            // create a new class path
            classPath = createClassPath(scope);
        }
        // add the location
        final PathElement referencePath = classPath.new PathElement();
        referencePath.setLocation(dependency.getLocation());
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
     */
    private void addFilesetLocation(final Type type, final Scope scope) {
        final String fileSetId = getFileSetId(type, scope);
        FileSet fileSet = (FileSet) getProject().getReference(fileSetId);
        if (null == fileSet) {
            fileSet = new FileSet();
            fileSet.setProject(getProject());
            fileSet.setDir(getVendorRootDirectory());
        }

        final String[] includes;
        switch (type) {
        case JAVA:
            includes = new String[] {dependency.getPath()};
            break;
        case NATIVE:
            final File[] nativeIncludes = dependency.getLocation().listFiles();
            includes = new String[nativeIncludes.length];
            for (int i = 0; i < nativeIncludes.length; i++) {
                includes[i] = new StringBuffer(dependency.getPath())
                    .append(File.separator)
                    .append(nativeIncludes[i].getName())
                    .toString();
            }
            break;
        default:
            throw panic("Unknown type {0}", type.name());
        }
        fileSet.appendIncludes(includes);

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
     */
    private void addPathElements() {
        if (DEPENDENCIES.contains(dependency))
            return;

        switch (dependency.getType()) {
        case JAVA:
            switch (dependency.getScope()) {
            case COMPILE:
                addClassPathElement(Scope.COMPILE);
                addClassPathElement(Scope.RUNTIME);
                addClassPathElement(Scope.TEST);

                addFilesetLocation(Type.JAVA, Scope.RUNTIME);
                addFilesetLocation(Type.JAVA, Scope.TEST);
                break;
            case RUNTIME:
                addClassPathElement(Scope.RUNTIME);
                addClassPathElement(Scope.TEST);

                addFilesetLocation(Type.JAVA, Scope.RUNTIME);
                addFilesetLocation(Type.JAVA, Scope.TEST);
                break;
            case TEST:
                addClassPathElement(Scope.TEST);

                addFilesetLocation(Type.JAVA, Scope.TEST);
                break;
            default:
                throw panic("Unknown scope {0}", dependency.getScope().name());
            }
            break;
        case NATIVE:
            switch (dependency.getScope()) {
            case COMPILE:
                break;
            case RUNTIME:
                addLibraryPathElement(Scope.RUNTIME, dependency.getLocation());
                addLibraryPathElement(Scope.TEST, dependency.getLocation());

                addFilesetLocation(Type.NATIVE, Scope.RUNTIME);
                addFilesetLocation(Type.NATIVE, Scope.TEST);
                break;
            case TEST:
                addLibraryPathElement(Scope.TEST, dependency.getLocation());

                addFilesetLocation(Type.NATIVE, Scope.TEST);
                break;
            default:
                throw panic("Unknown scope {0}", dependency.getScope().name());
            }
            break;
        default:
            throw panic("Unknown type {0}", dependency.getType().name());
        }
        DEPENDENCIES.add(dependency);
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
     * Obtain the vendor root directory.
     * 
     * @return A directory <code>File</code>.
     */
    private File getVendorRootDirectory() {
        return new File(getProject().getBaseDir(), "vendor");
    }

    /**
     * Initialize the dependency task.
     *
     */
    private void initialize() {
        if (null == dependency)
            dependency = new Dependency();
    }

    /**
     * Attempt to locate the dependency.
     *
     */
    private void locate(final Dependency dependency) {
        if (null == cvsLocator) {
            final Project project = getProject();
            cvsLocator = new CvsLocator(getProperty(project, "cvs.root"),
                    Integer.valueOf(getProperty(project, "cvs.compressionlevel")),
                    getProperty(project, "cvs.branch"),
                    getVendorRootDirectory()); 
        }
        cvsLocator.locate(dependency);
    }
}
