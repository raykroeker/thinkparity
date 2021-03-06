/*
 * AdminWindow.java
 *
 * Created on October 20, 2006, 2:46 PM
 */

package com.thinkparity.cordelia.ui.application.admin;

import com.thinkparity.cordelia.ui.util.swing.CordeliaJFrame;

/**
 *
 * @author  raymond
 */
public class AdminWindow extends CordeliaJFrame {
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.cordelia.ui.application.admin.AdminContentPanel adminContentPanel1;
    private com.thinkparity.cordelia.ui.application.admin.AdminStatusPanel adminStatusPanel;
    private com.thinkparity.cordelia.ui.application.admin.AdminTitlePanel adminTitlePanel1;
    // End of variables declaration//GEN-END:variables

    /** Creates new form AdminWindow */
    public AdminWindow() {
        initComponents();
    }

    public void open() {
        reopen();       
    }

    public void reopen() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
                applyRenderingHints();
                debug();
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        adminStatusPanel = new com.thinkparity.cordelia.ui.application.admin.AdminStatusPanel();
        adminTitlePanel1 = new com.thinkparity.cordelia.ui.application.admin.AdminTitlePanel();
        adminContentPanel1 = new com.thinkparity.cordelia.ui.application.admin.AdminContentPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout adminTitlePanel1Layout = new org.jdesktop.layout.GroupLayout(adminTitlePanel1);
        adminTitlePanel1.setLayout(adminTitlePanel1Layout);
        adminTitlePanel1Layout.setHorizontalGroup(
            adminTitlePanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 499, Short.MAX_VALUE)
        );
        adminTitlePanel1Layout.setVerticalGroup(
            adminTitlePanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 50, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(adminStatusPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
            .add(adminTitlePanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(adminContentPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(adminTitlePanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(adminContentPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(adminStatusPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
}
