/*
 * Mar 5, 2006
 */
package com.thinkparity.model.parity.model;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum L18nContext implements com.thinkparity.codebase.l10n.L18nContext {

	DOCUMENT("Artifact.Document");

	/**
	 * The l18n lookup context.
	 * 
	 */
	private final String lookupContext;

	/**
	 * Create a L18nContext.
	 * 
	 * @param lookupContext
	 *            The l18n lookup context.
	 */
	private L18nContext(final String lookupContext) {
		this.lookupContext = lookupContext;
	}

	/**
	 * @see com.thinkparity.codebase.l10n.L18nContext#getLookupContext()
	 * 
	 */
	public String getLookupContext() { return lookupContext; }
}
