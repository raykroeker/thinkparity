/*
 * Created On:  3-Jun-07 3:04:15 PM
 */
package com.thinkparity.desdemona.model.system;

import java.util.Calendar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface SystemModel {

    public Calendar readDateTime();

    public String readVersion();
}
