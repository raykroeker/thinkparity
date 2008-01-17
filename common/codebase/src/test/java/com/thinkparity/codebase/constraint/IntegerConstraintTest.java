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
public final class IntegerConstraintTest extends CodebaseTestCase {

    /**
     * Create IntegerConstraintTest.
     *
     */
    public IntegerConstraintTest() {
        super("Integer constraint");
    }

    /**
     * Test setting min/max.
     * 
     */
    public void testSetMinMax() {
        final IntegerConstraint constraint = new IntegerConstraint();
        constraint.setName("Test set min/max");

        constraint.setMaxValue(100);
        constraint.setMinValue(99);

        constraint.setMaxValue(100);
        constraint.setMinValue(100);

        boolean didThrow = false;
        constraint.setMaxValue(100);
        try {
            constraint.setMinValue(101);
        } catch (final IllegalArgumentException iax) {
            didThrow = true;
        }
        assertTrue("Did not throw for min>max argument.", didThrow);

        didThrow = false;
        constraint.setMinValue(100);
        try {
            constraint.setMaxValue(99);
        } catch (final IllegalArgumentException iax) {
            didThrow = true;
        }
        assertTrue("Did not throw for min>max argument.", didThrow);
    }

    /**
     * Test valid integers.
     * 
     */
    public void testValid() {
        final IntegerConstraint constraint = new IntegerConstraint();
        constraint.setMaxValue(100);
        constraint.setMinValue(99);
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);
        
        constraint.validate(99);
        constraint.validate(100);
        constraint.setNullable(Boolean.TRUE);
        constraint.validate(null);

        constraint.setMaxValue(100);
        constraint.setMinValue(100);
        constraint.validate(100);
    }

    /**
     * Test invalid integers.
     * 
     */
    public void testInvalid() {
        final IntegerConstraint constraint = new IntegerConstraint();
        constraint.setMaxValue(100);
        constraint.setMinValue(99);
        constraint.setName("Test invalid");
        constraint.setNullable(Boolean.FALSE);

        boolean didThrow = false;
        try {
            constraint.validate(98);
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Did not throw validation error.", didThrow);

        didThrow = false;
        try {
            constraint.validate(101);
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

        constraint.setMaxValue(100);
        constraint.setMinValue(100);
        didThrow = false;
        try {
            constraint.validate(99);
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Did not throw validation error.", didThrow);

        didThrow = false;
        try {
            constraint.validate(101);
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Did not throw validation error.", didThrow);
    }
}
