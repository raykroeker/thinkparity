/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.project;

import com.thinkparity.model.ModelTestCase;

/**
 * Test the project model getMyProjects api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class GetMyProjectsTest extends ModelTestCase {

	/**
	 * Create a GetMyProjectsTest.
	 */
	public GetMyProjectsTest() { super("testGetMyProjects"); }

	/**
	 * Test the project model getMyProjects api.
	 */
	public void testGetMyProjects() {
		try {
			final Project myProjects = getProjectModel().getMyProjects();
			assertNotNull(myProjects);
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}
}
