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
public final class DomainNameConstraintTest extends CodebaseTestCase {

    /**
     * Create DomainNameConstraintTest.
     *
     */
    public DomainNameConstraintTest() {
        super("Domain name constraint");
    }

    /**
     * Test invalid domain names.
     * 
     */
    public void testInvalid() {
        final DomainNameConstraint constraint = new DomainNameConstraint();
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
            constraint.validate("*.domain.com");
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Character validation constraint not working.", didThrow);

        didThrow = false;
        try {
            constraint.validate("*.com");
        } catch (final IllegalValueException ivx) {
            didThrow = true;
        }
        assertTrue("Character validation constraint not working.", didThrow);
    }

    /**
     * Test setting the min/max length.
     * 
     */
    public void testSetMinMax() {
        final DomainNameConstraint constraint = new DomainNameConstraint();
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);

        boolean didThrow = false;
        try {
            constraint.setMaxLength(1);
        } catch (final UnsupportedOperationException uox) {
            didThrow = true;
        }
        assertTrue("Constriant allowed set max length.", didThrow);

        didThrow = false;
        try {
            constraint.setMinLength(1);
        } catch (final UnsupportedOperationException uox) {
            didThrow = true;
        }
        assertTrue("Constriant allowed set max length.", didThrow);
    }
    /**
     * Test valid domain names.
     * 
     */
    public void testValid() {
        final DomainNameConstraint constraint = new DomainNameConstraint();
        constraint.setName("Test valid");
        constraint.setNullable(Boolean.FALSE);

        constraint.validate("host.domain.com");
        constraint.validate("domain.com");
        constraint.validate("host-0.domain.com");
        constraint.validate("domain-0.com");
        constraint.setNullable(Boolean.TRUE);
        constraint.validate(null);
    }
}
