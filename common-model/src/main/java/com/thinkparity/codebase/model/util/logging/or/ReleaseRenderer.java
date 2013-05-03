/*
 * Created On:  28-Mar-07 3:56:17 PM
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.model.migrator.Release;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReleaseRenderer implements ObjectRenderer {

    /**
     * Create ReleaseRenderer.
     *
     */
    public ReleaseRenderer() {
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
            final Release release = (Release) o;
            return StringUtil.toString(getClass(), "getDate()",
                    release.getDate(), "getId()", release.getId(), "getName()",
                    release.getName(), "getOS().name()",
                    release.getOs().name());
        }
    }

}
