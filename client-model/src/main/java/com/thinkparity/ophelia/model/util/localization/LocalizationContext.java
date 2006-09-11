/*
 * Created On: Mar 5, 2006
 */
package com.thinkparity.ophelia.model.util.localization;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum LocalizationContext implements com.thinkparity.codebase.l10n.L18nContext {

	MODEL("Model");

	/** The l18n lookup context. */
	private final String lookupContext;

	/**
	 * Create a LocalizationContext.
	 * 
	 * @param lookupContext
	 *            The l18n lookup context.
	 */
	private LocalizationContext(final String lookupContext) {
		this.lookupContext = lookupContext;
	}

	/**
	 * @see com.thinkparity.codebase.l10n.L18nContext#getLookupContext()
	 * 
	 */
	public String getLookupContext() { return lookupContext; }
}
