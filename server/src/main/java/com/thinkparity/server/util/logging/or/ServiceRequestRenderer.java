/*
 * Created On:  8-Jun-07 9:19:10 AM
 */
package com.thinkparity.desdemona.util.logging.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.desdemona.web.ServiceRequest;
import com.thinkparity.desdemona.web.service.Operation;
import com.thinkparity.desdemona.web.service.Service;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Desdemona Model Service Request Log4J Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceRequestRenderer implements ObjectRenderer {

    /** A service renderer. */
    private final ObjectRenderer serviceRenderer;

    /** An operation renderer. */
    private final ObjectRenderer operationRenderer;

    /**
     * Create ServiceRequestRenderer.
     *
     */
    public ServiceRequestRenderer() {
        super();
        this.serviceRenderer = new ServiceRenderer();
        this.operationRenderer = new OperationRenderer();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    public String doRender(final Object o) {
        if (null == o) {
            return Separator.Null.toString();
        } else {
            final ServiceRequest o2 = (ServiceRequest) o;
            return StringUtil.toString(ServiceRequest.class,
                    "getService()", doRender(o2.getService()),
                    "getOperation()", doRender(o2.getOperation()));
        }
    }

    /**
     * Render a service.
     * 
     * @param o
     *            A <code>Service</code>.
     * @return A <code>String</code>.
     */
    private String doRender(final Service o) {
        return serviceRenderer.doRender(o);
    }

    /**
     * Render an operation.
     * 
     * @param o
     *            An <code>Operation</code>.
     * @return A <code>String</code>.
     */
    private String doRender(final Operation o) {
        return operationRenderer.doRender(o);
    }
}
