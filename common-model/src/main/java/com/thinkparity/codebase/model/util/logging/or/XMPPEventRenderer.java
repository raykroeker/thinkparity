/*
 * Created On:  14-Nov-06 6:04:51 PM
 */
package com.thinkparity.codebase.model.util.logging.or;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.beans.BeanException;
import com.thinkparity.codebase.beans.BeanUtils;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XMPPEventRenderer implements ObjectRenderer {

    /**
     * Create XMPPEventRenderer.
     *
     */
    public XMPPEventRenderer() {
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
            final BeanUtils beanUtils = new BeanUtils(o);
            final PropertyDescriptor[] properties = beanUtils.getPropertyDescriptors();
            final List<Object> memberData = new ArrayList<Object>(properties.length * 2 + 4);
            memberData.add("date");
            memberData.add(DateUtil.format(((XMPPEvent) o).getDate(), DateImage.ISO));
            memberData.add("id");
            memberData.add(((XMPPEvent) o).getId());
            for (int i = 0; i < properties.length; i++) {
                memberData.add(properties[i].getName());
                try {
                    memberData.add(beanUtils.readProperty(properties[i]));
                } catch (final BeanException bx) {
                    memberData.add("UnableToRead");
                }
            }
            return StringUtil.toString(o.getClass(),
                    memberData.toArray(new Object[] {}));
        }
    }
}
