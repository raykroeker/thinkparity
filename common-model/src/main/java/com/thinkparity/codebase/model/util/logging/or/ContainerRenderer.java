/*
 * Created On:  25-Nov-06 12:49:03 PM
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.model.container.Container;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerRenderer implements ObjectRenderer {

    /**
     * Create ContainerRenderer.
     *
     */
    public ContainerRenderer() {
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
            final Container o2 = (Container) o;
            return StringUtil.toString(o2.getClass(),
                    "getId()", o2.getId(),
                    "getName()", o2.getName(),
                    "getUniqueId()", o2.getUniqueId(),
                    "hashCode()", Integer.toHexString(o2.hashCode()));
        }
    }
}
