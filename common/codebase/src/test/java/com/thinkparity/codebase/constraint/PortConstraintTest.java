/*
 * Created On:  22-Dec-07 4:45:44 PM
 */
package com.thinkparity.codebase.constraint;

import com.thinkparity.codebase.CodebaseTestCase;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PortConstraintTest extends CodebaseTestCase {

    /**
     * Create PortConstraintTest.
     *
     */
    public PortConstraintTest() {
        super("Port constraint");
    }

    /**
     * Test invalid domain names.
     * 
     */
    public void testInvalid() {
        final PortConstraint constraint = new PortConstraint();
        constraint.setName("Port");
        constraint.setNullable(Boolean.FALSE);

        boolean didThrow = false;
        try {
            constraint.validate(0);
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Did not throw validation error.", didThrow);
        didThrow = false;
        try {
            constraint.validate(65536);
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Did not throw validation error.", didThrow);

        didThrow = false;
        try {
            constraint.validate(null);
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Did not throw validation error.", didThrow);
    }

    /**
     * Test setting the min/max values.
     * 
     */
    public void testSetMinMax() {
        final PortConstraint constraint = new PortConstraint();
        constraint.setName("Port");
        constraint.setNullable(Boolean.FALSE);

        boolean didThrow = false;
        try {
            constraint.setMaxValue(0);
        } catch (final UnsupportedOperationException uox) {
            didThrow = true;
        }
        assertTrue("Port constraint should not allow set max value.", didThrow);

        didThrow = false;
        try {
            constraint.setMinValue(0);
        } catch (final UnsupportedOperationException uox) {
            didThrow = true;
        }
        assertTrue("Port constraint should not allow set max value.", didThrow);
    }

    /**
     * Test valid ports.
     * 
     */
    public void testValid() {
        final PortConstraint constraint = new PortConstraint();
        constraint.setName("Port");
        constraint.setNullable(Boolean.FALSE);

        for (int i = 1; i < 65536; i++) {
            constraint.validate(i);
        }

        constraint.setNullable(Boolean.TRUE);
        constraint.validate(null);
    }
}
