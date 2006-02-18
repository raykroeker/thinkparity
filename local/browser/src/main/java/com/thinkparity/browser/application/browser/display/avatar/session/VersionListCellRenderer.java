/*
 * Feb 17, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.session;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.thinkparity.browser.model.document.WorkingVersion;
import com.thinkparity.browser.platform.util.l10n.ListItemLocalization;

import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class VersionListCellRenderer extends DefaultListCellRenderer {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * List item localization.
	 * 
	 */
	protected final ListItemLocalization localization;

	/**
	 * Create a VersionListCellRenderer.
	 * 
	 */
	public VersionListCellRenderer() {
		super();
		this.localization = new ListItemLocalization("VersionListCellRenderer");
	}

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 * 
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		final DocumentVersion version = (DocumentVersion) value;
		final String versionText;
		if(version == WorkingVersion.getWorkingVersion()) {
			versionText = getString("LatestVersion");
		}
		else {
			versionText = getString("Version", new Object[] {version.getVersionId()});
		}
		super.getListCellRendererComponent(list, versionText, index, isSelected, cellHasFocus);
		return this;
	}

	protected String getString(final String localKey) {
		return localization.getString(localKey);
	}

	protected String getString(final String localKey, final Object[] arguments) {
		return localization.getString(localKey, arguments);
	}
}
