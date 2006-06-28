/*
 * Created On: Jun 27, 2006 4:01:02 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Container Test Abstraction<br>
 * <b>Description:</b>A thinkParity container test abstraction.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class ContainerTestCase extends ModelTestCase {

    /**
     * Create ContainerTestCase.
     * 
     * @param name
     *            The test name.
     */
    public ContainerTestCase(final String name) { super(name); }

    /**
     * @see junit.framework.TestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {}

    /**
     * @see junit.framework.TestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {}
}
