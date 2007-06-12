/*
 * Created On:  8-Jun-07 9:19:10 AM
 */
package com.thinkparity.desdemona.util.logging.or;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.desdemona.web.ServiceRequest;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceRequestRenderer implements ObjectRenderer {

    /**
     * Create ServiceRequestRenderer.
     *
     */
    public ServiceRequestRenderer() {
        super();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    public String doRender(final Object o) {
        if (null == o) {
            return "null";
        } else {
            final ServiceRequest serviceRequest = (ServiceRequest) o;
            return StringUtil.toString(serviceRequest.getClass(),
                    "getService()", serviceRequest.getService().getId(),
                    "getOperation()", serviceRequest.getOperation().getId());
        }
    }

}
