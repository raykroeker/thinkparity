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
public class VersionTest extends CodebaseTestCase {

    /** Test fixture. */
	private class Fixture {
		private final String name;
        private final String version;
		private Fixture(final String name, final String version) {
			this.name = name;
			this.version = version;
		}
	}

    /** Test data. */
	private List<Fixture> data;

	/** Create a VersionTest. */
	public VersionTest() { super("Version Test"); }

    /** Test Version#getName(). */
	public void testName() {
        for(final Fixture datum : data) {
            assertEquals(datum.name, Version.getName());
        }
	}

    /** Test Version#getName(). */
	public void testVersion() {
        for(final Fixture datum : data) {
            assertEquals(datum.version, Version.getVersion());
        }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		data = new LinkedList<Fixture>();
		data.add(new Fixture("thinkParity - Codebase", "1.0-SNAPSHOT"));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
        data.clear();
        data = null;

		super.tearDown();
	}
}
