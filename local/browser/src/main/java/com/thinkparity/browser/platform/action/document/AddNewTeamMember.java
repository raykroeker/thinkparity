/*
 * Tue May 02 14:21:01 PDT 2006
 */
package com.thinkparity.browser.platform.action.document;

import java.util.List;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.JabberId;

/**
 * An action to add a team member to an artifact.  The action will present
 * a dialogue; allowing the user to select from their contacts.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AddNewTeamMember extends AbstractAction {

    private static final Icon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.ADD_TEAM_MEMBER;
        NAME = "Document.AddNewTeamMember";
    }
        
    /**
     * The browser application.
     * 
     */
    private final Browser browser;

    /**
     * Create a SendVersion.
     * 
     */
    public AddNewTeamMember(final Browser browser) {
        super("AddNewTeamMember", ID, NAME, ICON);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        final Long documentId  = (Long) data.get(DataKey.DOCUMENT_ID);
        final List<JabberId> jabberIds = getDataJabberIds(data, DataKey.JABBER_IDS);

        if(null == jabberIds) {
            browser.displayAddNewDocumentTeamMember(documentId);
        }
        else {
            for(final JabberId jabberId : jabberIds) {
                getDocumentModel().share(documentId, jabberId);
                browser.fireDocumentUpdated(documentId);
            }
        }
    }

    public enum DataKey { DOCUMENT_ID, JABBER_IDS }
}
