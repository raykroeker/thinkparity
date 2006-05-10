/*
 * May 9, 2006 2:56:44 PM
 * $Id$
 */
package com.thinkparity.migrator;

import java.io.IOException;

import com.raykroeker.junitx.TestCase;

import com.thinkparity.codebase.FileUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class MockLibrary extends Library {

    private static Long LIBRARY_SEQ = 2000L;

    public static MockLibrary create(final MigratorTestCase testCase) {
        return new MockLibrary(testCase, LIBRARY_SEQ++, Type.JAVA);
    }

    public static MockLibrary createNative(final MigratorTestCase testCase) {
        return new MockLibrary(testCase, LIBRARY_SEQ++, Type.NATIVE);
    }

    private Byte[] bytes;

    private MockLibrary(final MigratorTestCase testCase, final Long libraryId, final Type type) {
        super();
        try { setBytes(FileUtil.readBytes(testCase.getInputFiles()[0])); }
        catch(final IOException iox) { throw new RuntimeException(iox); }
        setArtifactId("rMigrator");
        setGroupId("com.thinkparity.parity");
        setId(libraryId);
        setType(type);
        setVersion("1.0." + System.currentTimeMillis());
    }

    public Byte[] getBytes() { return bytes; }

    public void setBytes(final Byte[] bytes) {
        this.bytes = new Byte[bytes.length];
        System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = new Byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) { this.bytes[i] = bytes[i]; }
    }
}
