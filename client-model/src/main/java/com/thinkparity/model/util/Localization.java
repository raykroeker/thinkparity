/*
 * Mar 5, 2006
 */
package com.thinkparity.model.util;

import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.l10n.L18nContext;
import com.thinkparity.codebase.l10n.L18nResource;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Localization extends L18n {

	/**
	 * Create a Localization.
	 * 
	 * @param l18nContext
	 *            Localization context.
	 */
	public Localization(final L18nContext l18nContext) {
		super(new L18nResource() {
            public String getResourceBundleBaseName() {
                return "localization.Model_Messages";
            }
        }, l18nContext);
	}

	public String getString(final String localKey) {
		return super.getString(localKey);
	}

	public String getString(final String localKey, final Object[] arguments) {
		return super.getString(localKey, arguments);
	}
}
