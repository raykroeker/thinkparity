/*
 * Created On:  8-Dec-06 10:08:50 AM
 */
package com.thinkparity.codebase.log4j.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.jabber.JabberId;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class JabberIdRenderer implements ObjectRenderer {

    /**
     * Create JabberIdRenderer.
     *
     */
    public JabberIdRenderer() {
        super();

    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    public String doRender(final Object o) {
        if (null == o) {
            return Separator.Null.toString();
        } else {
            final JabberId o2 = (JabberId) o;
            return StringUtil.toString(o2.getClass(),
                    "getUsername()", o2.getUsername());
        }
    }
}
