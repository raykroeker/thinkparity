/**
 * 
 */
package com.thinkparity.model.parity.model.workspace;

import java.util.Locale;

/**
 * @author raykroeker@gmail.com
 */
public interface Preferences {

	public Locale getLocale();

	public String getServerHost();

	public Integer getServerPort();

	public String getSystemUsername();

	public String getUsername();

	public Boolean isSetLocale();

	public Boolean isSetUsername();

	public void setLocale(final Locale locale);

	public void setUsername(final String username);
}
