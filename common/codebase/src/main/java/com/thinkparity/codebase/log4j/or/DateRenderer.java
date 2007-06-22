/*
 * Created On:  31-Oct-06 2:23:07 PM
 */
package com.thinkparity.codebase.log4j.or;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DateRenderer implements ObjectRenderer {

    /**
     * Create CalendarRenderer.
     *
     */
    public DateRenderer() {
        super();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    public String doRender(final Object o) {
        if(null == o) {
            return "null";
        }
        else {
            final Date d = (Date) o;
            return MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss.SSS Z}", d);
        }
    }
}
