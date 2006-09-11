/*
 * Created On: Aug 29, 2006 12:28:59 PM
 */
package com.thinkparity.desdemona.util.logging.or;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.codebase.model.profile.Profile;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProfileObjectRenderer extends AbstractObjectRenderer implements
        ObjectRenderer {

    /** Create ProfileObjectRenderer. */
    public ProfileObjectRenderer() {
        super();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     */
    public String doRender(final Object o) {
        if (null == o) {
            return "null";
        } else {
            final Profile p = (Profile) o;
            return new StringBuffer()
                .append("id:").append(render(p.getId()))
                .append(",name:").append(p.getName())
                .append(",organization:").append(p.getOrganization())
                .append(",title:").append(p.getTitle())
                .toString();
        }
    }
}
