/**
 * Created On: 4-Jun-07 1:20:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar.TabId;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class BrowserKeyboardHelper extends BrowserHelper {

    /**
     * Create BrowserKeyboardHelper.
     */
    BrowserKeyboardHelper(final Browser browserApplication) {
        super(browserApplication);
    }

    /**
     * Bind keys to the appropriate actions.
     */
    public void bindKeys() {
        final MainPanel mainPanel = getMainWindow().getMainPanel();
        disableDefaultTraversalKeys();

        // F1: help tab
        mainPanel.bindF1Key(new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                selectHelpTab();
            }
        });
        // ctrl-F: find
        mainPanel.bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_F,
            InputEvent.CTRL_DOWN_MASK),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    requestFocusInSearch();
                }
            });
        // tab: select the next focus element
        mainPanel.bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    selectNextFocus();
                }
            });
        // shift-tab: select the previous focus element
        mainPanel.bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
            InputEvent.SHIFT_DOWN_MASK),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    selectPreviousFocus();
                }
            });
        // ctrl-tab: next tab
        mainPanel.bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
            InputEvent.CTRL_DOWN_MASK),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    selectNextTab();
                }
            });
        // ctrl-shift-tab: previous tab
        mainPanel.bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
            InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    selectPreviousTab();
                }
            });
        // shift-f10: open popup window
        mainPanel.bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_F10,
            InputEvent.SHIFT_DOWN_MASK),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    getMainWindow().notifyShowPopup();
                }
            });
        // insert: create container (if container tab selected), or
        // create outgoing email invitation (if contact tab is selected)
        mainPanel.bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0),
                new AbstractAction() {
                    public void actionPerformed(final ActionEvent e) {
                        if (isSelectedContainerTab()) {
                            runCreateContainer();
                        } else if (isSelectedContactTab()) {
                            if (browserApplication.isOnline()) {
                                runCreateOutgoingEMailInvitation();
                            }
                        }
                    }
                });
    }

    /**
     * Disable the default traversal keys (tab, shift-tab, etc.)
     * This makes it possible to bind different actions to these keys.
     */
    private void disableDefaultTraversalKeys() {
        final Set<? extends AWTKeyStroke> emptySet = Collections.emptySet();
        getMainWindow().setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, emptySet);
        getMainWindow().setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, emptySet);
    }

    /**
     * Get the main window.
     */
    private BrowserWindow getMainWindow() {
        return (BrowserWindow)browserApplication.getMainWindow();
    }

    /**
     * Determine if the contact tab is selected.
     * 
     * @return True if the contact tab is selected.
     */
    private Boolean isSelectedContactTab() {
        return (TabId.CONTACT == browserApplication.getSelectedTabId());
    }

    /**
     * Determine if the container tab is selected.
     * 
     * @return True if the container tab is selected.
     */
    private Boolean isSelectedContainerTab() {
        return (TabId.CONTAINER == browserApplication.getSelectedTabId());
    }

    /**
     * Request focus in search.
     */
    private void requestFocusInSearch() {
        browserApplication.requestFocusInSearch();
    }

    /**
     * Request focus in tab.
     */
    private void requestFocusInTab() {
        browserApplication.requestFocusInTab();
    }

    /**
     * Run the create container action.
     */
    private void runCreateContainer() {
        browserApplication.runCreateContainer();
    }

    /**
     * Run the create outgoing email invitation action.
     */
    private void runCreateOutgoingEMailInvitation() {
        browserApplication.runCreateOutgoingEMailInvitation();
    }

    /**
     * Select the help tab.
     */
    private void selectHelpTab() {
        browserApplication.selectTab(MainTitleAvatar.TabId.HELP);
    }

    /**
     * Select the next focus.
     */
    private void selectNextFocus() {
        final java.awt.Component focusOwner =
            KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (browserApplication.isAncestorOf(AvatarId.MAIN_TITLE, focusOwner)) {
            requestFocusInTab();
        } else {
            requestFocusInSearch();
        }
    }

    /**
     * Select the next tab.
     */
    private void selectNextTab() {
        final TabId tabId = browserApplication.getSelectedTabId();
        switch(tabId) {
        case CONTAINER:
            browserApplication.selectTab(TabId.CONTACT);
            break;
        case CONTACT:
            browserApplication.selectTab(TabId.HELP);
            break;
        case HELP:
            browserApplication.selectTab(TabId.CONTAINER);
            break;
        default:
            Assert.assertUnreachable("Unknown main title tab id.");
        }
    }

    /**
     * Select the previous focus.
     * At the moment this is the same as select the next focus.
     */
    private void selectPreviousFocus() {
        selectNextFocus();
    }

    /**
     * Select the previous tab.
     */
    private void selectPreviousTab() {
        final TabId tabId = browserApplication.getSelectedTabId();
        switch(tabId) {
        case CONTAINER:
            browserApplication.selectTab(TabId.HELP);
            break;
        case CONTACT:
            browserApplication.selectTab(TabId.CONTAINER);
            break;
        case HELP:
            browserApplication.selectTab(TabId.CONTACT);
            break;
        default:
            Assert.assertUnreachable("Unknown main title tab id.");
        }
    }
}
