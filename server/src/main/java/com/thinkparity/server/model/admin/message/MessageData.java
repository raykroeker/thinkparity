/*
 * Created On:  23-Nov-07 10:28:42 AM
 */
package com.thinkparity.desdemona.model.admin.message;

/**
 * <b>Title:</b>thinkParity Desdemona Model Admin Message Data<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MessageData<T extends Object> {

    /** A name. */
    private final String name;

    /** A value. */
    private final T value;

    /**
     * Create MessageData.
     *
     */
    MessageData(final String name, final T value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Obtain the name.
     *
     * @return A <code>String</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain the string value.
     * 
     * @return A <code>String</code>.
     */
    public String getStringValue() {
        return null == value ? null : String.valueOf(value);
    }

    /**
     * Obtain the value.
     *
     * @return A <code>T</code>.
     */
    public T getValue() {
        return value;
    }
}
