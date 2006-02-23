/*
 * Feb 22, 2006
 */
package com.thinkparity.codebase;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CollectionsUtilTest extends CodebaseTestCase {

	private Collection<String> myCollection;

	
	/**
	 * Create a CollectionsUtilTest.
	 */
	public CollectionsUtilTest() { super("Collections Util Test"); }


	/**
	 * Test the StackUtil getCallerMethodName api.
	 *
	 */
	public void testProxy() {
		final List<String> myList = CollectionsUtil.proxy(myCollection);
		assertNotNull("Proxied list is null.", myList);
		assertEquals("First element not equal to expectation.",
				myList.get(0), "ABC");
		assertEquals("Second element not equal to expectation.",
				myList.get(1), "XYZ");
		assertEquals("Third element not equal to expectation.",
				myList.get(2), "123");
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();

		myCollection = new Vector<String>(3);
		myCollection.add("ABC");
		myCollection.add("XYZ");
		myCollection.add("123");
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		myCollection.clear();
		myCollection = null;
		super.tearDown();
	}
}
