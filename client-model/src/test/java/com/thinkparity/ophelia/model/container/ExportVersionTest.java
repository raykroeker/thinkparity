/*
 * Created On: Aug 1, 2006 9:13:19 AM
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Export Test<br>
 * <b>Description:</b>thinkParity Container Export Test<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ExportVersionTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "Export Version Test";

    /** Test datum. */
    private Fixture datum;

    /** Create ExportTest. */
    public ExportVersionTest() { super(NAME); }

    /**
     * Test the container model create api.
     *
     */
    public void testExport() {
        datum.containerModel.exportVersion(datum.exportDirectory,
                datum.container.getId(), datum.version.getVersionId());
        Assert.assertTrue(true,"");
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        login(OpheliaTestUser.JUNIT_X);
        final InternalContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        final List<Document> documents = addDocuments(OpheliaTestUser.JUNIT, container);
        publishToContacts(OpheliaTestUser.JUNIT, container);

        createDraft(OpheliaTestUser.JUNIT, container);
        // remove half of the documents
        for (int i = 0; i < documents.size(); i++) {
            if (1 == i % 2)
                containerModel.removeDocument(container.getId(),
                        documents.get(i).getId());
        }
        publishToTeam(OpheliaTestUser.JUNIT, container);

        // re-add half of the documents
        createDraft(OpheliaTestUser.JUNIT_X, container);
        final File[] inputFiles = getInputFiles();
        for (int i = 0; i < inputFiles.length; i++) {
            if (1 == i % 2) {
                addDocument(OpheliaTestUser.JUNIT, container, inputFiles[i]);
            }
        }
        publishToTeam(OpheliaTestUser.JUNIT_X, container);

        final ContainerVersion version = containerModel.readLatestVersion(container.getId());
        datum = new Fixture(container, containerModel, getOutputDirectory(), version);
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout(OpheliaTestUser.JUNIT);
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private final File exportDirectory;
        private final ContainerVersion version;
        private Fixture(final Container container,
                final ContainerModel containerModel,
                final File exportDirectory, final ContainerVersion version) {
            this.containerModel = containerModel;
            this.container = container;
            this.exportDirectory = exportDirectory;
            this.version = version;
        }
    }
}
