/*
 * Created On:  4-Jun-07 12:48:51 PM
 */
package com.thinkparity.codebase.model.queue.notification;

import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationHeader {

    public static final NotificationHeader valueOf(final String header) {
        if (null == header)
            throw new NullPointerException("Header is null.");
        final int indexOfColon = header.indexOf(':');
        if (-1 == indexOfColon)
            throw new IllegalArgumentException("Header contains no name-value separation.");
        final int indexOfSemiColon = header.indexOf(';', indexOfColon);
        if (-1 == indexOfSemiColon)
            throw new IllegalArgumentException("Header contains no value termination.");
        final String value = header.substring(indexOfColon + 1, indexOfSemiColon);
        final Type type = Type.valueOfType(header.substring(0, indexOfColon));
        return new NotificationHeader(type, value);
    }

    /** The header <code>String</code>. */
    private final String header;

    /** The header <code>Type</code>. */
    private final Type type;

    /** The header value <code>String</code>. */
    private final String value;

    /**
     * Create NotificationHeader.
     *
     */
    public NotificationHeader(final Type type, final String value) {
        super();
        this.header = MessageFormat.format("{0}:{1};", type.toString(), value);
        this.type = type;
        this.value = value;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (getClass() == obj.getClass())
            return ((NotificationHeader) obj).header.equals(header);
        return false;
    }

    /**
     * Obtain type.
     *
     * @return A <code>Type</code>.
     */
    public Type getType() {
        return type;
    }

    /**
     * Obtain value.
     *
     * @return A value <code>String</code>.
     */
    public String getValue() {
        return value;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return header.hashCode();
    }

    /**
     * Obtain the length of the header.
     * 
     * @return The header length <code>int</code>.
     */
    public int length() {
        return header.length();
    }

    /**
     * Obtain a string representation of the header.
     * 
     * @return A header <code>String</code>.
     */
    public String toHeader() {
        return header;
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return toHeader();
    }

    /**
     * <b>Title:</b>Notification Header Type<br>
     * <b>Description:</b><br>
     */
    public enum Type {

        SESSION_ID("Session-Id"),
        UNKNOWN("Unknown");

        /**
         * Obtain the value of the type.
         * 
         * @param type
         *            A type <code>String</code>.
         * @return A <code>Type</code>.
         */
        public static Type valueOfType(final String type) {
            final Type[] types = values();
            for (final Type t : types) {
                if (t.getType().equals(type)) {
                    return t;
                }
            }
            return Type.UNKNOWN;
        }

        /** The type <code>String</code>. */
        private final String type;

        /**
         * Create Type.
         * 
         * @param type
         *            A type name <code>String</code>.
         */
        private Type(final String type) {
            this.type = type;
        }

        /**
         * Obtain the name of the type.
         * 
         * @return The type name <code>String</code>.
         */
        public String getType() {
            return type;
        }

        /**
         * @see java.lang.Enum#toString()
         *
         */
        @Override
        public String toString() {
            return type;
        }
    }
}
