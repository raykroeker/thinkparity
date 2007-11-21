/*
 * Created On:  Nov 19, 2007 9:24:30 AM
 */
package com.thinkparity.ophelia.support.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <b>Title:</b>thinkParity Ophelia Support UI Action Input<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Input implements Cloneable {

    /** An empty input. */
    private static final Input EMPTY_INPUT;

    static {
        EMPTY_INPUT = new EmptyInput();
    }

    /**
     * Obtain an empty input.
     * 
     * @return An <code>Input</code>.
     */
    public static Input emptyInput() {
        return EMPTY_INPUT;
    }

    /** An input map. */
    private final Map<Enum<?>, Object> input;

    /**
     * Create Input.
     *
     */
    public Input() {
        this(7);
    }

    /**
     * Create Input.
     * 
     * @param size
     *            An <code>int</code>.
     */
    public Input(final int size) {
        super();
        this.input = new HashMap<Enum<?>, Object>(size, 0.75F);
    }

    /**
     * @see java.lang.Object#clone()
     * 
     */
    @Override
    public Object clone() {
        final Input clone = new Input(input.size());
        for (final Entry<Enum<?>, Object> entry : input.entrySet()) {
            clone.set(entry.getKey(), entry.getValue());
        }
        return clone;
    }

    /**
     * Obtain the input.
     * 
     * @param key
     *            An <code>Enum<?></code>.
     * @return An <code>Object</code>.
     */
    public Object get(final Enum<?> key) {
        return input.get(key);
    }

    /**
     * Remove the input and return the previous value.
     * 
     * @param key
     *            An <code>Enum<?></code>.
     * @return An <code>Object</code>.
     */
    public Object remove(final Enum<?> key) {
        return input.remove(key);
    }

    /**
     * Set the input and return the previous value.
     * 
     * @param key
     *            An <code>Enum<?></code>.
     * @param value
     *            An <code>Object</code>.
     * @return An <code>Object</code>.
     */
    public Object set(final Enum<?> key, final Object value) {
        return input.put(key, value);
    }

    /** <b>Title:</b>Empty Input<br> */
    private static class EmptyInput extends Input {

        /**
         * @see com.thinkparity.ophelia.support.ui.action.Input#remove(java.lang.Enum)
         *
         */
        @Override
        public Object remove(final Enum<?> key) {
            throw new UnsupportedOperationException();
        }

        /**
         * @see com.thinkparity.ophelia.support.ui.action.Input#set(java.lang.Enum, java.lang.Object)
         *
         */
        @Override
        public Object set(final Enum<?> key, final Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
