/*
 * Created On: Jan 10, 2006
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thinkparity.codebase.assertion.Assert;

/**
 * Data is a wrapper around a set of mapped name\values.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class Data implements Cloneable {

    /** An empty data reference. */
    private static final Data EMPTY_DATA = new EmptyData();

    /**
     * Obtain an immutable reference to empty data.
     * 
     * @return An empty <code>Data</code>.
     */
    public static Data emptyData() {
        return EMPTY_DATA;
    }

    /**
	 * The underlying data container.  It contains a map of enumerated types
	 * pointing to object values.
	 * 
	 */
	private final Map<Enum<?>,Object> data;

	/**
	 * Create a Data.
	 * 
	 */
	public Data() { this(7); }

	/**
	 * Create a Data.
	 * 
	 * @param initialCapacity
	 *            The initial capacity of the data container.
	 */
	public Data(final Integer initialCapacity) {
		super();
		this.data = new Hashtable<Enum<?>,Object>(initialCapacity, 0.75F);
	}

	/**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        final Data clone = new Data(data.size());
        for (final Entry<Enum<?>, Object> entry : data.entrySet()) {
            clone.set(entry.getKey(), entry.getValue());
        }
        return clone;
    }

	/**
     * Determine if the input contains the data item.
     * 
     * @param key
     *            The data item key.
     * @return True if the data contains the data item.
     */
    public Boolean isSet(final Enum<?> key) {
        return data.containsKey(key);
    }

    /**
	 * Obtain a data item.
	 * 
	 * @param key
	 *            The data item key.
	 * @return The data item; or null if the container has no map for this key.
	 */
	public Object get(final Enum<?> key) { return data.get(key); }

	/**
     * Extract a typed list from the data.
     * 
     * @param <T>
     *            The list type.
     * @param key
     *            The data key.
     * @return A <code>List&lt;T&gt;</code>.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(final Enum<?> key) {
        final List<T> list;
        if (data.containsKey(key)) {
            final List unknownList = (List) data.get(key);
            list = new ArrayList<T>(unknownList.size());
            for (Iterator i = unknownList.iterator(); i.hasNext(); ) {
                list.add((T) i.next());
            }
        } else {
            list = Collections.emptyList();
        }
        return list;
    }

    /**
	 * Set a data item.
	 * 
	 * @param key
	 *            The data item key.
	 * @param value
	 *            The data item value.
	 * @return The previous value for the data item key.
	 */
	public Object set(final Enum<?> key, final Object value) {
		return data.put(key, value);
	}

    /**
     * Unset a data item.
     * 
     * @param key
     *            The data item key.
     * @return The data item's previous value.
     */
    public Object unset(final Enum<?> key) {
        return data.remove(key);
    }

    private static class EmptyData extends Data {

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.Data#clone()
         */
        @Override
        public Object clone() {
            throw Assert.createUnreachable("CANNOT CLONE EMPTY DATA");
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.Data#get(java.lang.Enum)
         */
        @Override
        public Object get(final Enum<?> key) {
            return null;
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.Data#getList(java.lang.Enum)
         */
        @Override
        public <T> List<T> getList(final Enum<?> key) {
            return null;
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.Data#set(java.lang.Enum, java.lang.Object)
         */
        @Override
        public Object set(final Enum<?> key, final Object value) {
            throw Assert.createUnreachable("CANNOT SET EMPTY DATA");
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.Data#unset(java.lang.Enum)
         */
        @Override
        public Object unset(final Enum<?> key) {
            throw Assert.createUnreachable("CANNOT UNSET EMPTY DATA");
        }
    }
}
