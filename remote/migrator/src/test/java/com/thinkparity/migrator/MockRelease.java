/*
 * May 9, 2006 2:56:44 PM
 * $Id$
 */
package com.thinkparity.migrator;


/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class MockRelease extends Release {

    private static Long RELEASE_SEQ = 3000L;

    public static MockRelease create(final MigratorTestCase testCase) {
        return new MockRelease(testCase, RELEASE_SEQ++);
    }

    private MockRelease(final MigratorTestCase testCase, final Long releaseId) {
        super();
        this.setArtifactId("rMigrator");
        this.setGroupId("com.thinkparity.parity");
        this.setId(releaseId);
        this.setName("TMOCKRELEASE-" + releaseId);
        this.setVersion("1.0");
        this.addLibrary(MockLibrary.create(testCase));
        this.addLibrary(MockLibrary.createNative(testCase));
    }
}
