/*
 * Created On:  Nov 19, 2007 10:15:19 AM
 */
package com.thinkparity.ophelia.support.ui.window;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WindowFactory {
    
    /** A map of ids to their classes. */
    private static final Map<String, Class<? extends Window>> ID_CLASS_MAP;

    /** A message format pattern for the illegal state exception. */
    private static final String ILLEGAL_STATE_INSTANTIATED_PATTERN;

    /** A singleton window factory instance. */
    private static WindowFactory SINGLETON;

    static {
        ID_CLASS_MAP = new HashMap<String, Class<? extends Window>>();
        ID_CLASS_MAP.put("/main/main", com.thinkparity.ophelia.support.ui.window.main.MainWindow.class);

        ILLEGAL_STATE_INSTANTIATED_PATTERN = "Window \"{0}\" has already been instantiated.";

        SINGLETON = new WindowFactory();
    }

    /**
     * Obtain a window factory instance.
     * 
     * @return A <code>WindowFactory</code>.
     */
    public static WindowFactory getInstance() {
        return SINGLETON;
    }

    /**
     * Instantiate an illegal state exception for an already instantiated
     * window.
     * 
     * @param id
     *            A <code>String</code>.
     * @return An <code>IllegalStateException</code>.
     */
    private static IllegalStateException newIllegalStateInstantiated(final String id) {
        return new IllegalStateException(MessageFormat.format(
                ILLEGAL_STATE_INSTANTIATED_PATTERN, id));
    }

    /** A window registry. */
    private final WindowRegistry registry;

    /**
     * Create WindowFactory.
     *
     */
    private WindowFactory() {
        super();
        this.registry = new WindowRegistry();
    }

    /**
     * Instantiate a window.
     * 
     * @param <T>
     *            A window <code>Type</code>.
     * @param id
     *            A <code>String</code>.
     * @return A <code>T</code>.
     */
    public Window newWindow(final String id) {
        if (registry.isRegistered(id)) {
            throw newIllegalStateInstantiated(id);
        }
        return newWindow(ID_CLASS_MAP.get(id));
    }
    
    /**
     * Instantiate a window.
     * 
     * @param <T>
     *            A window <code>Type</code>.
     * @param windowClass
     *            A <code>Class<T></code>.
     * @return A <code>T</code>.
     */
    private Window newWindow(final Class<?> windowClass) {
        try {
            final Window window = (Window) windowClass.newInstance();
            registry.register(window.getId(), window);
            return window;
        } catch (final IllegalAccessException iax) {
            throw new NoSuchWindowException(windowClass);
        } catch (final InstantiationException ix) {
            throw new NoSuchWindowException(windowClass);
        }
    }
}
