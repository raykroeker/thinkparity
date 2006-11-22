/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.install;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.ParityErrorTranslator;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.Constants.Image;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class InstallModelImpl extends AbstractModelImpl {

    /** Create InstallModelImpl. */
    InstallModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
    }

    /**
     * Install a release.
     * 
     * @param release
     *            A release.
     */
    void install(final Release release) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", release);
        try {
            final File installRoot = new File(
                    workspace.createTempDirectory("install-install"),
                    release.getVersion());
            final File downloadRoot = new File(
                    workspace.createTempDirectory("install-download"),
                    release.getVersion());
    
            // write the install image properties
            final Properties imageProperties =
                createImageProperties(release, new FileSystem(downloadRoot));
            final String header = new StringBuffer(release.getGroupId())
                    .append(":").append(release.getArtifactId())
                    .append(":").append(release.getVersion())
                    .toString();
    
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(
                        new File(downloadRoot, Image.PROPERTIES_FILENAME));
                imageProperties.store(fos, header);
            }
            catch(final IOException iox) { throw ParityErrorTranslator.translate(iox); }
            finally {
                if(null != fos) {
                    try { fos.close(); }
                    catch(final IOException iox) {
                        throw ParityErrorTranslator.translate(iox);
                    }
                }
            }
    
            // delete the download manifest
            final File downloadManifest = new File(downloadRoot, release.getVersion() + ".download");
            Assert.assertTrue(MessageFormat.format(
                    "[LMODEL] [INSTALL MODEL] [INSTALL] [CANNOT DELETE DOWNLOAD MANIFEST] [{0}]",
                    new Object[] {downloadManifest.getAbsolutePath()}),
                    downloadManifest.delete());
    
            // rename the download
            Assert.assertTrue(
                    MessageFormat.format(
                            "[LMODEL] [INSTALL MODEL] [INSTALL] [CANNOT RENAME DOWNLOAD TO INSTALL] [{0}] [{1}]",
                            new Object[] {downloadRoot.getAbsolutePath(), installRoot.getAbsolutePath()}),
                    downloadRoot.renameTo(installRoot));
    
            // delete the download
            FileUtil.deleteTree(downloadRoot);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Append to the class path all items within the path.
     * 
     * @param buffer
     *            The class path buffer.
     * @param path
     *            A file system path.
     */
    private void appendImageClassPath(final FileSystem fileSystem,
            final StringBuffer buffer, final String path) {
        final File[] files = fileSystem.listFiles(path);
        for(final File file : files) {
            if(0 < buffer.length()) { buffer.append(","); }
            buffer.append(path).append("/").append(file.getName());
        }
    }

    /**
     * Append to the library path all items within the path.
     * 
     * @param buffer
     *            The library path buffer.
     * @param path
     *            A file system path.
     */
    private void appendImageLibraryPath(final FileSystem fileSystem,
            final StringBuffer buffer, final String path) {
        final File[] directories = fileSystem.listDirectories(path);
        for(final File directory : directories) {
            if(0 < buffer.length()) { buffer.append(","); }
            buffer.append(path).append("/").append(directory.getName());
        }
    }

    /**
     * Create the image properties.
     * 
     * @param release
     *            The release.
     * @param fileSystem
     *            The file system.
     * @return The image properties.
     */
    private Properties createImageProperties(final Release release,
            final FileSystem fileSystem) {
        final Properties p = new Properties();
        p.setProperty("parity.image.classpath", getImageClassPathValue(fileSystem));
        p.setProperty("parity.image.librarypath", getImageLibraryPathValue(fileSystem));
        p.setProperty("parity.image.main", Image.MAIN);
        p.setProperty("parity.image.mainargs", Image.MAIN_ARGS);
        return p;
    }

    /**
     * Obtain the class path property value.
     * 
     * @return The class path property value.
     */
    private String getImageClassPathValue(final FileSystem fileSystem) {
        final StringBuffer buffer = new StringBuffer();
        appendImageClassPath(fileSystem, buffer, "core");
        appendImageClassPath(fileSystem, buffer, "lib");
        return buffer.toString();
    }

    /**
     * Obtain the library path property value.
     * 
     * @return The library path property value.
     */
    private String getImageLibraryPathValue(final FileSystem fileSystem) {
        final StringBuffer buffer = new StringBuffer();
        appendImageLibraryPath(fileSystem, buffer, "lib");
        return buffer.toString();
    }
}
