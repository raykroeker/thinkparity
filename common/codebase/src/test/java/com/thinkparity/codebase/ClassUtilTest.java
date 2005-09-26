package com.thinkparity.codebase;

import com.thinkparity.codebase.ClassUtil;

public class ClassUtilTest extends CodebaseTestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ClassUtilTest.class);
	}

	private StringBuffer[] testGetPathData;

	public void testGetPath() {
		final StringBuffer path = ClassUtil.getPath(ClassUtilTest.class);
		assertNotNull("Class path is null.", path);
		assertEquals("Class path not correct.", testGetPathData[1].toString(),
				path.toString());
	}

	public void testGetPathNull() {
		final StringBuffer path = ClassUtil.getPath(null);
		assertNotNull("Class path is null.", path);
		assertEquals("Class path not correct.", testGetPathData[0].toString(),
				path.toString());
	}

	protected void setUp() throws Exception {
		super.setUp();
		testGetPathData = new StringBuffer[2];
		testGetPathData[0] = new StringBuffer(0);
		testGetPathData[1] = new StringBuffer("com/thinkparity/codebase");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
