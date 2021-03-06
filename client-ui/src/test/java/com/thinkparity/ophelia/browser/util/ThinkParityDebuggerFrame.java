/*
 * Created on December 2, 2006, 9:14 AM
 */
package com.thinkparity.ophelia.browser.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.table.DefaultTableModel;

import com.thinkparity.codebase.swing.TableSorter;

import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI ThinkParity Debugger Frame<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ThinkParityDebuggerFrame extends OpheliaJFrame {

    /** The <code>ThinkParityDebuggerFrame</code>. */
    private static ThinkParityDebuggerFrame frame;

    /**
     * Display the debugger frame. If the frame is already visible it will be
     * brought to the front.
     * 
     */
    public static void display() {
        if (null == frame) {
            frame = new ThinkParityDebuggerFrame();
            frame.reload();
        }
        final Runnable displayRunnable;
        synchronized (frame) {
            if (frame.isVisible()) {
                displayRunnable = new Runnable() {
                    public void run() {
                        frame.toFront();
                    }
                };
            } else {
                displayRunnable = new Runnable() {
                    public void run() {
                        frame.setVisible(true);
                    }
                };
            }
            java.awt.EventQueue.invokeLater(displayRunnable);
        }
    }

    public static Boolean isDisplayed() {
        return null != frame && frame.isVisible();
    }

    public static void close() {
        if (isDisplayed()) {
            frame.dispose();
            frame = null;
        }
    }
    /** The system properties <code>DefaultListModel</code>. */
    private final DefaultTableModel systemPropertiesModel;

    /** The thinkparty properties <code>TableSorter</code>. */
    private final TableSorter systemPropertiesSorter;

    /** The thinkparty properties <code>DefaultTableModel</code>. */
    private final DefaultTableModel thinkParityPropertiesModel;

    /** The thinkparty properties <code>TableSorter</code>. */
    private final TableSorter thinkParityPropertiesSorter;

    /**
     * Create ThinkParityDebuggerFrame.
     *
     */
    public ThinkParityDebuggerFrame() {
        super("ThinkParityDebugger");
        this.systemPropertiesModel = new DefaultTableModel();
        this.systemPropertiesModel.addColumn("Name");
        this.systemPropertiesModel.addColumn("Value");
        this.systemPropertiesSorter = new TableSorter(systemPropertiesModel);
        this.thinkParityPropertiesModel = new DefaultTableModel();
        this.thinkParityPropertiesModel.addColumn("Name");
        this.thinkParityPropertiesModel.addColumn("Value");
        this.thinkParityPropertiesSorter = new TableSorter(thinkParityPropertiesModel);
        initComponents();
    }

    /**
     * Reload the debugger frame.
     *
     */
    public void reload() {
        reloadInspector();
        reloadSystemProperties();
        reloadThinkParityProperties();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton reloadSystemPropertiesJButton;
        javax.swing.ButtonGroup swingInspectorJButtonGroup;
        javax.swing.JPanel swingInspectorJPanel;
        javax.swing.JButton swingInspectorReloadJButton;
        javax.swing.JPanel systemPropertiesJPanel;
        javax.swing.JScrollPane systemPropertiesJScrollPane;
        javax.swing.JTable systemPropertiesJTable;
        javax.swing.JPanel thinkParityDebuggerJPanel;
        javax.swing.JTabbedPane thinkParityDebuggerJTabbedPane;
        javax.swing.JScrollPane thinkParityJScrollPane;
        javax.swing.JPanel thinkParityPropertiesJPanel;
        javax.swing.JTable thinkParityPropertiesJTable;
        javax.swing.JButton thinkParityPropertiesReloadJButton;

        swingInspectorJButtonGroup = new javax.swing.ButtonGroup();
        thinkParityDebuggerJPanel = new javax.swing.JPanel();
        thinkParityDebuggerJTabbedPane = new javax.swing.JTabbedPane();
        systemPropertiesJPanel = new javax.swing.JPanel();
        systemPropertiesJScrollPane = new javax.swing.JScrollPane();
        systemPropertiesJTable = new javax.swing.JTable();
        reloadSystemPropertiesJButton = new javax.swing.JButton();
        thinkParityPropertiesJPanel = new javax.swing.JPanel();
        thinkParityJScrollPane = new javax.swing.JScrollPane();
        thinkParityPropertiesJTable = new javax.swing.JTable();
        thinkParityPropertiesReloadJButton = new javax.swing.JButton();
        swingInspectorJPanel = new javax.swing.JPanel();
        swingInspectorReloadJButton = new javax.swing.JButton();

        setTitle("thinkParity Debugger");
        setName("thinkParityDebuggerJFrame");
        systemPropertiesJTable.setModel(systemPropertiesSorter);
        systemPropertiesSorter.setTableHeader(systemPropertiesJTable.getTableHeader());
        systemPropertiesJScrollPane.setViewportView(systemPropertiesJTable);

        reloadSystemPropertiesJButton.setText("Reload");
        reloadSystemPropertiesJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                reloadSystemPropertiesJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout systemPropertiesJPanelLayout = new org.jdesktop.layout.GroupLayout(systemPropertiesJPanel);
        systemPropertiesJPanel.setLayout(systemPropertiesJPanelLayout);
        systemPropertiesJPanelLayout.setHorizontalGroup(
            systemPropertiesJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, systemPropertiesJPanelLayout.createSequentialGroup()
                .addContainerGap(482, Short.MAX_VALUE)
                .add(reloadSystemPropertiesJButton)
                .addContainerGap())
            .add(systemPropertiesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
        );
        systemPropertiesJPanelLayout.setVerticalGroup(
            systemPropertiesJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, systemPropertiesJPanelLayout.createSequentialGroup()
                .add(systemPropertiesJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(reloadSystemPropertiesJButton)
                .addContainerGap())
        );
        thinkParityDebuggerJTabbedPane.addTab("System Properties", systemPropertiesJPanel);

        thinkParityPropertiesJTable.setModel(thinkParityPropertiesSorter);
        thinkParityJScrollPane.setViewportView(thinkParityPropertiesJTable);

        thinkParityPropertiesReloadJButton.setText("Reload");
        thinkParityPropertiesReloadJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                thinkParityPropertiesReloadJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout thinkParityPropertiesJPanelLayout = new org.jdesktop.layout.GroupLayout(thinkParityPropertiesJPanel);
        thinkParityPropertiesJPanel.setLayout(thinkParityPropertiesJPanelLayout);
        thinkParityPropertiesJPanelLayout.setHorizontalGroup(
            thinkParityPropertiesJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, thinkParityPropertiesJPanelLayout.createSequentialGroup()
                .addContainerGap(482, Short.MAX_VALUE)
                .add(thinkParityPropertiesReloadJButton)
                .addContainerGap())
            .add(thinkParityJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
        );
        thinkParityPropertiesJPanelLayout.setVerticalGroup(
            thinkParityPropertiesJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, thinkParityPropertiesJPanelLayout.createSequentialGroup()
                .add(thinkParityJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thinkParityPropertiesReloadJButton)
                .addContainerGap())
        );
        thinkParityDebuggerJTabbedPane.addTab("thinkParity Properties", thinkParityPropertiesJPanel);

        swingInspectorJButtonGroup.add(swingInspectorEnableJRadioButton);
        swingInspectorEnableJRadioButton.setText("Enable");
        swingInspectorEnableJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        swingInspectorEnableJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        swingInspectorEnableJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                swingInspectorEnableJRadioButtonActionPerformed(e);
            }
        });

        swingInspectorJButtonGroup.add(swingInspectorDisableJRadioButton);
        swingInspectorDisableJRadioButton.setSelected(true);
        swingInspectorDisableJRadioButton.setText("Disable");
        swingInspectorDisableJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        swingInspectorDisableJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        swingInspectorDisableJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                swingInspectorDisableJRadioButtonActionPerformed(e);
            }
        });

        swingInspectorReloadJButton.setText("Reload");
        swingInspectorReloadJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                swingInspectorReloadJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout swingInspectorJPanelLayout = new org.jdesktop.layout.GroupLayout(swingInspectorJPanel);
        swingInspectorJPanel.setLayout(swingInspectorJPanelLayout);
        swingInspectorJPanelLayout.setHorizontalGroup(
            swingInspectorJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, swingInspectorJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(swingInspectorEnableJRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(swingInspectorDisableJRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 342, Short.MAX_VALUE)
                .add(swingInspectorReloadJButton)
                .addContainerGap())
        );
        swingInspectorJPanelLayout.setVerticalGroup(
            swingInspectorJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, swingInspectorJPanelLayout.createSequentialGroup()
                .addContainerGap(411, Short.MAX_VALUE)
                .add(swingInspectorJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(swingInspectorReloadJButton)
                    .add(swingInspectorEnableJRadioButton)
                    .add(swingInspectorDisableJRadioButton))
                .addContainerGap())
        );
        thinkParityDebuggerJTabbedPane.addTab("Swing Inspector", swingInspectorJPanel);

        org.jdesktop.layout.GroupLayout thinkParityDebuggerJPanelLayout = new org.jdesktop.layout.GroupLayout(thinkParityDebuggerJPanel);
        thinkParityDebuggerJPanel.setLayout(thinkParityDebuggerJPanelLayout);
        thinkParityDebuggerJPanelLayout.setHorizontalGroup(
            thinkParityDebuggerJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thinkParityDebuggerJTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
        );
        thinkParityDebuggerJPanelLayout.setVerticalGroup(
            thinkParityDebuggerJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thinkParityDebuggerJTabbedPane)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thinkParityDebuggerJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thinkParityDebuggerJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reloadInspector() {
        if (swingInspectorEnableJRadioButton.isSelected()) {
        }
    }

    private void reloadSystemProperties() {
        final int rowCount = systemPropertiesModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            systemPropertiesModel.removeRow(i);
        }
        final List<String> keys = new ArrayList<String>();
        final Properties systemProperties = System.getProperties();
        for (final Object key : systemProperties.keySet()) {
            keys.add((String) key);
        }
        Collections.sort(keys);
        for (final String key : keys) {
            systemPropertiesModel.addRow(new Object[] { key,
                    systemProperties.getProperty(key) });
        }
    }

    private void reloadThinkParityProperties() {
        final int rowCount = thinkParityPropertiesModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            thinkParityPropertiesModel.removeRow(i);
        }
        final List<String> keys = new ArrayList<String>();
        final Properties systemProperties = System.getProperties();
        for (final Object key : systemProperties.keySet()) {
            keys.add((String) key);
        }
        Collections.sort(keys);
        for (final String key : keys) {
            if (key.startsWith("thinkparity") || key.startsWith("parity")) {
                thinkParityPropertiesModel.addRow(new Object[] { key,
                        systemProperties.getProperty(key) });
            }
        }
    }

    private void swingInspectorReloadJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_swingInspectorReloadJButtonActionPerformed
    }//GEN-LAST:event_swingInspectorReloadJButtonActionPerformed

    private void swingInspectorEnableJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_swingInspectorEnableJRadioButtonActionPerformed
    }//GEN-LAST:event_swingInspectorEnableJRadioButtonActionPerformed

    private void swingInspectorDisableJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_swingInspectorDisableJRadioButtonActionPerformed
    }//GEN-LAST:event_swingInspectorDisableJRadioButtonActionPerformed

    private void thinkParityPropertiesReloadJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thinkParityPropertiesReloadJButtonActionPerformed
    }//GEN-LAST:event_thinkParityPropertiesReloadJButtonActionPerformed

    private void reloadSystemPropertiesJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadSystemPropertiesJButtonActionPerformed
    }//GEN-LAST:event_reloadSystemPropertiesJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JRadioButton swingInspectorDisableJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JRadioButton swingInspectorEnableJRadioButton = new javax.swing.JRadioButton();
    // End of variables declaration//GEN-END:variables
}
