/*
 * Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * Assert
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Assert {

    /** The assertion message to use when the message provided is null. */
    private static final String NULL_MESSAGE = "";

	/**
	 * Assert that the object reference provided is null. If it is not, throw a
	 * NotNullPointerAssertion.
	 * 
	 * @param message
	 *            The assertion message.
	 * @param objectReference
	 *            The object reference to test.
	 */
	public static void assertIsNull(final Object message,
			final Object objectReference) {
		if(null != objectReference)
			throw new NotNullPointerAssertion(
                    null == message ? NULL_MESSAGE : message.toString());
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
	 * Create a new Assert [Singleton]
	 */
	private Assert() { super(); }
}
