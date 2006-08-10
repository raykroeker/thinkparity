/*
 * Created On: Aug 2, 2006 9:15:03 AM
 */
package com.thinkparity.model.handler.artifact;

import com.thinkparity.model.handler.HandlerTestCase;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ArtifactHandlerTestCase extends HandlerTestCase {

    /** Create ArtifactHandlerTestCase. */
    ArtifactHandlerTestCase(String name) { super(name); }

    /**
     * @see com.thinkparity.model.handler.HandlerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception { super.setUp(); }

    /**
     * @see com.thinkparity.model.handler.HandlerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception { super.tearDown(); }
}
