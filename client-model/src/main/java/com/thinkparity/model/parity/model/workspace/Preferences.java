/**
 * 
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.util.Locale;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 */
public interface Preferences {

	public void clearPassword();

	public File getArchiveOutputDirectory();

	public Locale getLocale();

	public String getPassword();

	public String getServerHost();

	public Integer getServerPort();

	public User getSystemUser();

	public String getUsername();

    public Long getLastRun();

	public Boolean isSetArchiveOutputDirectory();

	public Boolean isSetLocale();

	public Boolean isSetUsername();

	public void setArchiveOutputDirectory(final File archiveOutputDirectory);

	public void setLocale(final Locale locale);

	public void setPassword(final String password);

	public void setUsername(final String username);
}
