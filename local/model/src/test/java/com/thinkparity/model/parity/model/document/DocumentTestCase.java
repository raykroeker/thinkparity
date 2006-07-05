/*
 * Created On: Feb 14, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document;




import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class DocumentTestCase extends ModelTestCase {

    /** The parity document interface implementation. */
    private DocumentModelImpl impl;

	/**
	 * Create a DocumentTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	public DocumentTestCase(final String name) { super(name); }

	protected DocumentModelImpl getImpl() {
        if(null == impl) {
            impl = new DocumentModelImpl(WorkspaceModel.getModel().getWorkspace());
        }
        return impl;
    }

    /**
     * @param d1
     * @throws ParityException
     */
    protected void sendKey(final Document d1) throws ParityException {
        getSessionModel().sendKeyResponse(d1.getId(),
                ModelTestUser.getX().getJabberId(), KeyResponse.ACCEPT);
    }

    /**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();

		login();
	}

    /**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		logout();

		super.tearDown();
	}
}
