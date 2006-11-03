package com.thinkparity.codebase.model.stream;

import java.text.MessageFormat;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamHeader {

    /**
     * Returns a stream header object holding the name and value extracted from
     * the specified header.
     * 
     * @param header
     *            A header <code>String</code>.
     * @return A <code>StreamHeader</code>.
     */
    public static final StreamHeader valueOf(final String header) {
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
        return new StreamHeader(type, value);
    }

    /** The header <code>String</code>. */
    private final String header;

    /** The header <code>Type</code>. */
    private final Type type;

    /** The header value. */
    private final String value;

    /**
     * Create StreamHeader.
     * 
     * @param type
     *            A header <code>Type</code>.
     * @param value
     *            A header value <code>String</code>.
     */
    public StreamHeader(final Type type, final String value) {
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
        if (obj instanceof StreamHeader) {
            return ((StreamHeader) obj).header.equals(header);
        } else {
            return false;
        }
    }

    public final Type getType() {
        return type;
    }

    /**
     * Obtain the header value.
     * 
     * @return The header value <code>String</code>.
     */
    public final String getValue() {
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
    public final int length() {
        return header.length();
    }

    /**
     * Obtain a string representation of the header.
     * 
     * @return A header <code>String</code>.
     */
    public final String toHeader() {
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
     * A stream header enumerated type.
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    public enum Type {

        SESSION_ID("Session-Id"),
        SESSION_TYPE("Session-Type"),
        STREAM_ID("Stream-Id"),
        STREAM_SIZE("Stream-Size"),
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
         * @param name
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