/*
 * Created On:  26-Sep-07 7:54:50 PM
 */
package com.thinkparity.desdemona.util.logging.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.desdemona.web.service.Service;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Desdemona Model Service Log4J Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceRenderer implements ObjectRenderer {

    /**
     * Create ServiceRenderer.
     *
     */
    public ServiceRenderer() {
        super();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    @Override
    public String doRender(final Object o) {
        if (null == o) {
            return Separator.Null.toString();
        } else {
            final Service o2 = (Service) o;
            return StringUtil.toString(Service.class, "getId()", o2.getId());
        }
    }
}
