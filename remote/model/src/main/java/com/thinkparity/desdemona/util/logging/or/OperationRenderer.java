/*
 * Created On:  26-Sep-07 7:54:50 PM
 */
package com.thinkparity.desdemona.util.logging.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.desdemona.web.service.Operation;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Desdemona Model Operation Log4J Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OperationRenderer implements ObjectRenderer {

    /**
     * Create OperationRenderer.
     *
     */
    public OperationRenderer() {
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
            final Operation o2 = (Operation) o;
            return StringUtil.toString(Operation.class, "getId()", o2.getId());
        }
    }
}
