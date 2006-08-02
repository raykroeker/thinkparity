/**
 * Created On: 1-Aug-06 5:10:10 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.menu;

import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class MenuPopupL18n {
    
    /** The main cell localization. */
    private final MainCellL18n l18n;

    /**
     * Create a PopupL18n.
     */
    MenuPopupL18n(final String l18nContext) {
        super();
        this.l18n = new MainCellL18n(l18nContext);
    }

    String getString(final String localKey) {
        return l18n.getString(localKey);
    }

    String getString(final String localKey, final Object[] arguments) {
        return l18n.getString(localKey, arguments);
    }
}
