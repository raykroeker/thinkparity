/*
 * Created On:  28-Mar-07 3:56:27 PM
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.codebase.model.migrator.Resource;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ResourceRenderer implements ObjectRenderer {

    /**
     * Create ResourceRenderer.
     *
     */
    public ResourceRenderer() {
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
            final Resource resource = (Resource) o;
            return StringUtil.toString(getClass(), "getChecksum()",
                    resource.getChecksum(), "getChecksumAlgorithm()",
                    resource.getChecksumAlgorithm(), "getId()",
                    resource.getId(), "getPath()", resource.getPath(),
                    "getSize()", resource.getSize());
        }
    }
}
