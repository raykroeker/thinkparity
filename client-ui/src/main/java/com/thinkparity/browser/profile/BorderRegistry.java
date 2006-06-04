/*
 * Created On: Jun 3, 2006 2:54:10 PM
 * $Id$
 */
package com.thinkparity.browser.profile;

import java.util.HashMap;
import java.util.Map;

import javax.swing.border.Border;

import com.thinkparity.browser.javax.swing.border.ImageBorder;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class BorderRegistry {

    private static final Map<Class, Border> REGISTRY;

    static {
        REGISTRY = new HashMap<Class, Border>();

        REGISTRY.put(
                ProfileManagerAvatar.class,
                new ImageBorder("ProfileManagerBorderTop.png",
                        "ProfileManagerBorderLeft.png",
                        "ProfileManagerBorderBottom.png",
                        "ProfileManagerBorderRight.png"));

        REGISTRY.put(
                DeleteProfileAvatar.class,
                new ImageBorder("DeleteProfileBorderTop.png",
                        "DeleteProfileBorderLeft.png",
                        "DeleteProfileBorderBottom.png",
                        "DeleteProfileBorderRight.png"));

        REGISTRY.put(
                RenameProfileAvatar.class,
                new ImageBorder("RenameProfileBorderTop.png",
                        "RenameProfileBorderLeft.png",
                        "RenameProfileBorderBottom.png",
                        "RenameProfileBorderRight.png"));

        REGISTRY.put(
                CreateProfileAvatar.class,
                new ImageBorder("NewProfileBorderTop.png",
                        "NewProfileBorderLeft.png",
                        "NewProfileBorderBottom.png",
                        "NewProfileBorderRight.png"));
    }

    /** Create BorderRegistry. */
    BorderRegistry() { super(); }

    Border get(final Class clasz) { return REGISTRY.get(clasz); }
}
