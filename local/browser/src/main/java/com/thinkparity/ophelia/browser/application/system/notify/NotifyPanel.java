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

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
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

    /** A singleton list of notifications. */
    private static final List<Notification> NOTIFICATIONS;

    static {
        BACKGROUND = ImageIOUtil.read("Dialog_Background.png");
        CLOSE_ICON = ImageIOUtil.readIcon("Dialog_CloseButton.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("Dialog_CloseButtonRollover.png");
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
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_LEFT,
                0,
                getSize().height - Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getHeight(), this);
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_RIGHT,
                getSize().width - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getWidth(),
                getSize().height - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getHeight(), this);
    }

    /**
     * Reload the notifications.
     * 
     * @param notifications
     *            A <code>Notification</code> <code>List</code>.
     */
    public void display(final Notification notification) {
        NOTIFICATIONS.add(notification);
        notificationIndex = NOTIFICATIONS.size()-1;
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
        NOTIFICATIONS.clear();
        disposeWindow();
    }

    /**
     * Close one entry of the notify panel.
     * 
     * @param index
     *          The index to close.
     */
    private void closeNotifyPanel(final int index) {
        NOTIFICATIONS.remove(index);
        if (NOTIFICATIONS.size() == 0) {
            disposeWindow();
        } else {
            if (notificationIndex >= NOTIFICATIONS.size()) {
                notificationIndex = NOTIFICATIONS.size()-1;
            }
            reload();
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

        closeJButton = new javax.swing.JButton();
        logoJLabel = new javax.swing.JLabel();
        notificationTitleJPanel = new javax.swing.JPanel();
        notificationTitleJLabel = LabelFactory.createLink("",Fonts.DefaultFontBold);
        notificationTitleFillerJLabel = new javax.swing.JLabel();
        controlJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel fillerJLabel = new javax.swing.JLabel();

        setOpaque(false);
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

        logoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/thinkParityLogo.png")));
        logoJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        notificationTitleJPanel.setLayout(new java.awt.GridBagLayout());

        notificationTitleJPanel.setOpaque(false);
        notificationTitleJLabel.setText("!Title!");
        notificationTitleJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                notificationTitleJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        notificationTitleJPanel.add(notificationTitleJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        notificationTitleJPanel.add(notificationTitleFillerJLabel, gridBagConstraints);

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
        previousJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                previousJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        controlJPanel.add(previousJLabel, gridBagConstraints);

        countJLabel.setText("!count!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        controlJPanel.add(countJLabel, gridBagConstraints);

        nextJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("NotifyPanel.Next"));
        nextJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nextJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        controlJPanel.add(nextJLabel, gridBagConstraints);

        notificationJLabel.setText("!Notification text!");
        notificationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, notificationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, notificationTitleJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .add(closeJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, logoJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, controlJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
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
                .add(notificationTitleJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(notificationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(controlJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

    private void nextJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextJLabelMousePressed
        if (isNextEnabled()) {
            notificationIndex++;
            reload();
        }
    }//GEN-LAST:event_nextJLabelMousePressed

    private void notificationTitleJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_notificationTitleJLabelMousePressed
        NOTIFICATIONS.get(notificationIndex).invokeAction();
        closeNotifyPanel(notificationIndex);
    }//GEN-LAST:event_notificationTitleJLabelMousePressed

    private void previousJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousJLabelMousePressed
        if (isPreviousEnabled()) {
            notificationIndex--;
            reload();
        }
    }//GEN-LAST:event_previousJLabelMousePressed

    /**
     * Reload the notify panel.
     *
     */
    private void reload() {
        reloadCount();
        reloadNext();
        reloadPrevious();
        reloadNotificationTitle();
        reloadNotification();
    }

    /**
     * Reload the count label.
     *
     */
    private void reloadCount() {
        if (NOTIFICATIONS.size() > 1) {
            countJLabel.setText(MessageFormat.format("{0} of {1}",
                    notificationIndex + 1, NOTIFICATIONS.size()));
        } else {
            countJLabel.setText(" ");
        }
    }

    /**
     * Reload the next button.
     *
     */
    private void reloadNext() {
        nextJLabel.setVisible(isNextEnabled());
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
     * Reload the notififcation title text.
     *
     */
    private void reloadNotificationTitle() {
        if (notificationIndex >= 0 && notificationIndex < NOTIFICATIONS.size()) {
            notificationTitleJLabel.setText(NOTIFICATIONS.get(notificationIndex).getTitle());
        } else {
            notificationTitleJLabel.setText(" ");
        }
    }

    /**
     * Reload the previous button.
     *
     */
    private void reloadPrevious() {
        previousJLabel.setVisible(isPreviousEnabled());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeJButton;
    private javax.swing.JPanel controlJPanel;
    private final javax.swing.JLabel countJLabel = new javax.swing.JLabel();
    private javax.swing.JLabel logoJLabel;
    private final javax.swing.JLabel nextJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel notificationJLabel = new javax.swing.JLabel();
    private javax.swing.JLabel notificationTitleFillerJLabel;
    private javax.swing.JLabel notificationTitleJLabel;
    private javax.swing.JPanel notificationTitleJPanel;
    private final javax.swing.JLabel previousJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    // End of variables declaration//GEN-END:variables
}
