/*
 * Created On:  12-Aug-07 11:36:53 AM
 */
package com.thinkparity.codebase.delegate;

/**
 * <b>Title:</b>thinkParity Codebase Cancelable Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Cancelable {

    /**
     * Cancel the running delegate.
     * 
     * @throws CancelException
     */
    void cancel() throws CancelException;
}
