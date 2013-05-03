/*
 * Created On:  Nov 19, 2007 12:59:19 PM
 */
package com.thinkparity.ophelia.support.util.process;

import java.util.List;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ProcessUtil {

    /**
     * Obtain the current process id.
     * 
     * @return A <code>String</code>.
     */
    String getProcessId() throws ProcessException;

    /**
     * Obtain a process list.
     * 
     * @return A <code>List<ProcessInfo></code>.
     */
    List<ProcessInfo> getProcessList() throws ProcessException;

    /**
     * Determine if the process utility is loaded.
     * 
     * @return A <code>Boolean</code>.
     */
    Boolean isLoaded();

    /**
     * Terminate a process.
     * 
     * @param id
     *            A <code>String</code>.
     */
    Boolean terminate(String id) throws ProcessException;
}
