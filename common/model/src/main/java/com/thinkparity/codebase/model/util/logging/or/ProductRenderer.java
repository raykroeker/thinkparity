/*
 * Created On:  28-Mar-07 3:56:27 PM
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.codebase.model.migrator.Product;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProductRenderer implements ObjectRenderer {

    /**
     * Create ResourceRenderer.
     *
     */
    public ProductRenderer() {
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
            final Product product = (Product) o;
            return StringUtil.toString(getClass(), "getId()", product.getId(),
                    "getName()", product.getName());
        }
    }
}
