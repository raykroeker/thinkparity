/*
 * Created On:  11-Nov-06 5:35:12 PM
 */
package com.thinkparity.codebase.model.util.xstream;

import java.beans.PropertyDescriptor;

import com.thinkparity.codebase.beans.BeanUtils;

import com.thinkparity.codebase.model.ModelTestCase;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class XStreamTestCase extends ModelTestCase {

    /**
     * Create XStreamTestCase.
     *
     * @param name
     */
    protected XStreamTestCase(final String name) {
        super(name);
    }

    /**
     * @see com.thinkparity.codebase.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.codebase.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected static void assertEquals(final XMPPEvent expected,
            final XMPPEvent actual) {
        final BeanUtils expectedUtils = new BeanUtils(expected);
        final BeanUtils actualUtils = new BeanUtils(expected);
        final PropertyDescriptor[] expectedPDs = expectedUtils.getPropertyDescriptors();
        Object expectedPValue, actualPValue;
        for (final PropertyDescriptor expectedPD : expectedPDs) {
            expectedPValue = expectedUtils.readProperty(expectedPD);
            actualPValue = actualUtils.readProperty(expectedPD);
            assertEquals("XMPP event property " + expectedPD.getName()
                    + " does not match expectation.", expectedPValue,
                    actualPValue);
        }
    }

    protected static abstract class Fixture extends ModelTestCase.Fixture {}
}
