/*
 * Created On: Aug 31, 2006 10:26:41 AM
 */
package com.thinkparity.model.parity.model.index;

import java.io.IOException;
import java.util.List;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface IndexImpl<T, U> {
    public void delete(final T o) throws IOException;
    public void index(final T o) throws IOException;
    public List<U> search(final String expression) throws IOException;
}
