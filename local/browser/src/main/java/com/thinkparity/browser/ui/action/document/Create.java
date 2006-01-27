/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.ui.action.document;

import java.io.File;
import java.util.UUID;

import javax.swing.Icon;
import javax.swing.JFileChooser;

import com.thinkparity.browser.ui.action.AbstractAction;
import com.thinkparity.browser.ui.action.ActionId;
import com.thinkparity.browser.ui.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Create extends AbstractAction {

	public enum DataKey { PROJECT_ID }

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_CREATE;
		NAME = "Create";
	}

	/**
	 * The file chooser.
	 * 
	 * @see #getJFileChooser()
	 */
	private JFileChooser jFileChooser;

	/**
	 * Create a Create.
	 * 
	 */
	public Create() { super(ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.ui.action.AbstractAction#invoke(com.thinkparity.browser.ui.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		if(JFileChooser.APPROVE_OPTION == getJFileChooser().showOpenDialog(null)) {
			final File file = jFileChooser.getSelectedFile();
			final UUID projectId = (UUID) data.get(DataKey.PROJECT_ID);
			getDocumentModel().create(projectId, file.getName(), file.getName(), file);
		}
	}

	/**
	 * Obtain the file chooser.
	 * 
	 * @return The file chooser.
	 */
	private JFileChooser getJFileChooser() {
		if(null == jFileChooser) { jFileChooser = new JFileChooser(); }
		return jFileChooser;
	}
}
