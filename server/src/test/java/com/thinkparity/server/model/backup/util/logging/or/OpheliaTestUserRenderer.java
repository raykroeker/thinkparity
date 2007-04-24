/*
 * Created On:  25-Nov-06 12:06:39 PM
 */
package com.thinkparity.ophelia.model.util.logging.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OpheliaTestUserRenderer implements ObjectRenderer {

    /**
     * Create OpheliaTestUserRenderer.
     *
     */
    public OpheliaTestUserRenderer() {
        super();

    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    public String doRender(final Object o) {
        if(null == o) {
            return Separator.Null.toString();
        } else {
            final OpheliaTestUser o2 = (OpheliaTestUser) o;
            return StringUtil.toString(o2.getClass(),
                    "getEnvironment().getXMPPHost()", o2.getEnvironment().getXMPPHost(),
                    "getId().getUsername()", o2.getId().getUsername(),
                    "getLocalId()", o2.getLocalId());
        }
    }
}
