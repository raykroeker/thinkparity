/*
 * DisplayInfoAvatar.java
 *
 * Created on October 9, 2006, 1:48 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  Administrator
 */
public class DisplayInfoAvatar extends Avatar {
      
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** Creates new form DisplayInfoAvatar */
    public DisplayInfoAvatar() {
        super("DisplayInfoAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }
    
    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_DISPLAY_INFO;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        logoJLabel = new javax.swing.JLabel();
        versionJLabel = new javax.swing.JLabel();
        copyrightJLabel = new javax.swing.JLabel();
        okJButton = new javax.swing.JButton();

        logoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/thinkParityLogo.png")));
        logoJLabel.setFocusable(false);

        versionJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        versionJLabel.setText(bundle.getString("DisplayInfoAvatar.Version")); // NOI18N
        versionJLabel.setFocusable(false);

        copyrightJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        copyrightJLabel.setText(bundle.getString("DisplayInfoAvatar.Copyright")); // NOI18N
        copyrightJLabel.setFocusable(false);

        okJButton.setText("OK");
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, logoJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, copyrightJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, versionJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .add(okJButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(logoJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 30, Short.MAX_VALUE)
                .add(versionJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(copyrightJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(okJButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_okJButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel copyrightJLabel;
    private javax.swing.JLabel logoJLabel;
    private javax.swing.JButton okJButton;
    private javax.swing.JLabel versionJLabel;
    // End of variables declaration//GEN-END:variables
    
}
