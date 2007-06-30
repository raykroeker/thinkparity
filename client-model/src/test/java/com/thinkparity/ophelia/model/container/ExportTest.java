/*
 * Created On: Aug 1, 2006 9:13:19 AM
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Export Test<br>
 * <b>Description:</b>thinkParity Container Export Test<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ExportTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "Export Test";

    /** Test datum. */
    private Fixture datum;

    /** Create ExportTest. */
    public ExportTest() { super(NAME); }

    /**
     * Test the container model create api.
     *
     */
    public void testExport() {
        final Container c = createContainer(datum.junit, NAME);
        final List<Document> d_list = addDocuments(datum.junit, c.getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        // remove half of the documents
        for (int i = 0; i < d_list.size(); i++) {
            if (1 == i % 2)
                removeDocument(datum.junit, c.getId(), d_list.get(i).getId());
        }
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        // re-add half of the documents
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        final String[] inputFileNames = getInputFileNames();
        for (int i = 0; i < inputFileNames.length; i++) {
            if (1 == i % 2) {
                addDocument(datum.junit, c.getId(), inputFileNames[i]);
            }
        }
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        exportContainer(datum.junit, c.getId(),
                new File(getOutputDirectory(), c.getName() + ".zip"));
    }

    /**
     * Test the export of the audit report.
     *
     */
    public void testExportAuditReport() {
        final Container c = createContainer(datum.junit, NAME);
        final List<Document> d_list = addDocuments(datum.junit, c.getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        // remove half of the documents
        for (int i = 0; i < d_list.size(); i++) {
            if (1 == i % 2)
                removeDocument(datum.junit, c.getId(), d_list.get(i).getId());
        }
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        // re-add half of the documents
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        final String[] inputFileNames = getInputFileNames();
        for (int i = 0; i < inputFileNames.length; i++) {
            if (1 == i % 2) {
                addDocument(datum.junit, c.getId(), inputFileNames[i]);
            }
        }
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        exportContainerAuditReport(datum.junit, c.getId(),
                new File(getOutputDirectory(), c.getName() + ".pdf"));
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
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
     * 
     */
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            super();
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
    }
}
