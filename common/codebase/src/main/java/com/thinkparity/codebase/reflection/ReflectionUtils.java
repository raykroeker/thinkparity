/*
 * Created On: Oct 15, 2006 1:07:01 PM
 */
package com.thinkparity.codebase.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReflectionUtils {

    private static final ReflectionUtils SINGLETON;

    static { SINGLETON = new ReflectionUtils(); }

    public static Object newDefaultInstance(final Class clasz)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        return SINGLETON.doCreateNewDefaultInstance(clasz);
    }

    /** Create ReflectionUtils. */
    private ReflectionUtils() {
        super();
    }

    private Object doCreateNewDefaultInstance(final Class clasz)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        final Constructor constructor = clasz.getConstructor(new Class[] {});
        return constructor.newInstance(new Object[] {});
    }
}
