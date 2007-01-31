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

    private static final String PROPERTY_NAME_CVS_BRANCH;

    private static final String PROPERTY_NAME_CVS_COMPRESSION_LEVEL;

    private static final String PROPERTY_NAME_CVS_ROOT;

    private static final String PROPERTY_NAME_TARGET_CLASSES_DIR;

    private static final String PROPERTY_NAME_TARGET_TEST_CLASSES_DIR;

    static {
        PROPERTY_NAME_CVS_BRANCH = "cvs.branch";
        PROPERTY_NAME_CVS_COMPRESSION_LEVEL = "cvs.compressionlevel";
        PROPERTY_NAME_CVS_ROOT = "cvs.cvsroot";
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

    /** The list of dependencies added by this task invocation. */
    private final List<Dependency> dependencies;

    /** A dependency path <code>String</code>. */
    private String path;

    /** A dependency provider <code>String</code>. */
    private String provider;

    /** A dependency's <code>Scope</code>. */
    private Scope scope;

    /** A dependency's <code>Type</code>. */
    private Type type;

    /** A dependency version <code>String</code>. */
    private String version;

    /**
     * Create Dependency.
     *
     */
    public DependencyTask() {
        super();
        this.dependencies = new ArrayList<Dependency>();
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
     * @see com.thinkparity.antx.AntXTask#doExecute()
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
        if (null == path)
            throw panic("Dependency path for is not specified.");
        if (null == scope)
            throw panic("Dependency scope for is not specified.");
        if (null == type)
            throw panic("Dependency type for is not specified.");
        if (null == version)
            throw panic("Dependency version for is not specified.");
        // validate scope/type combination
        if (Type.NATIVE == type)
            if (Scope.COMPILE == scope)
                throw panic("Dependency type {0} for scope {1} is invalid.",
                        type.name(), scope.name());
        dependencies.clear();
        final File location = new File(getVendorRootDirectory(), path);
        Dependency dependency;
        switch (type) {
        case JAVA:
            if (!location.isFile())
                throw panic("Dependency path for must be a file.");
            dependency = new Dependency();
            dependency.setLocation(location);
            dependency.setPath(path);
            dependency.setProvider(provider);
            dependency.setScope(scope);
            dependency.setType(type);
            dependency.setVersion(version);
            dependencies.add(dependency);
            break;
        case NATIVE:
            if (!location.isDirectory())
                throw panic("Dependency path for must be a directory.");
            final File[] nativeFiles = location.listFiles(new FileFilter() {
                public boolean accept(final File pathname) {
                    return pathname.isFile();
                }
            });
            for (final File nativeFile : nativeFiles) {
                dependency = new Dependency();
                dependency.setLocation(nativeFile);
                dependency.setPath(path);
                dependency.setProvider(provider);
                dependency.setScope(scope);
                dependency.setType(type);
                dependency.setVersion(version);
                dependencies.add(dependency);
            }
            break;
        default:
            throw panic("Unknown type {0}", type.name());
        }
        for (final Dependency d : dependencies) {
            if (!d.getLocation().exists())
                locate(d);
            if (!d.getLocation().exists())
                throw panic("Dependency {0} does not exist and cannot be found.", d);
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
     * @param dependency
     *            A <code>Dependency</code>.
     */
    private void addClassPathElement(final Scope scope,
            final Dependency dependency) {
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
    private void addFilesetLocation(final Type type, final Scope scope,
            final Dependency dependency) {
        final String fileSetId = getFileSetId(type, scope);
        FileSet fileSet = (FileSet) getProject().getReference(fileSetId);
        if (null == fileSet) {
            fileSet = new FileSet();
            fileSet.setProject(getProject());
            fileSet.setDir(getVendorRootDirectory());
        }
        fileSet.appendIncludes(new String[] {dependency.getPath()});
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
        for (final Dependency dependency : dependencies) {
            if (isTracked(dependency))
                break;
    
            switch (dependency.getType()) {
            case JAVA:
                switch (dependency.getScope()) {
                case COMPILE:
                    addClassPathElement(Scope.COMPILE, dependency);
                    addClassPathElement(Scope.RUNTIME, dependency);
                    addClassPathElement(Scope.TEST, dependency);
    
                    addFilesetLocation(Type.JAVA, Scope.RUNTIME, dependency);
                    addFilesetLocation(Type.JAVA, Scope.TEST, dependency);
    
                    track(Scope.COMPILE, dependency);
                    track(Scope.RUNTIME, dependency);
                    track(Scope.TEST, dependency);
                    break;
                case RUNTIME:
                    addClassPathElement(Scope.RUNTIME, dependency);
                    addClassPathElement(Scope.TEST, dependency);
    
                    addFilesetLocation(Type.JAVA, Scope.RUNTIME, dependency);
                    addFilesetLocation(Type.JAVA, Scope.TEST, dependency);
    
                    track(Scope.RUNTIME, dependency);
                    track(Scope.TEST, dependency);
                    break;
                case TEST:
                    addClassPathElement(Scope.TEST, dependency);
    
                    addFilesetLocation(Type.JAVA, Scope.TEST, dependency);
    
                    track(Scope.TEST, dependency);
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
    
                    addFilesetLocation(Type.NATIVE, Scope.RUNTIME, dependency);
                    addFilesetLocation(Type.NATIVE, Scope.TEST, dependency);
    
                    track(Scope.RUNTIME, dependency);
                    track(Scope.TEST, dependency);
                    break;
                case TEST:
                    addLibraryPathElement(Scope.TEST, dependency.getLocation());
    
                    addFilesetLocation(Type.NATIVE, Scope.TEST, dependency);
    
                    track(Scope.TEST, dependency);
                    break;
                default:
                    throw panic("Unknown scope {0}", dependency.getScope().name());
                }
                break;
            default:
                throw panic("Unknown type {0}", dependency.getType().name());
            }
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
     * Attempt to locate the dependency.
     *
     */
    private void locate(final Dependency dependency) {
        if (null == cvsLocator) {
            final Project project = getProject();
            cvsLocator = new CvsLocator(getProperty(project, PROPERTY_NAME_CVS_ROOT),
                    Integer.valueOf(getProperty(project, PROPERTY_NAME_CVS_COMPRESSION_LEVEL)),
                    getProperty(project, PROPERTY_NAME_CVS_BRANCH),
                    project.getBaseDir());
        }
        cvsLocator.locate(dependency);
    }
}
