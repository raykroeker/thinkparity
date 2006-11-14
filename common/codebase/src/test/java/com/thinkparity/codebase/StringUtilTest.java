package com.thinkparity.codebase;

import java.nio.charset.Charset;

import junit.framework.Assert;

public class StringUtilTest extends CodebaseTestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(StringUtilTest.class);
	}

	protected Charset CHARSET_ISO_8559_1;
	protected Charset CHARSET_US_ASCII;
	protected Charset CHARSET_UTF_16;
	protected Charset CHARSET_UTF_16_BE;
	protected Charset CHARSET_UTF_16_LE;
	protected Charset CHARSET_UTF_8;

	public StringUtilTest(String name) {
		super(name);
	}

    public void testToString() {
        Pair<String, String> pair = new Pair<String, String>("one", "two");
        String toString = StringUtil.toString(Pair.class, "pair.getOne()", pair.getOne(),
                "pair.getTwo()", pair.getTwo());
        assertEquals("com.thinkparity.codebase.Pair$pair.getOne():one,pair.getTwo():two", toString);

        // null member data
        pair = null;
        toString = StringUtil.toString(Pair.class, pair);
        assertEquals("com.thinkparity.codebase.Pair$null", toString);

        // odd number of member data
        pair = new Pair<String, String>("one", "two");
        toString = StringUtil.toString(Pair.class, "pair.getOne()", pair.getOne(),
                "pair.getTwo()");
        assertEquals("com.thinkparity.codebase.Pair$pair.getOne():one,pair.getTwo():null", toString);
    }

	public void testCharset() {
		Charset charset = StringUtil.Charset.ISO_8859_1.getCharset();
		Assert.assertNotNull(charset);
		Assert.assertSame(charset, CHARSET_ISO_8559_1);

		charset = StringUtil.Charset.US_ASCII.getCharset();
		Assert.assertNotNull(charset);
		Assert.assertSame(charset, CHARSET_US_ASCII);

		charset = StringUtil.Charset.UTF_16.getCharset();
		Assert.assertNotNull(charset);
		Assert.assertSame(charset, CHARSET_UTF_16);

		charset = StringUtil.Charset.UTF_16_BE.getCharset();
		Assert.assertNotNull(charset);
		Assert.assertSame(charset, CHARSET_UTF_16_BE);
		
		charset = StringUtil.Charset.UTF_16_LE.getCharset();
		Assert.assertNotNull(charset);
		Assert.assertSame(charset, CHARSET_UTF_16_LE);
		
		charset = StringUtil.Charset.UTF_8.getCharset();
		Assert.assertNotNull(charset);
		Assert.assertSame(charset, CHARSET_UTF_8);
	}

	public void testRemoveAfter() {
		final String case0Search = "user@domain/resource";
		final String case0Find = "/";
		final String case0Result = StringUtil.removeAfter(case0Search, case0Find);
		final String case0Target = "user@domain";
		Assert.assertEquals(case0Result, case0Target);

		final String case1Search = "user@domain/resource";
		final String case1Find = "";
		final String case1Result = StringUtil.removeAfter(case1Search, case1Find);
		final String case1Target = case1Search;
		Assert.assertEquals(case1Result, case1Target);

		final String case2Search = "user@domain/resource";
		final String case2Find = null;
		final String case2Result = StringUtil.removeAfter(case2Search, case2Find);
		final String case2Target = case2Search;
		Assert.assertEquals(case2Result, case2Target);

		final String case3Search = "user@domain/resource";
		final String case3Find = "userf";
		final String case3Result = StringUtil.removeAfter(case3Search, case3Find);
		final String case3Target = case3Search;
		Assert.assertEquals(case3Result, case3Target);
	}

	protected void setUp() throws Exception {
		super.setUp();
		CHARSET_ISO_8559_1 = Charset.forName(StringUtil.Charset.ISO_8859_1.getCharsetName());
		CHARSET_US_ASCII = Charset.forName(StringUtil.Charset.US_ASCII.getCharsetName());
		CHARSET_UTF_16 = Charset.forName(StringUtil.Charset.UTF_16.getCharsetName());
		CHARSET_UTF_16_BE = Charset.forName(StringUtil.Charset.UTF_16_BE.getCharsetName());
		CHARSET_UTF_16_LE = Charset.forName(StringUtil.Charset.UTF_16_LE.getCharsetName());
		CHARSET_UTF_8 = Charset.forName(StringUtil.Charset.UTF_8.getCharsetName());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
