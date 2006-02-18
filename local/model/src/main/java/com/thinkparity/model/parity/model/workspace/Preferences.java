/**
 * 
 */
package com.thinkparity.model.parity.model.workspace;

import java.util.Locale;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 */
public interface Preferences {

	public Locale getLocale();

	public String getPassword();

	public String getServerHost();

	public Integer getServerPort();

	public User getSystemUser();

	public String getUsername();

	public Boolean isSetLocale();

	public Boolean isSetUsername();

	public void setLocale(final Locale locale);

	public void setPassword(final String password);

	public void clearPassword();

	public void setUsername(final String username);
}
