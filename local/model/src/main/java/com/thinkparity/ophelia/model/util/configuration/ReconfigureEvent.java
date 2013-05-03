/*
 * Created On:  9-Nov-07 3:52:49 PM
 */
package com.thinkparity.ophelia.model.util.configuration;

import java.util.Properties;

/**
 * <b>Title:</b>thinkParity Ophelia Model Configuration Update Event<br>
 * <b>Description:</b>An event fired by the configuration provider when its
 * configuration is updated.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            The properties configuration type.
 */
public final class ReconfigureEvent<T extends Properties> {

    /** The current configuration. */
    private final T current;

    /** The previous configuration. */
    private final T previous;

    /**
     * Create ConfigurationUpdateEvent.
     * 
     * @param previous
     *            A <code>T</code>.
     * @param current
     *            A <code>T</code>.
     */
    public ReconfigureEvent(final T previous, final T current) {
        super();
        this.previous = previous;
        this.current = current;
    }

    /**
     * Obtain the current configuration.
     *
     * @return A <code>T</code>.
     */
    public T getCurrent() {
        return current;
    }

    /**
     * Obtain the previous configuration.
     *
     * @return A <code>T</code>.
     */
    public T getPrevious() {
        return previous;
    }

    /**
     * Determine whether or not the named property is reconfigured. A property
     * is considered reconfigured if:
     * <ol>
     * <li> it existed in current and previous and the values are not equal</li>
     * <li>it exists in current and not in previous</li>
     * <li>it existed in previous and not in current</li>
     * </ol>
     * 
     * @param name
     *            A <code>String</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean isReconfigured(final String name) {
        if (current.containsKey(name) && previous.containsKey(name)) {
            final String currentValue = current.getProperty(name);
            final String previousValue = previous.getProperty(name);
            if (null == currentValue && null == previousValue) {
                return Boolean.FALSE;
            } else {
                /* the current or previous value is null; but not both */
                if (null == currentValue) {
                    return Boolean.TRUE;
                } else {
                    return currentValue.equals(previousValue);
                }
            }
        } else {
            /* the key exists in either current or previous but not both. */
            return Boolean.TRUE;
        }
    }
}
