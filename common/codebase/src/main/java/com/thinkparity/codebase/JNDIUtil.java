/*
 * Nov 10, 2003
 */
package com.thinkparity.codebase;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <b>Title:</b>  JNDIUtil
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class JNDIUtil {

	/**
	 * Create a new JNDIUtil [Singleton]
	 */
	private JNDIUtil() {super();}

	/**
	 * Lookup a JNDI reference in the tree.
	 * @param reference <code>java.lang.String</code>
	 * @return <code>java.lang.Object</code>
	 * @throws NamingException
	 */
	public static synchronized Object lookup(String reference)
		throws NamingException {
		Context ic = new InitialContext();
		return ic.lookup(reference);
	}
	
	/**
	 * Lookup a JNDI reference in the tree.
	 * @param reference <code>java.lang.StringBuffer</code>
	 * @return <code>java.lang.Object</code>
	 * @throws NamingException
	 */
	public static synchronized Object lookup(StringBuffer reference)
		throws NamingException {
		return lookup(null == reference ? null : reference.toString());
	}

}
