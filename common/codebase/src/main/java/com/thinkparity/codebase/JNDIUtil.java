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
 * @author raymond@raykroeker.com
 * @version 1.0.0
 */
public abstract class JNDIUtil {

    /**
     * Retrieves the named object.
     * 
     * @param name
     *      the name of the object to look up
     * @return  the object bound to <tt>name</tt>
     * @throws  NamingException if a naming exception is encountered
     */
	public static Object lookup(final String name)
            throws NamingException {
		final Context c = new InitialContext();
		return c.lookup(name);
	}

	/**
     * Binds a name to an object, overwriting any existing binding.
     *
     * @param name
     *      the name to bind; may not be empty
     * @param obj
     *      the object to bind; possibly null
     * @throws  javax.naming.directory.InvalidAttributesException
     *      if object did not supply all mandatory attributes
     * @throws  NamingException if a naming exception is encountered
     */
    public static void rebind(final String name, final Object obj)
            throws NamingException {
        final Context c = new InitialContext();
        c.rebind(name, obj);
    }

    /**
     * Create JNDIUtil.
     *
     */
	private JNDIUtil() {
        super();
	}
}
