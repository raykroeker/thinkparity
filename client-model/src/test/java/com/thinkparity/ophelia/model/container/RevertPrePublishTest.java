/*
 * Created On: Aug 23, 2006 1:04:52 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RevertPrePublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Revert pre publish test.";

    /** Test datum. */
    private Fixture datum;

    /** Create RevertPrePublishTest. */
    public RevertPrePublishTest() { super(NAME); }

    /** Test the rename api. */
    public void testRevert() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        modifyDocument(datum.junit, d.getId());
        Boolean didAssert;
        try {
            didAssert = Boolean.FALSE;
            revertDocument(datum.junit, c.getId(), d.getId());
        } catch (final NotTrueAssertion nta) {
            didAssert = Boolean.TRUE;
            if (!nta.getMessage().equals("LATEST VERSION DOES NOT EXIST")) {
                fail(createFailMessage(nta));
            }
        }
        assertTrue("Revert did not assert.", didAssert);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X,
                OpheliaTestUser.JUNIT_Y);
        login(datum.junit);
        login(datum.junit_x);
        login(datum.junit_y);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
    }
}
