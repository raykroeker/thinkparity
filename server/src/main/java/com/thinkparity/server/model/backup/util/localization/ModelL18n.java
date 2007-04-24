/*
 * Mar 5, 2006
 */
package com.thinkparity.ophelia.model.util.localization;

import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.l10n.L18nContext;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelL18n extends L18n {

	/**
	 * Create a ModelL18n.
	 * 
	 * @param l18nContext
	 *            Localization context.
	 */
	public ModelL18n(final L18nContext l18nContext) {
		super(L18nResource.MODEL, l18nContext);
	}

	public String getString(final String localKey) {
		return super.getString(localKey);
	}

	public String getString(final String localKey, final Object[] arguments) {
		return super.getString(localKey, arguments);
	}
}
