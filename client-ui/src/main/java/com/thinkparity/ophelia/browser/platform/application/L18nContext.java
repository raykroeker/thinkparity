/*
 * Mar 20, 2006
 */
package com.thinkparity.ophelia.browser.platform.application;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum L18nContext implements com.thinkparity.codebase.l10n.L18nContext {

	BROWSER2("Browser2Application"), SYS_APP("SystemApplication");

	private final String lookupContext;

	private L18nContext(final String lookupContext) {
		this.lookupContext = lookupContext;
	}

	public String getLookupContext() { return lookupContext; }
}
