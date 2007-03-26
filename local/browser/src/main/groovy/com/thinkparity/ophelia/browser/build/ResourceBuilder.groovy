/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.antx.Dependency
import com.thinkparity.antx.DependencyTracker

import com.thinkparity.codebase.FileUtil
import com.thinkparity.codebase.FileSystem

import com.thinkparity.codebase.model.migrator.Product
import com.thinkparity.codebase.model.migrator.Release
import com.thinkparity.codebase.model.migrator.Resource
import com.thinkparity.codebase.model.util.codec.MD5Util

import com.thinkparity.ophelia.model.util.UUIDGenerator

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Resource Builder<br>
 * <b>Description:</b>A resource builder is responsible for the creation of the
 * migrator resources required for deployment.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ResourceBuilder {

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /** A <code>Product</code>. */
    Product product

    /** A <code>Release</code>. */
    Release release

    /** The image directory <code>File</code>. */
    File imageDir

    /**
     * Initialize the resource builder.  Create a resource root directory from
     * the <code>"thinkparity.install"</code> property in the configuration.
     *
     */
    void init() {
        if (null == imageDir) {
            imageDir = configuration["thinkparity.target.package.image-dir"]
        }
    }

    /**
     * Create a list of resources.
     *
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    List create() {
        init()
        def resources = []
        def imageDirFileSystem = new FileSystem(imageDir)
        // add resources for all runtime dependencies
        def dependencies = new DependencyTracker().getDependencies(Dependency.Scope.RUNTIME)
        for (dependency in dependencies) {
            resources.add(create(dependency,lookup(imageDirFileSystem, dependency)))
        }
        // iterate files in the file-system and add resources for those not
        // added as dependencies
        def imageFiles = imageDirFileSystem.listFiles("/", Boolean.TRUE)
        for (imageFile in imageFiles) {
            def doAdd = true
            for (dependency in dependencies) {
                if (dependency.getLocation().getName().equals(imageFile.getName())) {
                    doAdd = false
                    break
                }
            }
            if (doAdd) {
                resources.add(create(imageFile))
            }
        }
        return resources
    }

    /**
     * Lookup a dependency within a file system.  It is given that the dependency
     * exists within the file system with the name name as it's location.
     *
     * @param fileSystem
     *      A <code>FileSystem</code> housing the dependency.
     * @param dependency
     *      A <code>Dependency</code>.
     */
    File lookup(FileSystem fileSystem, Dependency dependency) {
        def lookupPath = "/${configuration["thinkparity.image.lib-dirname"]}/"
        switch (dependency.getType()) {
        case Dependency.Type.JAVA:
            lookupPath += "${dependency.getLocation().getName()}"
            break;
        case Dependency.Type.NATIVE:
            lookupPath += "${configuration["thinkparity.image.lib.native-dirname"]}/${dependency.getLocation().getName()}"
            break;
        default:
            assert false;
        }
        return fileSystem.find(lookupPath)
    }

    Resource create(File file) {
        return create(release.getName(), file)
    }

    Resource create(Dependency dependency, File file) {
        return create(dependency.getVersion(), file)
    }

    Resource create(String version, File file) {
        def resource = new Resource()
        resource.setChecksum(checksum(file))
        resource.setName(file.getName())
        resource.setOs(release.getOs())
        resource.setPath(FileUtil.getRelativePath(imageDir, file))
        resource.setSize(file.length())
        resource.setVersion(version)
        return resource
    }

    /**
     * Calculate a checksum for a file.
     *
     * @param file
     *      A <code>File</code>.
     * @param buffer
     *      A buffer size <code>Integer</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    String checksum(final File file) {
        final InputStream stream = new FileInputStream(file)
        try {
            return MD5Util.md5Hex(stream, configuration["thinkparity.buffer"])
        } finally {
            stream.close()
        }
    }
}
