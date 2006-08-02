/*
 * Created On: Jun 27, 2006 4:01:02 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;


import java.util.List;

import com.thinkparity.model.parity.model.ModelTestCase;


/**
 * <b>Title:</b>thinkParity Container Test Abstraction<br>
 * <b>Description:</b>A thinkParity container test abstraction.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
abstract class ContainerTestCase extends ModelTestCase {

    /**
     * Assert the containers are not null.
     * 
     * @param assertion
     *            An assertion.
     * @param containers
     *            A list of containers.
     */
    protected static void assertNotNull(final String assertion, final List<Container> containers) {
        assertNotNull(assertion + " [CONTAINERS IS NULL]", (Object) containers);
        for(final Container container : containers) {
            assertNotNull(assertion, container);
        }
    }

    /**
     * Create ContainerTestCase.
     * 
     * @param name
     *            The test name.
     */
    protected ContainerTestCase(final String name) { super(name); }

    /**
     * @see junit.framework.TestCase#setUp()
     * 
     */
    protected void setUp() throws Exception { super.setUp(); }

    /**
     * @see junit.framework.TestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception { super.tearDown(); }
}
