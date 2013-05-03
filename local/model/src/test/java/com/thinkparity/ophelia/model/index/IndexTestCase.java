/*
 * Mar 7, 2006
 */
package com.thinkparity.ophelia.model.index;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;


import com.thinkparity.ophelia.model.ModelTestCase;
import com.thinkparity.ophelia.model.index.IndexHit;

/**
 * <b>Title:</b>thinkParity Index Test Abstraction<br>
 * <b>Description:</b>An abstraction for index test implementations. Provides
 * assertions for index hit lists and index hits.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
abstract class IndexTestCase extends ModelTestCase {

	/**
     * Assert that an index hit and all of its required members are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param indexHit
     *            An index hit.
     */
    protected static void assertNotNull(final String assertion,
            final IndexHit indexHit) {
        assertNotNull(assertion + " [INDEX HIT IS NULL]", (Object) indexHit);
        throw Assert.createNotYetImplemented("IndexTestCase#assertNotNull");
//        assertNotNull(assertion + " [INDEX HIT ID IS NULL]", indexHit.getId());
//        assertNotNull(assertion + " [INDEX HIT TYPE IS NULL]", indexHit.getType());
    }

    /**
     * Assert that list of index hits is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param indexHits
     *            A list of index hits.
     */
    protected static void assertNotNull(final String assertion,
            final List<IndexHit> indexHits) {
        assertNotNull(assertion + " [INDEX HIT LIST IS NULL]", (Object) indexHits);
        for(final IndexHit indexHit : indexHits) {
            assertNotNull(assertion, indexHit);
        }
    }

	/**
	 * Create a IndexTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	protected IndexTestCase(final String name) { super(name); }

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception { super.setUp(); }

     /**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception { super.tearDown(); }
}
