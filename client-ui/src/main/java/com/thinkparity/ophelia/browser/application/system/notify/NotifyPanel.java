/*
 * Created On: November 22, 2006, 10:48 AM
 */
package com.thinkparity.ophelia.browser.application.system.notify;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotifyPanel extends AbstractJPanel {

    /** A background image. */
    private static final BufferedImage BACKGROUND;

    /** Close label icon. */
    private static final Icon CLOSE_ICON;

    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    /** A set of next button <code>ImageIcon</code>s. */
    private static final ImageIcon[] NEXT_ICONS;

    /** A singleton list of notifications. */
    private static final List<Notification> NOTIFICATIONS;

    /** A set of previous button <code>ImageIcon</code>s. */
    private static final ImageIcon[] PREVIOUS_ICONS;

    static {
        BACKGROUND = ImageIOUtil.read("Dialog_Background.png");
        CLOSE_ICON = ImageIOUtil.readIcon("Dialog_CloseButton.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("Dialog_CloseButtonRollover.png");
        PREVIOUS_ICONS = new ImageIcon[] {
                ImageIOUtil.readIcon("ScrollLeftButton.png"),
                ImageIOUtil.readIcon("ScrollLeftButtonRollover.png") };
        NEXT_ICONS = new ImageIcon[] {
                ImageIOUtil.readIcon("ScrollRightButton.png"),
                ImageIOUtil.readIcon("ScrollRightButtonRollover.png") };
        NOTIFICATIONS = new Vector<Notification>();
    }

    /** The current notification index. */
    private int notificationIndex;

    /** The buffer to use when displaying the notification. */
    private final StringBuffer notificationText;

    private Image scaledBackground;

    private Dimension scaledSize;

    /**
     * Create NotifyPanel.
     * 
     */
    NotifyPanel() {
        super();
        initComponents();
        this.notificationIndex = 0;
        this.notificationText = new StringBuffer();
        addMoveListener(notificationJLabel);
        bindEscapeKey("Close", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                closeNotifyPanel();
            }
        });
    }
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if(null == scaledBackground || null == scaledSize
                || !getSize().equals(scaledSize)) {
            scaledSize = getSize();
            scaledBackground = BACKGROUND.getScaledInstance(scaledSize.width,
                    scaledSize.height, Image.SCALE_SMOOTH);
        }
        g.drawImage(scaledBackground, 0, 0, null);
        
        // These images help to make the rounded corners look good.
        g.drawImage(Images.BrowserTitle.BROWSER_TOP_LEFT_INNER,
                0,
                0,
                Images.BrowserTitle.BROWSER_TOP_LEFT_INNER.getWidth(),
                Images.BrowserTitle.BROWSER_TOP_LEFT_INNER.getHeight(), this);
        g.drawImage(Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER,
                getSize().width - Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getWidth(),
                0,
                Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getWidth(),
                Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getHeight(), this);
        g.drawImage(Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER,
                0,
                getSize().height - Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER.getHeight(),
                Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER.getWidth(),
                Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER.getHeight(), this);
        g.drawImage(Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER,
                getSize().width - Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getWidth(),
                getSize().height - Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getHeight(),
                Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getWidth(),
                Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getHeight(), this);
    }

    /**
     * Reload the notifications.
     * 
     * @param notifications
     *            A <code>Notification</code> <code>List</code>.
     */
    void display(final Notification notification) {
        NOTIFICATIONS.add(notification);
        reload();
    }

    private void closeJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseEntered
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
    }//GEN-LAST:event_closeJButtonMouseEntered

    private void closeJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseExited
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
    }//GEN-LAST:event_closeJButtonMouseExited

    private void closeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeJButtonActionPerformed
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
        closeNotifyPanel();
    }//GEN-LAST:event_closeJButtonActionPerformed

    /**
     * Close the notify panel.
     *
     */
    private void closeNotifyPanel() {
        NOTIFICATIONS.remove(notificationIndex);
        reload();
        if (!isNextEnabled()) {
            disposeWindow();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        oldcontrolJPanel = new javax.swing.JPanel();
        oldcountJLabel = new javax.swing.JLabel();
        oldnextJLabel = new javax.swing.JLabel();
        oldpreviousJLabel = new javax.swing.JLabel();
        controlJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel fillerJLabel = new javax.swing.JLabel();
        closeJButton = new javax.swing.JButton();
        logoJLabel = new javax.swing.JLabel();

        oldcontrolJPanel.setOpaque(false);
        oldcountJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        oldcountJLabel.setText(" ");

        oldnextJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ScrollRightButton.png")));
        oldnextJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                oldnextJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                oldnextJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                oldnextJLabelMousePressed(evt);
            }
        });

        oldpreviousJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ScrollLeftButton.png")));
        oldpreviousJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                oldpreviousJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                oldpreviousJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                oldpreviousJLabelMousePressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout oldcontrolJPanelLayout = new org.jdesktop.layout.GroupLayout(oldcontrolJPanel);
        oldcontrolJPanel.setLayout(oldcontrolJPanelLayout);
        oldcontrolJPanelLayout.setHorizontalGroup(
            oldcontrolJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 17, Short.MAX_VALUE)
            .add(oldnextJLabel)
            .add(oldpreviousJLabel)
            .add(oldcountJLabel)
        );
        oldcontrolJPanelLayout.setVerticalGroup(
            oldcontrolJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 73, Short.MAX_VALUE)
            .add(oldnextJLabel)
            .add(oldpreviousJLabel)
            .add(oldcountJLabel)
        );

        setOpaque(false);
        controlJPanel.setLayout(new java.awt.GridBagLayout());

        controlJPanel.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        controlJPanel.add(fillerJLabel, gridBagConstraints);

        previousJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("NotifyPanel.Previous"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        controlJPanel.add(previousJLabel, gridBagConstraints);

        countJLLabel.setText("!count!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        controlJPanel.add(countJLLabel, gridBagConstraints);

        nextJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("NotifyPanel.Next"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        controlJPanel.add(nextJLabel, gridBagConstraints);

        closeJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Dialog_CloseButton.png")));
        closeJButton.setBorderPainted(false);
        closeJButton.setContentAreaFilled(false);
        closeJButton.setFocusPainted(false);
        closeJButton.setFocusable(false);
        closeJButton.setMaximumSize(new java.awt.Dimension(14, 14));
        closeJButton.setMinimumSize(new java.awt.Dimension(14, 14));
        closeJButton.setPreferredSize(new java.awt.Dimension(14, 14));
        closeJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeJButtonMouseExited(evt);
            }
        });
        closeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeJButtonActionPerformed(evt);
            }
        });

        notificationJLabel.setText("!Notification text!");
        notificationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        logoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/thinkParityLogo.png")));
        logoJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        logoJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logoJLabelMousePressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, closeJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(notificationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .add(controlJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .add(logoJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(closeJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(logoJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(notificationJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(controlJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if the next button is enabled.
     * 
     * @return True if the next button is enabled.
     */
    private boolean isNextEnabled() {
        // if there is more than one notification and the current index is
        // not the last spot; enable the button
        if (NOTIFICATIONS.size() > 1) {
            if (notificationIndex < NOTIFICATIONS.size() - 1) {
                return true;
            }
        }
        return false;
    }
    /**
     * Determine if the previous button should be enabled.
     * 
     * @return True if it is enabled.
     */
    private boolean isPreviousEnabled() {
        // if there is more than one notification and the current index is
        // not the first spot; enable the button
        if (NOTIFICATIONS.size() > 1) {
            if (notificationIndex > 0) {
                return true;
            }
        }
        return false;
    }

    private void logoJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoJLabelMousePressed
        NOTIFICATIONS.get(notificationIndex).invokeAction();
    }//GEN-LAST:event_logoJLabelMousePressed

    private void oldnextJLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oldnextJLabelMouseEntered
        if (isNextEnabled()) {
            ((JLabel) evt.getSource()).setIcon(NEXT_ICONS[1]);
        }
    }//GEN-LAST:event_oldnextJLabelMouseEntered
    private void oldnextJLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oldnextJLabelMouseExited
        if (isNextEnabled()) {
            ((JLabel) evt.getSource()).setIcon(NEXT_ICONS[0]);
        }
    }//GEN-LAST:event_oldnextJLabelMouseExited

    private void oldnextJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oldnextJLabelMousePressed
        if (isNextEnabled()) {
            notificationIndex++;
            reload();
        }
    }//GEN-LAST:event_oldnextJLabelMousePressed

    private void oldpreviousJLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oldpreviousJLabelMouseEntered
        if (isPreviousEnabled()) {
            ((JLabel) evt.getSource()).setIcon(PREVIOUS_ICONS[1]);
        }
    }//GEN-LAST:event_oldpreviousJLabelMouseEntered

    private void oldpreviousJLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oldpreviousJLabelMouseExited
        if (isPreviousEnabled()) {
            ((JLabel) evt.getSource()).setIcon(PREVIOUS_ICONS[0]);
        }
    }//GEN-LAST:event_oldpreviousJLabelMouseExited

    private void oldpreviousJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oldpreviousJLabelMousePressed
        if (isPreviousEnabled()) {
            notificationIndex--;
            reload();
        }
    }//GEN-LAST:event_oldpreviousJLabelMousePressed

    /**
     * Reload the notify panel.
     *
     */
    private void reload() {
        reloadCount();
        reloadNext();
        reloadPrevious();
        reloadNotification();
    }
    /**
     * Reload the count label.
     *
     */
    private void reloadCount() {
/*        if (NOTIFICATIONS.size() > 0)
            countJLabel.setText(MessageFormat.format("{0} of {1}",
                    notificationIndex + 1, NOTIFICATIONS.size()));
        else
            countJLabel.setText(" ");*/
    }
    /**
     * Reload the next button.
     *
     */
    private void reloadNext() {
        if (isNextEnabled()) {
            nextJLabel.setEnabled(true);
        } else {
            nextJLabel.setEnabled(false);
        }
    }
    /**
     * Reload the notififcation text.
     *
     */
    private void reloadNotification() {
        if (notificationIndex >= 0 && notificationIndex < NOTIFICATIONS.size()) {
            notificationText.setLength(0);
            notificationText.append("<html>")
                .append(NOTIFICATIONS.get(notificationIndex).getMessage())
                .append("</html>");
            notificationJLabel.setText(notificationText.toString());
        } else {
            notificationJLabel.setText(" ");
        }
    }
    /**
     * Reload the previous button.
     *
     */
    private void reloadPrevious() {
        if (isPreviousEnabled()) {
            previousJLabel.setEnabled(true);
        } else {
            previousJLabel.setEnabled(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeJButton;
    private javax.swing.JPanel controlJPanel;
    private final javax.swing.JLabel countJLLabel = new javax.swing.JLabel();
    private javax.swing.JLabel logoJLabel;
    private final javax.swing.JLabel nextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel notificationJLabel = new javax.swing.JLabel();
    private javax.swing.JPanel oldcontrolJPanel;
    private javax.swing.JLabel oldcountJLabel;
    private javax.swing.JLabel oldnextJLabel;
    private javax.swing.JLabel oldpreviousJLabel;
    private final javax.swing.JLabel previousJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables
}
