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
public final class StringConstraintTest extends CodebaseTestCase {

    /**
     * Create StringConstraintTest.
     *
     */
    public StringConstraintTest() {
        super("String constraint");
    }

    /**
     * Test invalid domain names.
     * 
     */
    public void testInvalid() {
        final StringConstraint constraint = new StringConstraint();
        constraint.setMaxLength(10);
        constraint.setMinLength(9);
        constraint.setName("Test invalid");
        constraint.setNullable(Boolean.FALSE);

        boolean didThrow = false;
        try {
            constraint.validate(null);
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Nullable constraint not working.", didThrow);

        didThrow = false;
        try {
            constraint.validate("01234567");
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Min length constraint not working.", didThrow);

        didThrow = false;
        try {
            constraint.validate("01234567890");
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Max length constraint not working.", didThrow);

        constraint.setMinLength(10);
        constraint.setMaxLength(10);
        didThrow = false;
        try {
            constraint.validate("01234567");
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Min length constraint not working.", didThrow);

        didThrow = false;
        try {
            constraint.validate("01234567890");
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Max length constraint not working.", didThrow);
    }

    /**
     * Test setting the min/max length.
     * 
     */
    public void testSetMinMax() {
        StringConstraint constraint;
        boolean didThrow;

        didThrow = false;
        constraint = new StringConstraint();
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);
        constraint.setMaxLength(1);
        try {
            constraint.setMinLength(2);
        } catch (final IllegalArgumentException iax) {
            didThrow = true;
        }
        assertTrue("Set min length validation not working.", didThrow);

        didThrow = false;
        constraint = new StringConstraint();
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);
        constraint.setMinLength(2);
        try {
            constraint.setMaxLength(1);
        } catch (final IllegalArgumentException iax) {
            didThrow = true;
        }
        assertTrue("Set max length validation not working.", didThrow);

        didThrow = false;
        constraint = new StringConstraint();
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);
        try {
            constraint.setMaxLength(null);
        } catch (final IllegalArgumentException iax) {
            didThrow = true;
        }
        assertTrue("Set max length validation not working.", didThrow);

        didThrow = false;
        constraint = new StringConstraint();
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);
        try {
            constraint.setMinLength(null);
        } catch (final IllegalArgumentException iax) {
            didThrow = true;
        }
        assertTrue("Set min length validation not working.", didThrow);
    }

    /**
     * Test valid strings.
     * 
     */
    public void testValid() {
        final StringConstraint constraint = new StringConstraint();
        constraint.setMaxLength(10);
        constraint.setMinLength(9);
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);

        constraint.validate("012345678");
        constraint.validate("0123456789");
        constraint.setNullable(Boolean.TRUE);
        constraint.validate(null);
        constraint.setMaxLength(10);
        constraint.setMinLength(10);
        constraint.validate("0123456789");
    }
}
