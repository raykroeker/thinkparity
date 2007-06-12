/*
 * Created On:  29-May-07 4:57:05 PM
 */
package com.thinkparity.service;

import java.util.Calendar;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * <b>Title:</b>thinkParity System Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "System")
public interface SystemService {

    /**
     * Read the service date/time.
     * 
     * @return An instance of a <code>Calendar</code> in GMT.
     */
    @WebMethod
    Calendar readDateTime(AuthToken authToken);

    @WebMethod
    String readVersion(AuthToken authToken);
}
