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

	/**
	 * Assert that the boolean expression provided is true. If it is not, throw
	 * a <code>org.kcs.projectmanager.client.util.NotTrueAssertion</code>.
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param trueExpression
	 *            <code>java.lang.Boolean</code>
	 */
	public static void assertTrue(final String message, final Boolean trueExpression) {
		if(Boolean.TRUE != trueExpression)
			throw new NotTrueAssertion(message);
	}

	/**
	 * Assert that the boolean expression provided is false. If it is not, throw
	 * a <code>org.kcs.projectmanager.client.util.TrueAssertion</code>.
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param falseExpression
	 *            <code>java.lang.Boolean</code>
	 */
	public static void assertNotTrue(final String message, final Boolean falseExpression) {
		if(Boolean.FALSE != falseExpression)
			throw new TrueAssertion(message);
	}

	/**
	 * Assert that the object reference provided is not null. If it is, throw a
	 * <code>org.kcs.projectmanager.client.util.NullPointerAssertion</code>.
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param objectReference
	 *            <code>java.lang.Object</code>
	 */
	public static void assertNotNull(final String message,
			final Object objectReference) {
		if(null == objectReference)
			throw new NullPointerAssertion(message);
	}

	/**
	 * Assert that the object references provided are not null. If they are,
	 * throw a
	 * <code>org.kcs.projectmanager.client.util.NullPointerAssertion</code>.
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param objectReferences
	 *            <code>java.lang.Object</code>
	 */
	public static void assertNotNull(final String message,
			final Object[] objectReferences) {
		Assert.assertNotNull(message, (Object) objectReferences);
		for(int i = 0; i < objectReferences.length; i++)
			Assert.assertNotNull(message, objectReferences[i]);
	}

	/**
	 * Assert that the current api method has not yet been implemented.
	 * @param message <code>java.lang.String</code>
	 */
	public static void assertNotYetImplemented(final String message) {
		throw Assert.createNotYetImplemented(message);
	}

	/**
	 * Create a not yet implemented code assertion.
	 * @param message <code>java.lang.String</code>
	 * @return <code>NotYetImplementedAssertion</code>
	 */
	public static NotYetImplementedAssertion createNotYetImplemented(
			final String message) {
		return new NotYetImplementedAssertion(message);
	}

	/**
	 * Create an unreachable code assertion.
	 * @param message <code>java.lang.String</code>
	 * @return <code>UnreachableCodeAssertion</code>
	 */
	public static UnreachableCodeAssertion createUnreachable(
			final String message) {
		return new UnreachableCodeAssertion(message);
	}

	/**
	 * Assert that an area of code is unreachable. This is achived by throwing
	 * an <code>UnreachableCodeAssertion</code>.
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 */
	public static void assertUnreachable(final String message) {
		throw Assert.createUnreachable(message);
	}

	/**
	 * Create a new Assert [Singleton]
	 */
	private Assert() { super(); }
}
