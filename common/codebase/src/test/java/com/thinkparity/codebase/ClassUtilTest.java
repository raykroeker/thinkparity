package com.thinkparity.codebase;


public class ClassUtilTest extends CodebaseTestCase {


	private StringBuffer[] testGetPathData;

	public ClassUtilTest() {
		super("Class util test");
	}

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
