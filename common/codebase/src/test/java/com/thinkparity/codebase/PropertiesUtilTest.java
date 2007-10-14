/*
 * Dec 10, 2005
 */
package com.thinkparity.codebase;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PropertiesUtilTest extends CodebaseTestCase {

	private class Fixture {
		private final StringBuffer buffer;
		private final String comments;
		private final Properties properties;
		private Fixture(final StringBuffer buffer, final String comments,
				final Properties properties) {
			this.buffer = buffer;
			this.comments = comments;
			this.properties = properties;
		}
	}

	private Vector<Fixture> data;

	/**
	 * Create a PropertiesUtilTest.
	 */
	public PropertiesUtilTest() { super("PropertiesUtilTest"); }

	public void testPrint() {
		for(Fixture datum : data) {
			PropertiesUtil.print(datum.buffer, datum.comments, datum.properties);

			final String bufferString = datum.buffer.toString();
			PropertiesUtilTest.assertNotNull(bufferString);
			PropertiesUtilTest.assertTrue(bufferString.length() > 0);
			PropertiesUtilTest.assertTrue(bufferString.startsWith("# " + datum.comments));
			final Set<Object> keys = datum.properties.keySet();
			String keyColonValue;
			for(Object key : keys) {
				keyColonValue = new StringBuffer(key.toString())
					.append(Separator.FullColon)
					.append(datum.properties.getProperty(key.toString()))
					.toString();

				PropertiesUtilTest.assertTrue(bufferString.contains(keyColonValue));
			}
		}
	}

	public void testReplace() {
	    final Map<String, String> expected = new HashMap<String, String>();
	    expected.put("one", System.getProperty("java.home") + System.getProperty("file.separator") + "bin");
        expected.put("two", "${java.fhome}" + System.getProperty("file.separator") + "bin");
        expected.put("three", "${java.fhome}${file.fseparator}bin");
        expected.put("four", "testReplace");
        expected.put("five", "test${Replace");
        expected.put("six", "t}est${Replace");
        expected.put("seven", "test${}Replace");
        expected.put("eight", "testReplace");
        expected.put("nine", "testReplace${");
        expected.put("ten", "testReplace" + expected.get("one"));
        expected.put("twelve", "testReplace");
        expected.put("eleven", "testReplace" + expected.get("twelve"));

	    final Properties properties = new Properties();
	    properties.setProperty("one", "${java.home}${file.separator}bin");
        properties.setProperty("two", "${java.fhome}${file.separator}bin");
        properties.setProperty("three", "${java.fhome}${file.fseparator}bin");
        properties.setProperty("four", "testReplace");
        properties.setProperty("five", "test${Replace");
        properties.setProperty("six", "t}est${Replace");
        properties.setProperty("seven", "test${}Replace");
        properties.setProperty("eight", "test${x}");
        properties.setProperty("nine", "testReplace${");
        properties.setProperty("ten", "testReplace${one}");
        properties.setProperty("eleven", "testReplace${twelve}");
        properties.setProperty("twelve", "testReplace");

        final Properties replace = new Properties();
        replace.setProperty("x", "Replace");

        PropertiesUtil.replace(properties, properties);
	    PropertiesUtil.replace(properties, replace);
	    PropertiesUtil.replace(properties, System.getProperties());

	    for (final Entry<String, String> expectedEntry : expected.entrySet()) {
	        assertTrue("Properties does not contain expected entry " + expectedEntry.getKey(),
	                properties.containsKey(expectedEntry.getKey()));
	        assertEquals("Properties value does not match expected entry " + expectedEntry.getKey(),
	                expectedEntry.getValue(), properties.get(expectedEntry.getKey()));
	    }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(1);

		data.add(new Fixture(
				new StringBuffer(),
				"--- Test Property Printer ---",
				System.getProperties()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
