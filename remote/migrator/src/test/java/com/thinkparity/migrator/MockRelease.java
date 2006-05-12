/*
 * May 9, 2006 2:56:44 PM
 * $Id$
 */
package com.thinkparity.migrator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class MockRelease extends Release {

    private static Long RELEASE_SEQ = 3000L;

    public static MockRelease create(final MigratorTestCase testCase) {
        return new MockRelease(testCase, RELEASE_SEQ++);
    }

    private final List<MockLibrary> mockLibraries;

    private MockRelease(final MigratorTestCase testCase, final Long releaseId) {
        super();
        this.mockLibraries = new LinkedList<MockLibrary>();
        setArtifactId("rMigrator");
        setGroupId("com.thinkparity.parity");
        setId(releaseId);
        setName("TMOCKRELEASE-" + releaseId);
        setVersion("1.0." + System.currentTimeMillis());
        addLibrary(MockLibrary.create(testCase));
        addLibrary(MockLibrary.createNative(testCase));
    }

    public void addLibrary(final MockLibrary mockLibrary) {
        super.addLibrary(mockLibrary);
        this.mockLibraries.add(mockLibrary);
    }

    public List<Long> getLibraryIds() {
        final List<Long> libraryIds = new LinkedList<Long>();
        for(final Library library : getLibraries()) { libraryIds.add(library.getId()); }
        return libraryIds;
    }

    public List<MockLibrary> getMockLibraries() { return mockLibraries; }

    public void removeLibrary(final MockLibrary mockLibrary) {
        super.removeLibrary(mockLibrary);
        this.mockLibraries.remove(mockLibrary);
    }
}
