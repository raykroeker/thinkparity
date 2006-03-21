/*
 * Mar 20, 2006
 */
package com.thinkparity.browser.platform.util.l10n;

import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.l10n.L18nContext;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ApplicationL18n extends L18n {

	/**
	 * Create a ApplicationL18n.
	 * 
	 * @param l18nContext
	 *            The localization contet.
	 */
	public ApplicationL18n(final L18nContext l18nContext) {
		super(L18nResource.APPLICATION, l18nContext);
	}

	public String getString(final String localKey) {
		return super.getString(localKey);
	}
	public String getString(final String localKey, final Object[] arguments) {
		return super.getString(localKey, arguments);
	}
}
