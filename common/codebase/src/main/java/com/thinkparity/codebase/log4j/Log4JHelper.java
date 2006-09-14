/*
 * Created On: Aug 8, 2006 10:51:16 AM
 */
package com.thinkparity.codebase.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Log4JHelper {

    /** A singleton instance. */
    private static final Log4JHelper SINGLETON;

    static { SINGLETON = new Log4JHelper(); }

    /**
     * Render an object for a logger.
     * 
     * @param logger
     *            An apache logger.
     * @param o
     *            An object.
     * @return A rendered version of an object.
     */
    public static Object render(final Logger logger, final Object object) {
        return SINGLETON.doRender(logger, object);
    }

    /**
     * Render an object for a logger.
     * 
     * @param logger
     *            An apache logger.
     * @param o
     *            An object.
     * @return A rendered version of an object.
     */
    public static Object[] render(final Logger logger, final Object...objects) {
        return SINGLETON.doRender(logger, objects);
    }

    /** Create Log4JHelper. */
    private Log4JHelper() { super(); }

    /**
     * Render an object for a logger.
     * 
     * @param logger
     *            An apache logger.
     * @param o
     *            An object.
     * @return A rendered version of an object.
     */
    private Object doRender(final Logger logger, final Object o) {
        final LoggerRepository loggerRepository = logger.getLoggerRepository();
        if(loggerRepository instanceof RendererSupport) {
            return ((RendererSupport) loggerRepository).getRendererMap().findAndRender(o);
        }
        else { return o.toString(); }
    }


    /**
     * Render an object for a logger.
     * 
     * @param logger
     *            An apache logger.
     * @param o
     *            An object.
     * @return A rendered version of an object.
     */
    private Object[] doRender(final Logger logger, final Object... objects) {
        final Object[] rendered = new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            rendered[i] = doRender(logger, objects[i]);
        }
        return rendered;
    }
}
