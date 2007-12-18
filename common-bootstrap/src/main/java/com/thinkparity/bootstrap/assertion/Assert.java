/*
 * Apr 27, 2005
 */
package com.thinkparity.bootstrap.assertion;

import java.text.MessageFormat;

/**
 * Assert
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Assert {

    /** The assertion message to use when the message provided is null. */
    private static final String NULL_MESSAGE = "";

    /**
     * Asssert that the index provided is within bounds of the given array.
     * 
     * @param message
     *            An assertion message.
     * @param index
     *            An index.
     * @param array
     *            An array.
     */
    public static <T> void assertInBounds(final Object message,
            final Integer index, final T[] array) {
        if (index < 0 || index > array.length - 1) {
            throw new IndexOutOfBoundsAssertion(message.toString());
        }
    }

	/**
	 * Assert that the object reference provided is null. If it is not, throw a
	 * NotNullPointerAssertion.
	 * 
	 * @param message
	 *            The assertion message.
	 * @param objectReference
	 *            The object reference to test.
	 */
	public static void assertIsNull(final String message,
			final Object objectReference) {
		if(null != objectReference)
			throw new NotNullPointerAssertion(
                    null == message ? NULL_MESSAGE : message.toString());
	}

    public static void assertIsNull(final Object objectReference,
            final String assertionPattern, final Object... assertionArguments) {
        if (null != objectReference) {
            throw new NotNullPointerAssertion(
                createMessage(assertionPattern, assertionArguments));
        }

    }

    /**
	 * Assert that the object reference provided is not null. If it is, throw a
	 * <code>org.kcs.projectmanager.client.util.NullPointerAssertion</code>.
	 * 
	 * @param message
	 *            A message object.
	 * @param objectReference
	 *            <code>java.lang.Object</code>
	 */
	public static void assertNotNull(final Object message,
			final Object objectReference) {
		if(null == objectReference)
			throw new NullPointerAssertion(
                    null == message ? NULL_MESSAGE : message.toString());
	}

    /**
	 * Assert that the object references provided are not null. If they are,
	 * throw a {@link NullPointerAssertion}.
	 * 
	 * @param message
	 *            An assertion message.
	 * @param objectReferences
	 *            <code>java.lang.Object</code>
	 */
	public static void assertNotNull(final Object message,
			final Object[] objectReferences) {
		Assert.assertNotNull(message, (Object) objectReferences);
		for(int i = 0; i < objectReferences.length; i++)
			Assert.assertNotNull(message, objectReferences[i]);
	}

    /**
     * Assert that the reference is not null. If it is null throw
     * {@link NullPointerAssertion}.
     * 
     * @param reference
     *            An object reference.
     * @param pattern
     *            An assertion message pattern.
     * @param arguments
     *            Assertion message arguments.
     */
	public static void assertNotNull(final Object reference,
            final String pattern, final Object... arguments) {
        if (null == reference) {
            throw new NullPointerAssertion(createMessage(pattern, arguments));
        }
    }

	/**
     * Assert that a boolean expression is false.
     * 
     * @param falseExpression
     *            A false expression.
     * @param pattern
     *            An assertion message pattern.
     * @param arguments
     *            Assertion message arguments.
     */
    public static void assertNotTrue(final Boolean falseExpression,
            final String pattern, final Object... arguments) {
        if (Boolean.FALSE != falseExpression) {
            throw new TrueAssertion(createMessage(pattern, arguments));
        }
    }

    /**
	 * Assert that the boolean expression provided is false. If it is not, throw
	 * a <code>org.kcs.projectmanager.client.util.TrueAssertion</code>.
	 * 
	 * @param message
	 *            An assertion message.
	 * @param falseExpression
	 *            <code>java.lang.Boolean</code>
	 */
	public static void assertNotTrue(final Object message, final Boolean falseExpression) {
		if(Boolean.FALSE != falseExpression)
			throw new TrueAssertion(
                    null == message ? NULL_MESSAGE : message.toString());
	}

	/**
     * Assert that the current api method has not yet been implemented.
     * 
     * @param message
     *            An assertion message.
     */
	public static void assertNotYetImplemented(final Object message) {
		throw Assert.createNotYetImplemented(message);
	}

	/**
	 * Assert that the instance of an object provided is of the type provided.
	 * 
	 * @param message
	 *            The assertion message.
	 * @param type
	 *            The target type.
	 * @param instance
	 *            The questionable instance.
	 * @throws NotOfTypeAssertion
	 *             If the instance is not of the target type.
	 */
	public static void assertOfType(final Object message, final Class<?> type,
			final Object instance) {
		if(!type.isAssignableFrom(instance.getClass()))
			throw new NotOfTypeAssertion(
                    null == message ? NULL_MESSAGE : message.toString(),
                    type, instance);
	}

	/**
     * Assert that the expression provided is true.
     * 
     * @param expression
     *            A boolean expression.
     * @param pattern
     *            The assertion message pattern.
     * @param arguments
     *            The assertion message arguments.
     */
    public static void assertTrue(final Boolean expression,
            final String pattern, final Object... arguments) {
        if (Boolean.TRUE != expression) {
            throw new NotTrueAssertion(createMessage(pattern, arguments));
        }
    }

    /**
	 * Assert that the expression provided is true.
	 * 
	 * @param message
	 *            The assertion message.
	 * @param expression
	 *            A boolean expression.
	 * @throws NotTrueAssertion
	 *             If the expression does not evaluate to true.
	 */
	public static void assertTrue(final Object message, final Boolean expression) {
		if(Boolean.TRUE != expression)
			throw new NotTrueAssertion(
                    null == message ? NULL_MESSAGE : message.toString());
	}

    /**
     * Assert that an area of code is unreachable. This is achived by throwing
     * an <code>UnreachableCodeAssertion</code>.
     * 
     * @param message
     *            An assertion message.
     */
    public static void assertUnreachable(final Object message) {
        throw Assert.createUnreachable(message);
    }

    /**
     * Assert that an area of code is unreachable. This is achived by throwing
     * an <code>UnreachableCodeAssertion</code>.
     * 
     * @param message
     *            An assertion message.
     */
    public static void assertUnreachable(final String assertionMessage,
            final Object... assertionArguments) {
        throw Assert.createUnreachable(createMessage(assertionMessage,
                assertionArguments));
    }

	/**
     * Create a not yet implemented code assertion.
     * 
     * @param message
     *            An assertion message.
     * @return <code>NotYetImplementedAssertion</code>
     */
	public static NotYetImplementedAssertion createNotYetImplemented(
			final Object message) {
		return new NotYetImplementedAssertion(
                null == message ? NULL_MESSAGE : message.toString());
	}

	/**
     * Create an unreachable code assertion.
     * 
     * @param message
     *            An assertion message.
     * @return <code>UnreachableCodeAssertion</code>
     */
	public static UnreachableCodeAssertion createUnreachable(
			final Object message) {
		return new UnreachableCodeAssertion(
                null == message ? NULL_MESSAGE : message.toString());
	}

    /**
     * Create an unreachable code assertion.
     * 
     * @param message
     *            An assertion message.
     * @return <code>UnreachableCodeAssertion</code>
     */
    public static UnreachableCodeAssertion createUnreachable(
            final String pattern, final Object... arguments) {
        return new UnreachableCodeAssertion(createMessage(pattern, arguments));
    }

	/**
     * Create a formatted message. If the pattern is null; or if the message
     * cannot be formatted; a standard message is returned.
     * 
     * @param pattern
     *            A message pattern.
     * @param arguments
     *            Message arguments.
     * @return A formatted message.
     */
    private static String createMessage(final String pattern,
            final Object... arguments) {
        if (null == pattern) {
            return NULL_MESSAGE;
        } else {
            try {
                return MessageFormat.format(pattern, arguments);
            } catch (final IllegalArgumentException iax) {
                return NULL_MESSAGE;
            }
        }
    }

	/**
	 * Create a new Assert [Singleton]
	 */
	private Assert() { super(); }
}
