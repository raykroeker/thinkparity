/*
 * May 9, 2006
 */
package com.thinkparity.migrator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

import com.raykroeker.junitx.TestCase;
import com.raykroeker.junitx.TestSession;
import com.thinkparity.migrator.model.library.LibraryModel;
import com.thinkparity.migrator.model.release.ReleaseModel;

/**
 * The remote migrator's test case abstraction.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public abstract class MigratorTestCase extends TestCase {

    private static Long RELEASE_VERSION_SUFFIX = 1000L;

    private static Long LIBRARY_VERSION_SUFFIX = 1000L;

    static {
        final TestSession testSession = TestCase.getTestSession();
        final File dbDirectory = new File(testSession.getSessionDirectory(), "db.io");
        final File dbFile = new File(dbDirectory, "db");
        System.setProperty("hsqldb.file", dbFile.getAbsolutePath());
    }

    /** The parity release interface. */
    private final ReleaseModel rModel;

    /** The parity library interface. */
    private final LibraryModel lModel;

    /**
     * Create MigratorTestCase.
     * 
     * @param name
     *            The test case name.
     */
    protected MigratorTestCase(final String name) {
        super(name);
        this.lModel = LibraryModel.getModel();
        this.rModel = ReleaseModel.getModel();
    }

    protected File[] getInputFiles() throws IOException {
        return super.getInputFiles();
    }

    protected LibraryModel getLibraryModel(final Class clasz) {
        return LibraryModel.getModel();
    }

    protected ReleaseModel getReleaseModel(final Class clasz) {
        return ReleaseModel.getModel();
    }

    /** Obtain a test release. */
    protected Release getRelease() {
        final Release release = new Release();
        release.setArtifactId("testRelease");
        release.setGroupId("com.thinkparity.parity");
        release.setVersion("1.0.0." + RELEASE_VERSION_SUFFIX++);
        return release;
    }

    /** Create a test release. */
    protected Release createRelease() {
        final List<Library> libraries = new LinkedList<Library>();
        libraries.add(createJavaLibrary());
        libraries.add(createNativeLibrary());
        final Release release = getRelease();
        return rModel.create(release.getArtifactId(), release.getGroupId(),
                release.getVersion(), libraries);
    }

    /** Obtain a test java library. */
    protected Library getJavaLibrary() {
        final Library library = new Library();
        library.setArtifactId("testLibrary");
        library.setGroupId("com.thinkparity.parity");
        library.setType(Library.Type.JAVA);
        library.setVersion("1.0.0." + LIBRARY_VERSION_SUFFIX++);
        return library;
    }

    /** Obtain a test native library. */
    protected Library getNativeLibrary() {
        final Library library = new Library();
        library.setArtifactId("testLibrary");
        library.setGroupId("com.thinkparity.parity");
        library.setType(Library.Type.NATIVE);
        library.setVersion("1.0.0." + LIBRARY_VERSION_SUFFIX++);
        return library;
    }

    /** Obtain java library byte content. */
    protected Byte[] getJavaLibraryBytes() {
        final Library javaLibrary = getJavaLibrary();
        return autobox(new StringBuffer(javaLibrary.getGroupId())
            .append(":").append(javaLibrary.getArtifactId())
            .append(":").append(javaLibrary.getType())
            .toString().getBytes());
    }

    /** Obtain native library byte content. */
    protected Byte[] getNativeLibraryBytes() {
        final Library nativeLibrary = getNativeLibrary();
        return autobox(new StringBuffer(nativeLibrary.getGroupId())
            .append(":").append(nativeLibrary.getArtifactId())
            .append(":").append(nativeLibrary.getType())
            .toString().getBytes());
    }

    protected Byte[] autobox(final byte[] unboxed) {
        final Byte[] boxed = new Byte[unboxed.length];
        for(int i = 0; i < unboxed.length; i++) boxed[i] = unboxed[i];
        return boxed;
    }

    /** Create a java test library. */
    protected Library createJavaLibrary() {
        final Library javaLibrary =  getJavaLibrary();
        final Library createdJavaLibrary = lModel.create(
                javaLibrary.getArtifactId(), javaLibrary.getGroupId(),
                javaLibrary.getType(), javaLibrary.getVersion());
        lModel.createBytes(createdJavaLibrary.getId(), getJavaLibraryBytes());
        return createdJavaLibrary;
    }

    /** Create a native test library. */
    protected Library createNativeLibrary() {
        final Library nativeLibrary =  getJavaLibrary();
        final Library createdNativeLibrary = lModel.create(
                nativeLibrary.getArtifactId(), nativeLibrary.getGroupId(),
                nativeLibrary.getType(), nativeLibrary.getVersion());
        lModel.createBytes(createdNativeLibrary.getId(), getNativeLibraryBytes());
        return createdNativeLibrary;
    }

    protected List<Long> extractIds(final List<Library> libraries) {
        final List<Long> ids = new LinkedList<Long>();
        for(final Library library : libraries) ids.add(library.getId());
        return ids;
    }
}
