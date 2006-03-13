/*
 * Feb 24, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionInviteContactAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    // Variables declaration - do not modify
	private javax.swing.JButton closeJButton;
	private javax.swing.JLabel embeddedAssistanceJLabel;
	private javax.swing.JButton inviteJButton;
	private javax.swing.JLabel inviteJLabel;
	private javax.swing.JButton manageJButton;
    private javax.swing.JTextField userJTextField;
	// End of variables declaration

	/**
	 * Create a SessionInviteContactAvatar.
	 * 
	 */
	public SessionInviteContactAvatar() {
		// COLOR SessionInvite Background
		super("SessionInviteContact", Color.WHITE);
		initComponents();
	}
	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.SESSION_INVITE_CONTACT; }
	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }
	/**
	 * @see com.thinkparity.browser.javax.swing.AbstractJPanel#isInputValid()
	 * 
	 */
	public Boolean isInputValid() {
		final JabberId jabberId = extractJabberId();
		if(null != jabberId) { return Boolean.TRUE; }

		return Boolean.FALSE;
	}
	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}
	private void closeJButtonActionPerformed(final ActionEvent e) {
		SwingUtilities.getWindowAncestor(this).dispose();
	}
	private JabberId extractJabberId() {
    	final String contact = userJTextField.getText();
    	JabberId jabberId = null;
    	try { jabberId = JabberIdBuilder.parseQualifiedJabberId(contact); }
    	catch(final IllegalArgumentException iax) { jabberId = null; }
    	if(null == jabberId) {
	    	try { jabberId = JabberIdBuilder.parseQualifiedUsername(contact); }
	    	catch(final IllegalArgumentException iax) { jabberId = null; }
	    	if(null == jabberId) {
		    	try { jabberId = JabberIdBuilder.parseUsername(contact); }
		    	catch(final IllegalArgumentException iax) { jabberId = null; }
	    	}
    	}
    	return jabberId;
    }

	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">
    private void initComponents() {
        inviteJLabel = LabelFactory.create();
        userJTextField = TextFactory.create();
        embeddedAssistanceJLabel = LabelFactory.create();
        inviteJButton = ButtonFactory.create();
        closeJButton = ButtonFactory.create();
        manageJButton = ButtonFactory.create();

        embeddedAssistanceJLabel.setText(getString("EmbeddedAssistance"));
        inviteJButton.setText(getString("InviteButton"));
        inviteJLabel.setText(getString("InviteLabel"));
        closeJButton.setText(getString("CloseButton"));
        manageJButton.setText(getString("ManageButton"));

        closeJButton.addActionListener(new ActionListener() {
        	public void actionPerformed(final ActionEvent evt) {
        		closeJButtonActionPerformed(evt);
        	}
        });

        inviteJButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                inviteJButtonActionPerformed(evt);
            }
        });

        manageJButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				manageJButtonActionPerformed(e);
			}
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, embeddedAssistanceJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(inviteJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(userJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(inviteJButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(manageJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(closeJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(embeddedAssistanceJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(inviteJLabel)
                    .add(inviteJButton)
                    .add(userJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeJButton)
                    .add(manageJButton))
                .addContainerGap())
        );
    }// </editor-fold>

	private void inviteJButtonActionPerformed(final ActionEvent e) {
		if(isInputValid()) {
			toggleVisualFeedback(Boolean.TRUE);
			final JabberId jabberId = extractJabberId();
			try {
				getSessionModel().inviteContact(jabberId);
				closeJButtonActionPerformed(e);
			}
			catch(final ParityException px) { throw new RuntimeException(px); }
			finally { toggleVisualFeedback(Boolean.TRUE); }
		}
	}

	/**
	 * Close the window and display manage contacts.
	 * 
	 * @param e
	 *            The action event.
	 */
	private void manageJButtonActionPerformed(final ActionEvent e) {
		closeJButtonActionPerformed(e);
		getController().displaySessionManageContacts();
	}
}
