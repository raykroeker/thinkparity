/*
 * Created On: Aug 31, 2006 10:26:41 AM
 */
package com.thinkparity.ophelia.model.index;

import java.io.IOException;
import java.util.List;

/**
 * <b>Title:</b>thinkParity OpheliaModel Index Implementation<br>
 * <b>Description:</b>An index implementation knows how to read/write and
 * search a given type of index.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 * @param <T>
 *            The type of thinkParity object to index.
 * @param <U>
 *            The thinkParity object's id type.
 */
public interface IndexImpl<T, U> {

    /**
     * Delete a thinkParity object from the index.
     * 
     * @param o
     *            An instance of <code>T</code>.
     * @throws IOException
     */
    public void delete(final T o) throws IOException;

    /**
     * Index a thinkParity object.
     * 
     * @param o
     *            An instance of <code>T</code>.
     * @throws IOException
     */
    public void index(final T o) throws IOException;

    /**
     * Search the index using the expression and return instances of the
     * thinkParity object's id.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List</code> of <code>U</code>.
     * @throws IOException
     */
    public List<U> search(final String expression) throws IOException;
}
