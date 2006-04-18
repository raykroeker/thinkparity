/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main.popup;

import com.thinkparity.browser.platform.util.l10n.MainCellL18n;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class PopupL18n {

    /** The main cell localization. */
    private final MainCellL18n l18n;

    /**
     * Create a PopupL18n.
     */
    PopupL18n(final String l18nContext) {
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