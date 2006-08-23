/*
 * Created On: Aug 23, 2006 1:04:52 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RevertPrePublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST REVERT PRE PUBLISH";

    /** Test datum. */
    private Fixture datum;

    /** Create RevertPrePublishTest. */
    public RevertPrePublishTest() { super(NAME); }

    /** Test the rename api. */
    public void testRevert() {
        Boolean didAssert;
        try {
            datum.containerModel.revertDocument(
                    datum.container.getId(), datum.document.getId());
            didAssert = Boolean.FALSE;
        } catch (final NotTrueAssertion nta) {
            didAssert = Boolean.TRUE;
            if (!nta.getMessage().equals("LATEST VERSION DOES NOT EXIST")) {
                fail(createFailMessage(nta));
            }
        }
        assertTrue(NAME + " REVERT WAS PERFORMED", didAssert);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        final Document document = addDocument(container, getInputFiles()[0]);
        modifyDocument(document);
        datum = new Fixture(container, containerModel, document);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final InternalContainerModel containerModel;
        private final Document document;
        private Fixture(final Container container,
                final InternalContainerModel containerModel, final Document document) {
            this.container = container;
            this.containerModel = containerModel;
            this.document = document;
        }
    }
}
