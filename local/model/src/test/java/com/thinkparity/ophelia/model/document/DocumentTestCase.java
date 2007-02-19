/*
 * Created On: Feb 14, 2006
 */
package com.thinkparity.ophelia.model.document;


import java.io.File;
import java.io.IOException;

import com.thinkparity.ophelia.model.ModelTestCase;
import com.thinkparity.ophelia.model.events.DocumentEvent;
import com.thinkparity.ophelia.model.events.DocumentListener;

/**
 * <b>Title:</b>thinkParity Document Test Case<br>
 * <b>Description:</b>An abstraction of a document test case. Within the setup
 * for this abstraction a user's session is established.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class DocumentTestCase extends ModelTestCase {

    /**
	 * Create DocumentTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	protected DocumentTestCase(final String name) { super(name); }

    /**
     * Obtain an output directory for a container test case. This will locate
     * the output directory in a per-test director beneath a common container
     * root.
     * 
     * @return A directory <code>File</code>.
     */
    @Override
    public File getOutputDirectory() {
        final File parentFile = new File(super.getOutputDirectory(), "Container");
        final File outputDirectory = new File(parentFile, getName());
        if (!outputDirectory.exists())
            assertTrue(outputDirectory.mkdirs());
        return outputDirectory;
    }

	/**
     * Obtain an output file for a container test case. This will locate
     * the output directory in a per-test director beneath a common container
     * root.
     * 
     * @return A <code>File</code>.
     */
    public File getOutputFile(final String name, final Boolean create) {
        final File file = new File(getOutputDirectory(), name);
        if (file.exists() && create.booleanValue()) {
            fail("Ouput file for test already exists.");
        } else {
            try {
                if (!file.createNewFile()) {
                    fail("Output file for test cannot be created.");
                }
            } catch (final IOException iox) {
                fail(createFailMessage("Output file for test cannot be created.", iox));
            }
        }
        return file;
    }

    /**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 * 
	 */
    @Override
	protected void setUp() throws Exception { super.setUp(); }

    /**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
	 * 
	 */
    @Override
	protected void tearDown() throws Exception { super.tearDown(); }

    /**
     * <b>Title:</b>thinkParity Document Test Fixture<br>
     * <b>Description:</b>A thinkParity container test fixture provides a way
     * to abstract the document listener events to a central location. If any
     * events are fired in the abstraction the test case will fail.
     * 
     */
    protected abstract class Fixture extends ModelTestCase.Fixture implements
            DocumentListener {
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#confirmationReceived(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void confirmationReceived(DocumentEvent e) {
            fail(getName() + "[CONFIRMATION RECEIVED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#documentArchived(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void documentArchived(DocumentEvent e) {
            fail(getName() + "[DOCUMENT ARCHIVED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#documentClosed(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void documentClosed(DocumentEvent e) {
            fail(getName() + "[DOCUMENT CLOSED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#documentCreated(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void documentCreated(DocumentEvent e) {
            fail(getName() + "[DOCUMENT CREATED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#documentDeleted(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void documentDeleted(DocumentEvent e) {
            fail(getName() + "[DOCUMENT DELETED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#documentPublished(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void documentPublished(DocumentEvent e) {
            fail(getName() + "[DOCUMENT PUBLISHED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#documentReactivated(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void documentReactivated(DocumentEvent e) {
            fail(getName() + "[DOCUMENT REACTIVATED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#documentUpdated(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void documentUpdated(DocumentEvent e) {
            fail(getName() + "[DOCUMENT UPDATED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#keyRequestAccepted(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void keyRequestAccepted(DocumentEvent e) {
            fail(getName() + "[KEY REQUEST ACCEPTED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#keyRequestDeclined(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void keyRequestDeclined(DocumentEvent e) {
            fail(getName() + "[KEY REQUEST DECLINED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#keyRequested(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void keyRequested(DocumentEvent e) {
            fail(getName() + "[KEY REQUESTED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#teamMemberAdded(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void teamMemberAdded(DocumentEvent e) {
            fail(getName() + "[TEAM MEMBER ADDED EVENT FIRED]");
        }
        /**
         * @see com.thinkparity.ophelia.model.events.DocumentListener#teamMemberRemoved(com.thinkparity.ophelia.model.events.DocumentEvent)
         */
        public void teamMemberRemoved(DocumentEvent e) {
            fail(getName() + "[TEAM MEMBER REMOVED EVENT FIRED]");
        }
    }
}
