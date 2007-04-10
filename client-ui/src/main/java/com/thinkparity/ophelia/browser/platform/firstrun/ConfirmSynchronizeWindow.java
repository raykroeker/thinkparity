/*
 * Created On: October 18, 2006, 6:44 PM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.model.workspace.InitializeMediator;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowPanel;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitle;
import com.thinkparity.ophelia.browser.platform.firstrun.ConfirmSynchronizeAvatar.AccountType;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI Confirm Synchronize Window<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public final class ConfirmSynchronizeWindow extends OpheliaJFrame implements
        InitializeMediator {

    /** The confirm synchronize avatar. */
    private ConfirmSynchronizeAvatar confirmSynchronizeAvatar;

    /** The panel onto which all displays are dropped. */
    private WindowPanel windowPanel;

    /**
     * Create ConfirmSynchronizeWindow.
     * 
     */
    public ConfirmSynchronizeWindow() {
        super("ConfirmSynchronizeWindow");
        confirmSynchronizeAvatar = new ConfirmSynchronizeAvatar();
        windowPanel = new WindowPanel();
        windowPanel.getWindowTitle().setBorderType(WindowTitle.BorderType.WINDOW_BORDER2);
        initComponents(confirmSynchronizeAvatar);
        confirmSynchronizeAvatar.reload();
        pack();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.InitializeMediator#confirmRestorePremium()
     */
    public Boolean confirmRestorePremium() {
        confirmSynchronizeAvatar.setAccountType(AccountType.PREMIUM);
        setVisibleAndWait();
        return didConfirm();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.InitializeMediator#confirmRestoreStandard()
     */
    public Boolean confirmRestoreStandard() {
        confirmSynchronizeAvatar.setAccountType(AccountType.STANDARD);
        setVisibleAndWait();
        return didConfirm();
    }

    /**
     * Determine if the user confirmed or not.
     * 
     * @return true if the user confirmed, false otherwise.
     */
    private Boolean didConfirm() {
        return confirmSynchronizeAvatar.didConfirm();
    }

    /**
     * Initialize the swing components on the window.
     * 
     * @param avatar
     *            The avatar.
     */
    private void initComponents(final Avatar avatar) {
        windowPanel.addPanel(avatar, Boolean.TRUE);
        add(windowPanel);
    }
}
