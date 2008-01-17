/*
 * Jan 16, 2006
 */
package com.thinkparity.codebase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotOfTypeAssertion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AssertTypeTest extends CodebaseTestCase {

	private class Fixture {
		private final boolean doAssert;
		private final Object instance;
		private final String message;
		private final Class<?> type;
		private Fixture(final boolean doAssert, final Object instance,
				final String message, final Class<?> type) {
			this.doAssert = doAssert;
			this.instance = instance;
			this.message = message;
			this.type = type;
		}
	}

	private Vector<Fixture> data;

	/**
	 * Create a AssertTypeTest.
	 * 
	 */
	public AssertTypeTest() { super("testAssertType"); }

	public void testAssertType() {
		try {
			for(Fixture datum : data) {
				try { Assert.assertOfType(datum.message, datum.type, datum.instance); }
				catch(NotOfTypeAssertion nota) {
					if(!datum.doAssert) { fail(datum.message); }
				}
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.data = new Vector<Fixture>(2);
		data.add(new Fixture(false, new Object(),
				"An Object is an Object.",
				Object.class));
		data.add(new Fixture(false, new Integer(0),
				"An Integer extends Number.",
				Number.class));
		data.add(new Fixture(false, new LinkedList<Object>(),
				"A LinkedList implements List.", List.class));
		data.add(new Fixture(false, new Vector<Object>(),
				"A Vector extends AbstractList extends AbstractCollection implements Collection.",
				Collection.class));
		data.add(new Fixture(true, new Float(0.0),
				"A Float extends Number extends Object.", Package.class));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
