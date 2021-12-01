/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.document;

import java.io.File;
import java.io.IOException;

import com.thinkparity.ophelia.model.util.Opener;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.swing.DesktopUtil;


/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class Open extends AbstractBrowserAction {

	/** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
     * Create Open.
     * 
     * @param browser
     *            The browser application.
     */
	public Open(final Browser browser) {
		super(ActionId.DOCUMENT_OPEN);
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		getDocumentModel().open(documentId, new Opener() {
		    public void open(final File file) {
		        try {
		            DesktopUtil.open(file);
                } catch (final IOException iox) {
                    throw translateError(iox);
                }
            }
        });
	}

	/** Data keys. */
	public enum DataKey { DOCUMENT_ID }
}
