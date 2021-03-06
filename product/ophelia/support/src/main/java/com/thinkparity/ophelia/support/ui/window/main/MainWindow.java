/*
 * MainWindow.java
 *
 * Created on November 19, 2007, 10:24 AM
 */

package com.thinkparity.ophelia.support.ui.window.main;

import com.thinkparity.ophelia.support.ui.avatar.main.MainAvatar;
import com.thinkparity.ophelia.support.ui.window.AbstractWindow;

/**
 * <b>Title:</b>thinkParity Ophelia Support Main Window<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MainWindow extends AbstractWindow {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final com.thinkparity.ophelia.support.ui.avatar.main.MainAvatar mainAvatar = (MainAvatar) getContext().lookupAvatar("/main/main");
    // End of variables declaration//GEN-END:variables

    /**
     * Create MainWindow
     *
     */
    public MainWindow() {
        super("/main/main");
        initComponents();
    }
    
    /**
     * @see com.thinkparity.ophelia.support.ui.window.Window#close()
     *
     */
    @Override
    public void close() {
        super.dispose();
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.window.Window#close()
     *
     */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            mainAvatar.reload();
        }
        super.setVisible(visible);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(java.util.ResourceBundle.getBundle("localization/UIMessages").getString("window.main.mainwindow.title"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainAvatar, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainAvatar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
}
