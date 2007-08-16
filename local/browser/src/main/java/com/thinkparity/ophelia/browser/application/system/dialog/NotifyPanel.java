/*
 * Created On: November 22, 2006, 10:48 AM
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotifyPanel extends SystemPanel {

    /** Close label icon. */
    private static final Icon CLOSE_ICON;

    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    /** Maximum title width. */
    private static final int MAX_TITLE_WIDTH;

    /** A singleton list of notifications. */
    private static final List<Notification> NOTIFICATIONS;

    static {
        CLOSE_ICON = ImageIOUtil.readIcon("Dialog_CloseButton.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("Dialog_CloseButtonRollover.png");
        MAX_TITLE_WIDTH = 185;
        NOTIFICATIONS = new Vector<Notification>();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel contentLine1JLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contentLine2JLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel countJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel headingLine1JLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel headingLine2JLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel nextJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel previousJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel titleLinkJLabel = LabelFactory.createLink("",Fonts.DefaultFontBold);
    private javax.swing.JLabel titleTextJLabel;
    // End of variables declaration//GEN-END:variables

    /** The current notification index. */
    private int notificationIndex;

    /**
     * Create NotifyPanel.
     * 
     */
    NotifyPanel() {
        super();
        initComponents();
        this.notificationIndex = 0;
        bindEscapeKey("Close", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                closeNotifyPanel();
            }
        });
    }

    /**
     * Close notifications.
     * 
     * @param notificationId
     *            A notification id or group id <code>String</code>.
     */
    void close(final String notificationId) {
        int index;
        while (-1 < (index = indexOf(notificationId))) {
            logger.logInfo("Closing notification {0}.", notificationId);
            closeNotifyPanel(index);
        }
    }

    /**
     * Display a notification.
     * 
     * @param notification
     *            A <code>Notification</code>.
     */
    void display(final Notification notification) {
        NOTIFICATIONS.add(notification);
        notificationIndex = NOTIFICATIONS.size()-1;
        reload();
    }

    private void closeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeJButtonActionPerformed
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
        closeNotifyPanel();
    }//GEN-LAST:event_closeJButtonActionPerformed

    private void closeJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseEntered
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
    }//GEN-LAST:event_closeJButtonMouseEntered

    private void closeJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseExited
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
    }//GEN-LAST:event_closeJButtonMouseExited

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
                notificationIndex = NOTIFICATIONS.size() - 1;
            }
            reload();
        }
    }

    /**
     * Obtain the index of the notification within the list.
     * 
     * @param notificationId
     *            A notification id or group id <code>String</code>.
     * @return An <code>int</code> index or -1 if the notification does not
     *         exist.
     */
    private int indexOf(final String notificationId) {
        for (int i = 0; i < NOTIFICATIONS.size(); i++) {
            if (NOTIFICATIONS.get(i).getId().equals(notificationId)) {
                return i;
            } else if (NOTIFICATIONS.get(i).getGroupId().equals(notificationId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JButton closeJButton = new javax.swing.JButton();
        final javax.swing.JLabel logoJLabel = new javax.swing.JLabel();
        final javax.swing.JPanel titleJPanel = new javax.swing.JPanel();
        titleTextJLabel = LabelFactory.create(Fonts.DefaultFontBold);
        final javax.swing.JLabel titleFillJLabel = new javax.swing.JLabel();
        final javax.swing.JPanel controlJPanel = new javax.swing.JPanel();
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

        logoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        logoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ThinkParity16x16.png")));
        logoJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        titleJPanel.setLayout(new java.awt.GridBagLayout());

        titleJPanel.setOpaque(false);
        titleLinkJLabel.setText("!Title Link!");
        titleLinkJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                titleLinkJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        titleJPanel.add(titleLinkJLabel, gridBagConstraints);

        titleTextJLabel.setText("!Title Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        titleJPanel.add(titleTextJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        titleJPanel.add(titleFillJLabel, gridBagConstraints);

        headingLine1JLabel.setText("!Head 1!");

        contentLine1JLabel.setText("!Notification text 1!");
        contentLine1JLabel.setPreferredSize(new java.awt.Dimension(170, 14));

        headingLine2JLabel.setText("!Head 2!");

        contentLine2JLabel.setText("!Notification text 2!");
        contentLine2JLabel.setPreferredSize(new java.awt.Dimension(170, 14));

        controlJPanel.setLayout(new java.awt.GridBagLayout());

        controlJPanel.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        controlJPanel.add(fillerJLabel, gridBagConstraints);

        previousJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LinkPrevious.png")));
        previousJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.NotifyPanel.Previous"));
        previousJLabel.setIconTextGap(3);
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

        nextJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LinkNext.png")));
        nextJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.NotifyPanel.Next"));
        nextJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        nextJLabel.setIconTextGap(2);
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(logoJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(controlJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(headingLine2JLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(headingLine1JLabel))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(contentLine2JLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, contentLine1JLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)))
                            .add(layout.createSequentialGroup()
                                .add(titleJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(closeJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, logoJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(closeJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(titleJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(9, 9, 9)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(headingLine1JLabel)
                            .add(contentLine1JLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(headingLine2JLabel)
                            .add(contentLine2JLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(controlJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
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

    private boolean isNotification() {
        return notificationIndex >= 0
                && notificationIndex < NOTIFICATIONS.size();
    }

    /**
     * Determine if there is text in the first line of the notification.
     * 
     * @return true if there is text in the first line of the notification.
     */
    private boolean isNotificationLine1() {
        return (notificationIndex >= 0 && notificationIndex < NOTIFICATIONS.size() &&
                NOTIFICATIONS.get(notificationIndex).getNumberLines() > 0);
    }

    /**
     * Determine if there is text in the second line of the notification.
     * 
     * @return true if there is text in the second line of the notification.
     */
    private boolean isNotificationLine2() {
        return (notificationIndex >= 0 && notificationIndex < NOTIFICATIONS.size() &&
                NOTIFICATIONS.get(notificationIndex).getNumberLines() > 1);
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
        reloadTitle();
        reloadNotificationHeadingLine1();
        reloadNotificationContentLine1();
        reloadNotificationHeadingLine2();
        reloadNotificationContentLine2();
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
     * Reload the content for the first line of the notification.
     */
    private void reloadNotificationContentLine1() {
        Assert.assertNotNull("Null graphics in notification panel.",
                contentLine1JLabel.getGraphics());
        if (isNotificationLine1()) {
            final String clippedContent = SwingUtil.limitWidthWithEllipsis(
                    NOTIFICATIONS.get(notificationIndex).getContentLine1(),
                    contentLine1JLabel.getPreferredSize().width,
                    contentLine1JLabel.getGraphics());
            contentLine1JLabel.setText(clippedContent);
        } else {
            contentLine1JLabel.setText(" ");
        }
    }
    /**
     * Reload the content for the second line of the notification.
     */
    private void reloadNotificationContentLine2() {
        Assert.assertNotNull("Null graphics in notification panel.",
                contentLine2JLabel.getGraphics());
        if (isNotificationLine2()) {
            final String clippedContent = SwingUtil.limitWidthWithEllipsis(
                    NOTIFICATIONS.get(notificationIndex).getContentLine2(),
                    contentLine2JLabel.getPreferredSize().width,
                    contentLine2JLabel.getGraphics());
            contentLine2JLabel.setText(clippedContent);
        } else {
            contentLine2JLabel.setText(" ");
        }
    }
    /**
     * Reload the heading for the first line of the notification.
     */
    private void reloadNotificationHeadingLine1() {
        if (isNotificationLine1()) {
            headingLine1JLabel.setText(NOTIFICATIONS.get(notificationIndex).getHeadingLine1());
        } else {
            headingLine1JLabel.setText(" ");
        }
    }
    /**
     * Reload the heading for the second line of the notification.
     */
    private void reloadNotificationHeadingLine2() {
        if (isNotificationLine2()) {
            headingLine2JLabel.setText(NOTIFICATIONS.get(notificationIndex).getHeadingLine2());
        } else {
            headingLine2JLabel.setText(" ");
        }
    }
    /**
     * Reload the previous button.
     *
     */
    private void reloadPrevious() {
        previousJLabel.setVisible(isPreviousEnabled());
    }
    /**
     * Reload the notififcation title text.
     *
     */
    private void reloadTitle() {
        titleLinkJLabel.setText("");
        titleTextJLabel.setText("");
        if (isNotification()) {
            final String textTitle = NOTIFICATIONS.get(notificationIndex).getTextTitle();
            if (null == textTitle) {
                final String linkTitle = NOTIFICATIONS.get(notificationIndex).getLinkTitle();
                final String clippedLinkTitle = SwingUtil.limitWidthWithEllipsis(
                        linkTitle, MAX_TITLE_WIDTH,
                        titleLinkJLabel.getGraphics());
                titleLinkJLabel.setText(clippedLinkTitle);
            } else {
                final String clippedTextTitle = SwingUtil.limitWidthWithEllipsis(
                        textTitle, MAX_TITLE_WIDTH,
                        titleTextJLabel.getGraphics());
                titleTextJLabel.setText(clippedTextTitle);
            }
        }
    }
    private void titleLinkJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleLinkJLabelMousePressed
        NOTIFICATIONS.get(notificationIndex).invokeAction();
        closeNotifyPanel(notificationIndex);
    }//GEN-LAST:event_titleLinkJLabelMousePressed
}
