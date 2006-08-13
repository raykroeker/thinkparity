/*
 * Created On: Feb 14, 2006
 */
package com.thinkparity.model.parity.model.document;

import com.thinkparity.model.parity.api.events.DocumentEvent;
import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.model.ModelTestCase;

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
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
    @Override
	protected void setUp() throws Exception { super.setUp(); }

    /**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
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
    protected abstract class Fixture implements DocumentListener {
        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#confirmationReceived(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void confirmationReceived(DocumentEvent e) {
            fail(getName() + "[CONFIRMATION RECEIVED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentArchived(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentArchived(DocumentEvent e) {
            fail(getName() + "[DOCUMENT ARCHIVED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentClosed(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentClosed(DocumentEvent e) {
            fail(getName() + "[DOCUMENT CLOSED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentCreated(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentCreated(DocumentEvent e) {
            fail(getName() + "[DOCUMENT CREATED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentDeleted(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentDeleted(DocumentEvent e) {
            fail(getName() + "[DOCUMENT DELETED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentPublished(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentPublished(DocumentEvent e) {
            fail(getName() + "[DOCUMENT PUBLISHED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentReactivated(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentReactivated(DocumentEvent e) {
            fail(getName() + "[DOCUMENT REACTIVATED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#documentUpdated(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void documentUpdated(DocumentEvent e) {
            fail(getName() + "[DOCUMENT UPDATED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#keyRequestAccepted(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void keyRequestAccepted(DocumentEvent e) {
            fail(getName() + "[KEY REQUEST ACCEPTED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#keyRequestDeclined(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void keyRequestDeclined(DocumentEvent e) {
            fail(getName() + "[KEY REQUEST DECLINED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#keyRequested(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void keyRequested(DocumentEvent e) {
            fail(getName() + "[KEY REQUESTED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#teamMemberAdded(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void teamMemberAdded(DocumentEvent e) {
            fail(getName() + "[TEAM MEMBER ADDED EVENT FIRED]");
        }

        /**
         * @see com.thinkparity.model.parity.api.events.DocumentListener#teamMemberRemoved(com.thinkparity.model.parity.api.events.DocumentEvent)
         */
        public void teamMemberRemoved(DocumentEvent e) {
            fail(getName() + "[TEAM MEMBER REMOVED EVENT FIRED]");
        }
    }
}
