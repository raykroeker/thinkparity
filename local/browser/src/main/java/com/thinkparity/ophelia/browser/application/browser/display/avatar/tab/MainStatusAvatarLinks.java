/**
 * Created On: Jan 7, 2007 3:52:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.platform.action.LinkAction;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class MainStatusAvatarLinks {
    
    /** The saved LinkActions */
    private final List<LinkAction> linkActions;
    
    /** The input LinkAction */
    private LinkAction inputLinkAction;
    
    /** The show once LinkAction */
    private LinkAction showOnceLinkAction;
    
    /** The intro text JLabels */
    private final javax.swing.JLabel[] introJLabels;
    
    /** The link JLabels */
    private final javax.swing.JLabel[] linkJLabels;
    
    /**
     * Create a MainStatusAvatarLinks.
     * 
     * @param introJLabels
     *            The array of intro text <code>javax.swing.JLabel</code>.
     * @param linkJLabels
     *            The array of link <code>javax.swing.JLabel</code>.          
     */
    public MainStatusAvatarLinks(
            final javax.swing.JLabel[] introJLabels,
            final javax.swing.JLabel[] linkJLabels) {
        super();
        this.introJLabels = introJLabels;
        this.linkJLabels = linkJLabels;
        this.linkActions = new ArrayList<LinkAction>();
    }
    
    /**
     * Handle mouse pressed event for a link JLabel.
     * 
     * @param e
     *          The <code>java.awt.event.MouseEvent</code>.
     * @param index
     *          The index to the label (as the user sees them left to right).
     */
    public void linkJLabelMousePressed(final java.awt.event.MouseEvent e, final int index) {
        getLinkAction(index).getAction().actionPerformed(
                new ActionEvent(e.getSource(), e.getID(),
                        "MainStatusAvatarLinks", e.getWhen(), e.getModifiers()));
    }
    
    /**
     * Reload with a new input LinkAction
     * 
     * @param inputLinkAction
     *          The input <code>LinkAction</code>.
     */
    public void reload(final LinkAction inputLinkAction) {
        setInputLinkAction(inputLinkAction);
        reloadShowOnceLinkAction();
        reloadShowAlwaysLinkAction();
        for (int index = 0; index < linkJLabels.length; index++) {
            reloadLinkActionText(index);
        }
    }
    
    /**
     * Add the link action to the list.
     * 
     * @param addLinkAction
     *          The <code>LinkAction</code> to add.
     */
    private void addLinkAction(final LinkAction addLinkAction) {
        boolean found = false;
        // If the LinkAction is already in the list, replace it
        for (final LinkAction linkAction : linkActions) {
            if (linkAction.equals(addLinkAction)) {
                linkActions.set(linkActions.indexOf(linkAction), addLinkAction);
                found = true;
                break;
            }
        }
        // If the LinkAction is not in the list, insert it according to priority
        if (!found) {
            int index = 0;
            if (addLinkAction.getPriority() != LinkAction.LinkPriority.HIGH) {
                for (final LinkAction linkAction : linkActions) {
                    if (linkAction.getPriority() == LinkAction.LinkPriority.HIGH) {
                        index++;
                    } else {
                        break;
                    }
                }
            }
            linkActions.add(index, addLinkAction);
        }
    }
    
    /**
     * Get the input LinkAction
     * 
     * @return A <code>LinkAction</code>.
     */
    private LinkAction getInputLinkAction() {
        return this.inputLinkAction;
    } 
    
    /**
     * Get the LinkAction for the JLabel at this index.
     * 
     * @param index
     *          The index.
     * @return A <code>LinkAction</code>.
     */
    private LinkAction getLinkAction(final int index) {
        if (isShowOnceLinkAction()) {
            return index == 0 ? getShowOnceLinkAction() : null;
        } else if (linkActions.size() > index) {
            return linkActions.get(index);
        } else {
            return null;
        }
    }
        
    /**
     * Get the show-once LinkAction
     * 
     * @return A <code>LinkAction</code>.
     */
    private LinkAction getShowOnceLinkAction() {
        return this.showOnceLinkAction;
    }
        
    /**
     * Determine if there is an input link action.
     * 
     * @return A Boolean.
     */
    private Boolean isInputLinkAction() {
        return (null != inputLinkAction);
    }
    
    /**
     * Determine if there is a show-once link action.
     * 
     * @return A Boolean.
     */
    private Boolean isShowOnceLinkAction() {
        return (null != showOnceLinkAction);
    }
   
    /**
     * Reload the LinkAction text for this index.
     * 
     * @param index
     *          The index.
     */
    private void reloadLinkActionText(final int index) {
        introJLabels[index].setText("");
        linkJLabels[index].setText("");
        final LinkAction linkAction = getLinkAction(index);
        if (null != linkAction) {
            introJLabels[index].setText(linkAction.getIntroText(index==0));
            linkJLabels[index].setText(linkAction.getLinkText());
        }
    }
    
    /**
     * Reload the show-always LinkAction from the input.
     */
    private void reloadShowAlwaysLinkAction() {
        if (isInputLinkAction()) {
            switch(getInputLinkAction().getLinkType()) {
            case CLEAR:
                removeLinkAction(getInputLinkAction());
                break;                
            case SHOW_ALWAYS:
                addLinkAction(getInputLinkAction());
                break;
            case SHOW_ONCE:
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN LINK ACTION TYPE");
            }
        }
    }
    
    /**
     * Reload the show-once LinkAction from the input.
     */
    private void reloadShowOnceLinkAction() {
        if (isInputLinkAction() && getInputLinkAction().getLinkType() == LinkAction.LinkType.SHOW_ONCE) {
            setShowOnceLinkAction(getInputLinkAction());
        } else {
            setShowOnceLinkAction(null); 
        }
    }
    
    /**
     * Remove the link action from the list.
     * 
     * @param removeLinkAction
     *          The <code>LinkAction</code> to remove.
     */
    private void removeLinkAction(final LinkAction removeLinkAction) {
        for (final LinkAction linkAction : linkActions) {
            if (linkAction.equals(removeLinkAction)) {
                linkActions.remove(linkAction);
                break;
            }
        }
    } 
    
    /**
     * Set the input LinkAction
     * 
     * @param inputLinkAction
     *          The input <code>LinkAction</code>.
     */    
    private void setInputLinkAction(final LinkAction inputLinkAction) {
        this.inputLinkAction = inputLinkAction;
    }
    
    /**
     * Set the show-once LinkAction
     * 
     * @param showOnceLinkAction
     *          The show-once <code>LinkAction</code>.
     */    
    private void setShowOnceLinkAction(final LinkAction showOnceLinkAction) {
        this.showOnceLinkAction = showOnceLinkAction;
    }
}
