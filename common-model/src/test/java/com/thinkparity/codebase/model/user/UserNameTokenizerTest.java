/*
 * Created On: Jun 14, 2006 10:31:45 AM
 * $Id$
 */
package com.thinkparity.codebase.model.user;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.model.ModelTestCase;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UserNameTokenizerTest extends ModelTestCase {

    private static final String NAME = "[LMODEL] [XMPP] [USER] [USER NAME TOKENIZER TEST]";

    private Map<String, Fixture> data;

    /** Create UserNameTokenizerTest. */
    public UserNameTokenizerTest() {
        super(NAME);
    }

    public void testUserTokenizer() {
        Fixture datum = data.get("GivenFamily");
        assertEquals(datum.eFamily, datum.eGiven, datum.eMiddle, datum.tokenizer);
        
        datum = data.get("GivenMiddleFamily");
        assertEquals(datum.eFamily, datum.eGiven, datum.eMiddle, datum.tokenizer);
    }

    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        data = new HashMap<String, Fixture>();
        data.put("GivenFamily", new Fixture("Family", "Given", null, new UserNameTokenizer("Given Family")));
        data.put("GivenMiddleFamily", new Fixture("Family", "Given", "Middle", new UserNameTokenizer("Given Middle Family")));
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;
        super.tearDown();
    }

    /**
     * @param datum
     */
    private void assertEquals(final String eFamily, final String eGiven,
            final String eMiddle, final UserNameTokenizer tokenizer) {
        assertEquals(NAME + "[FAMILY NAME DOES NOT MATCH EXPECTATION]", eFamily, tokenizer.getFamily());
        assertEquals(NAME + "[GIVEN NAME DOES NOT MATCH EXPECTATION]", eGiven, tokenizer.getGiven());
        assertEquals(NAME + "[MIDDLE NAME DOES NOT MATCH EXPECTATION]", eMiddle, tokenizer.getMiddle());
    }

    private class Fixture {
        private final String eFamily;
        private final String eGiven;
        private final String eMiddle;
        private final UserNameTokenizer tokenizer;
        private Fixture(final String eFamily, final String eGiven,
                final String eMiddle, final UserNameTokenizer tokenizer) {
            this.eFamily = eFamily;
            this.eGiven = eGiven;
            this.eMiddle = eMiddle;
            this.tokenizer = tokenizer;
        }
    }
}
