package com.thinkparity.codebase;

import com.thinkparity.codebase.StringUtil;

public class StringUtilTest extends CodebaseTestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(StringUtilTest.class);
	}

	public StringUtilTest(String name) {
		super(name);
	}

	public void testRemoveAfter() {
		final String case0Search = "user@domain/resource";
		final String case0Find = "/";
		final String case0Result = StringUtil.removeAfter(case0Search, case0Find);
		final String case0Target = "user@domain";
		assertEquals(case0Result, case0Target);

		final String case1Search = "user@domain/resource";
		final String case1Find = "";
		final String case1Result = StringUtil.removeAfter(case1Search, case1Find);
		final String case1Target = case1Search;
		assertEquals(case1Result, case1Target);

		final String case2Search = "user@domain/resource";
		final String case2Find = null;
		final String case2Result = StringUtil.removeAfter(case2Search, case2Find);
		final String case2Target = case2Search;
		assertEquals(case2Result, case2Target);

		final String case3Search = "user@domain/resource";
		final String case3Find = "userf";
		final String case3Result = StringUtil.removeAfter(case3Search, case3Find);
		final String case3Target = case3Search;
		assertEquals(case3Result, case3Target);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
