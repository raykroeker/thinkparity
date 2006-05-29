/*
 * May 9, 2006
 */
package com.thinkparity.migrator;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.raykroeker.junitx.TestCase;
import com.raykroeker.junitx.TestSession;
import com.thinkparity.migrator.model.library.LibraryModel;
import com.thinkparity.migrator.model.release.ReleaseModel;
import com.thinkparity.migrator.util.ChecksumUtil;

/**
 * The remote migrator's test case abstraction.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public abstract class MigratorTestCase extends TestCase {

    private static Long LIBRARY_VERSION_SUFFIX = 1000L;

    private static Long RELEASE_VERSION_SUFFIX = 1000L;

    static {
        final TestSession testSession = TestCase.getTestSession();
        final File dbDirectory = new File(testSession.getSessionDirectory(), "db.io");
        final File dbFile = new File(dbDirectory, "db");
        System.setProperty("hsqldb.file", dbFile.getAbsolutePath());
    }

    /**
     * Assert that two byte arrays are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected byte array.
     * @param actual
     *            The actual byte array.
     */
    protected static void assertEquals(final String assertion,
            final byte[] expected, final byte[] actual) {
        assertEquals(new StringBuffer(assertion).append(" [BYTE ARRAY LENGTH DOES NOT MATCH EXPECTATION]").toString(), expected.length, actual.length);
        for(int i = 0; i < expected.length; i++) {
            assertEquals(new StringBuffer(assertion).append(" [BYTE AT POSITION ").append(i).append(" DOES NOT MATCH EXPECTATION]").toString(), expected[i], actual[i]);
        }
    }

    /**
     * Assert that two libraries are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected library.
     * @param actual
     *            The actual library
     */
    protected static void assertEquals(final String assertion,
            final Library expected, final Library actual) {
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY DOES NOT EQUAL EXPECTATION]").toString(), (Object) expected, (Object) actual);
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY ARTIFACT ID DOES NOT MATCH EXPECTATION]").toString(), expected.getArtifactId(), actual.getArtifactId());
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY CREATED ON DOES NOT MATCH EXPECTATION]").toString(), expected.getCreatedOn(), actual.getCreatedOn()); 
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY GROUP ID DOES NOT MATCH EXPECTATION]").toString(), expected.getGroupId(), actual.getGroupId());
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY ID DOES NOT MATCH EXPECTATION]").toString(), expected.getId(), actual.getId());
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY PATH DOES NOT MATCH EXPECTATION]").toString(), expected.getPath(), actual.getPath());
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY TYPE DOES NOT MATCH EXPECTATION]").toString(), expected.getType(), actual.getType());
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY VERSION DOES NOT MATCH EXPECTATION]").toString(), expected.getVersion(), actual.getVersion());

    }

    /**
     * Assert that the two library bytes objects are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected library bytes.
     * @param actual
     *            The actual library bytes.
     */
    protected static void assertEquals(final String assertion,
            final LibraryBytes expected, final LibraryBytes actual) {
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY BYTES DOES NOT EQUAL EXPECTATION]").toString(), (Object) expected, (Object) actual);
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY BYTES LIBRARY ID DOES NOT MATCH EXPECTATION]").toString(), expected.getLibraryId(), actual.getLibraryId());
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY BYTES DO NOT MATCH EXPECTATION]").toString(), expected.getBytes(), actual.getBytes()); 
        assertEquals(new StringBuffer(assertion).append(" [LIBRARY BYTES CHECKSUM DOES NOT MATCH EXPECTATION]").toString(), expected.getChecksum(), actual.getChecksum());
    }

    /**
     * Assert that two releases are equal.
     * 
     * @param assertion
     *            the assertion.
     * @param expected
     *            the expected release.
     * @param actual
     *            The actual release.
     */
    protected static void assertEquals(final String assertion,
            final Release expected, final Release actual) {
        assertEquals(new StringBuffer(assertion).append(" [RELEASE DOES NOT EQUAL EXPECTATION]").toString(), (Object) expected, (Object) actual);
        assertEquals(new StringBuffer(assertion).append(" [RELEASE ARTIFACT ID DOES NOT MATCH EXPECTATION]").toString(), expected.getArtifactId(), actual.getArtifactId());
        assertEquals(new StringBuffer(assertion).append(" [RELEASE CREATED ON DOES NOT MATCH EXPECTATION]").toString(), expected.getCreatedOn(), actual.getCreatedOn()); 
        assertEquals(new StringBuffer(assertion).append(" [RELEASE GROUP ID DOES NOT MATCH EXPECTATION]").toString(), expected.getGroupId(), actual.getGroupId());
        assertEquals(new StringBuffer(assertion).append(" [RELEASE ID DOES NOT MATCH EXPECTATION]").toString(), expected.getId(), actual.getId());
        assertEquals(new StringBuffer(assertion).append(" [RELEASE VERSION DOES NOT MATCH EXPECTATION]").toString(), expected.getVersion(), actual.getVersion());
    }

    /**
     * Assert that a library is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param library
     *            A library.
     */
    protected static void assertNotNull(final String assertion,
            final Library library) {
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY IS NULL]").toString(), (Object) library);
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY ARTIFACT ID IS NULL]").toString(), library.getArtifactId());
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY CREATED ON IS NULL]").toString(), library.getCreatedOn());
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY GROUP ID IS NULL]").toString(), library.getGroupId());
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY ID IS NULL]").toString(), library.getId());
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY PATH IS NULL]").toString(), library.getPath());
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY TYPE IS NULL]").toString(), library.getType());
        assertNotNull(new StringBuffer(assertion).append(" [LIBRARY VERSION IS NULL]").toString(), library.getVersion());
    }

    /**
     * Assert that a release is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param release
     *            A release.
     */
    protected static void assertNotNull(final String assertion,
            final Release release) {
        assertNotNull(new StringBuffer(assertion).append(" [RELEASE IS NULL]").toString(), (Object) release);
        assertNotNull(new StringBuffer(assertion).append(" [RELEASE ARTIFACT ID IS NULL]").toString(), release.getArtifactId());
        assertNotNull(new StringBuffer(assertion).append(" [RELEASE CREATED ON IS NULL]").toString(), release.getCreatedOn());
        assertNotNull(new StringBuffer(assertion).append(" [RELEASE GROUP ID NULL]").toString(), release.getGroupId());
        assertNotNull(new StringBuffer(assertion).append(" [RELEASE ID IS NULL]").toString(), release.getId());
        assertNotNull(new StringBuffer(assertion).append(" [RELEASE VERSION IS NULL]").toString(), release.getVersion());
    }
 
    /** The parity library interface. */
    private final LibraryModel lModel;

    /** The parity release interface. */
    private final ReleaseModel rModel;

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

    protected Byte[] autobox(final byte[] unboxed) {
        final Byte[] boxed = new Byte[unboxed.length];
        for(int i = 0; i < unboxed.length; i++) boxed[i] = unboxed[i];
        return boxed;
    }

    protected byte[] autobox(final Byte[] bytes) {
        final byte[] boxed = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) { boxed[i] = bytes[i]; }
        return boxed;
    }

    /** Create a java test library. */
    protected Library createJavaLibrary() {
        final Library javaLibrary =  getJavaLibrary();
        final Library createdJavaLibrary = lModel.create(
                javaLibrary.getArtifactId(), javaLibrary.getGroupId(),
                javaLibrary.getPath(), javaLibrary.getType(), javaLibrary.getVersion());
        final byte[] javaLibraryBytes = getJavaLibraryBytes(javaLibrary);
        final String javaLibraryChecksum = getJavaLibraryChecksum(javaLibraryBytes);
        lModel.createBytes(createdJavaLibrary.getId(), javaLibraryBytes, javaLibraryChecksum);
        return createdJavaLibrary;
    }

    /** Create a native test library. */
    protected Library createNativeLibrary() {
        final Library nativeLibrary =  getJavaLibrary();
        final Library createdNativeLibrary = lModel.create(
                nativeLibrary.getArtifactId(), nativeLibrary.getGroupId(),
                nativeLibrary.getPath(), nativeLibrary.getType(),
                nativeLibrary.getVersion());

        final byte[] nativeLibraryBytes = getNativeLibraryBytes(nativeLibrary);
        final String nativeLibraryChecksum = getNativeLibraryChecksum(nativeLibraryBytes);
        lModel.createBytes(createdNativeLibrary.getId(), nativeLibraryBytes, nativeLibraryChecksum);
        return createdNativeLibrary;
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

    protected List<Long> extractIds(final List<Library> libraries) {
        final List<Long> ids = new LinkedList<Long>();
        for(final Library library : libraries) ids.add(library.getId());
        return ids;
    }

    protected File[] getInputFiles() throws IOException {
        return super.getInputFiles();
    }

    /** Obtain a test java library. */
    protected Library getJavaLibrary() {
        final Library library = new Library();
        library.setArtifactId("testLibrary");
        library.setGroupId("com.thinkparity.parity");
        library.setType(Library.Type.JAVA);
        library.setVersion("1.0.0." + LIBRARY_VERSION_SUFFIX++);
        library.setPath(new StringBuffer("core/")
                .append(library.getArtifactId())
                .append("-").append(library.getVersion())
                .append(".jar").toString());
        return library;
    }

    /** Obtain java library byte content. */
    protected byte[] getJavaLibraryBytes(final Library javaLibrary) {
        return new StringBuffer(javaLibrary.getGroupId())
            .append(":").append(javaLibrary.getArtifactId())
            .append(":").append(javaLibrary.getType())
            .toString().getBytes();
    }

    /** Obtain a java library content checksum. */
    protected String getJavaLibraryChecksum(final byte[] javaLibraryBytes) {
        return ChecksumUtil.md5Hex(javaLibraryBytes);
    }

    protected LibraryModel getLibraryModel(final Class clasz) {
        return LibraryModel.getModel();
    }

    /** Obtain a test native library. */
    protected Library getNativeLibrary() {
        final Library library = new Library();
        library.setArtifactId("testLibrary");
        library.setGroupId("com.thinkparity.parity");
        library.setType(Library.Type.NATIVE);
        library.setVersion("1.0.0." + LIBRARY_VERSION_SUFFIX++);
        library.setPath(new StringBuffer("lib/win32/")
                .append(library.getArtifactId())
                .append("-").append(library.getVersion())
                .append(".dll").toString());
        return library;
    }

    /** Obtain native library byte content. */
    protected byte[] getNativeLibraryBytes(final Library nativeLibrary) {
        return new StringBuffer(nativeLibrary.getGroupId())
            .append(":").append(nativeLibrary.getArtifactId())
            .append(":").append(nativeLibrary.getType())
            .toString().getBytes();
    }

    /** Obtain a native library content checksum. */
    protected String getNativeLibraryChecksum(final byte[] bytes) {
        return ChecksumUtil.md5Hex(bytes);
    }

    /** Obtain a test release. */
    protected Release getRelease() {
        final Release release = new Release();
        release.setArtifactId("testRelease");
        release.setGroupId("com.thinkparity.parity");
        release.setVersion("1.0.0." + RELEASE_VERSION_SUFFIX++);
        return release;
    }

    protected ReleaseModel getReleaseModel(final Class clasz) {
        return ReleaseModel.getModel();
    }
}
