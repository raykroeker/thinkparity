/*
 * Created On:  2007-11-19 10:25 -0700
 */
package com.thinkparity.ophelia.support.ui.avatar.main;

import com.thinkparity.ophelia.support.ui.avatar.AbstractAvatar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MainAvatar extends AbstractAvatar {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteProfileJButton;
    private javax.swing.JButton killJButton;
    private javax.swing.JButton quitJButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Create MainAvatar
     *
     */
    public MainAvatar() {
        super("/main/main");
        initComponents();
    }

    private void deleteProfileJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProfileJButtonActionPerformed
        getContext().newActionRunner("/data/delete").run();
    }//GEN-LAST:event_deleteProfileJButtonActionPerformed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        deleteProfileJButton = new javax.swing.JButton();
        killJButton = new javax.swing.JButton();
        quitJButton = new javax.swing.JButton();

        deleteProfileJButton.setText(java.util.ResourceBundle.getBundle("localization/UIMessages").getString("avatar.main.mainavatar.deleteprofilebuttontext"));
        deleteProfileJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProfileJButtonActionPerformed(evt);
            }
        });

        killJButton.setText(java.util.ResourceBundle.getBundle("localization/UIMessages").getString("avatar.main.mainavatar.killbuttontext"));
        killJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                killJButtonActionPerformed(evt);
            }
        });

        quitJButton.setText(java.util.ResourceBundle.getBundle("localization/UIMessages").getString("avatar.main.mainavatar.quitbuttontext"));
        quitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(killJButton)
                    .addComponent(deleteProfileJButton))
                .addContainerGap(175, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(235, Short.MAX_VALUE)
                .addComponent(quitJButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(killJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteProfileJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(quitJButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void killJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_killJButtonActionPerformed
        getContext().newActionRunner("/application/terminate").run();
    }//GEN-LAST:event_killJButtonActionPerformed

    private void quitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitJButtonActionPerformed
        getContext().newActionRunner("/main/quit").run();
    }//GEN-LAST:event_quitJButtonActionPerformed
}
