/*
 * Created On: Jul 20, 2006 5:28:11 PM
 */
package com.thinkparity.model.parity.model.container;

import java.io.IOException;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class UberDataTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [ADD DOCUMENT TEST]";

    /** Create AddDocumentTest. */
    public UberDataTest() { super(NAME); }

    /**
     * Test the container model's add document api.
     *
     */
    public void testUberData() {
        for(int i = 0; i < 1000; i++) {
            final ContainerModel cModel = getContainerModel();

            Container eContainer = null;
            try { eContainer = cModel.create(NAME); }
            catch(final ParityException px) { fail(createFailMessage(px)); }

            for(int j = 0; j < 1000; j++) {
                Document eDocument = null;
                try { eDocument = create(getInputFiles()[j % getInputFilesLength()]); }
                catch(final ParityException px) { fail(createFailMessage(px)); }
                catch(final IOException iox) { fail(createFailMessage(iox)); }
    
                try { cModel.addDocument(eContainer.getId(), eDocument.getId()); }
                catch(final ParityException px) { fail(createFailMessage(px)); }
            }
        }
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout();
        super.tearDown();
    }
}
