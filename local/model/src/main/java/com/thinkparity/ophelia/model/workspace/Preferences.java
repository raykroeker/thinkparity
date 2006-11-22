/**
 * 
 */
package com.thinkparity.ophelia.model.workspace;

import java.util.Locale;

import com.thinkparity.codebase.model.user.User;


/**
 * @author raykroeker@gmail.com
 */
public interface Preferences {

	public void clearPassword();

	public Locale getLocale();

	public String getPassword();

	public User getSystemUser();

	public String getUsername();

    public Long getLastRun();

	public Boolean isSetLocale();

    public Boolean isSetPassword();

	public Boolean isSetUsername();

	public void setLocale(final Locale locale);

	public void setPassword(final String password);

	public void setUsername(final String username);
}
