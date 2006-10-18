/*
 * Created On:  October 16, 2006, 10:44 AM
 */
package com.thinkparity.ophelia.browser.mode.demo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.filechooser.FileFilter;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.JOptionPaneUtil;
import com.thinkparity.ophelia.browser.Constants.Colors;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DemoManagerPanel extends AbstractJPanel {

    /** A <code>DemoManager</code>. */
    private DemoManager demoManager;

    /** A <code>DemoProvider</code>. */
    private DemoProvider demoProvider;

    /** The demo root <code>FileSystem</code>. */
    private FileSystem demoRoot;

    /** The demo root <code>JFileChooser</code>. */
    private JFileChooser jFileChooser;

    /** The scenario <code>DefaultListModel</code>. */
    private final DefaultListModel scenarioModel;

    /** The <code>Scenario</code> selected by the user. */
    private Scenario selectedScenario;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel demoRootJLabel;
    private javax.swing.JList scenarioJList;
    // End of variables declaration//GEN-END:variables

    /** Create DemoManagerPanel. */
    public DemoManagerPanel() {
        super();
        this.scenarioModel = new DefaultListModel();
        bindEnterKey("Initialize", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                initializeJButtonActionPerformed(e);
            }
        });
        bindEscapeKey("Exit", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                exitJButtonActionPerformed(e);
            }
        });
        initComponents();
    }

    @Override
    public Boolean isInputValid() {
        return !scenarioJList.isSelectionEmpty();
    }

    /**
     * Obtain the selected scenario.
     * 
     * @return A <code>Scenario</code>.
     */
    Scenario getSelectedScenario() {
        return selectedScenario;
    }

    /**
     * Set the scenario manager.
     * 
     * @param demoManager
     *            A <code>DemoManager</code>.
     */
    void setDemoManager(final DemoManager demoManager) {
        this.demoManager = demoManager;
    }

    /**
     * Set the scenario provider.
     * 
     * @param scenarioProvider
     *            A <code>ScenarioProvider</code>.
     */
    void setDemoProvider(final DemoProvider demoProvider) {
        this.demoProvider = demoProvider;
        reload();
    }

    private void browseJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseJButtonActionPerformed
        if(JFileChooser.APPROVE_OPTION == getJFileChooser().showOpenDialog(getParent())) {
            demoRoot = new FileSystem(jFileChooser.getSelectedFile());
            reload();
        }
    }//GEN-LAST:event_browseJButtonActionPerformed
    private void exitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitJButtonActionPerformed
        selectedScenario = null;
        disposeWindow();
    }//GEN-LAST:event_exitJButtonActionPerformed

    private void extractDefaultJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractDefaultJButtonActionPerformed
        if(JFileChooser.APPROVE_OPTION == getJFileChooser().showOpenDialog(getParent())) {
            final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
            final String confirmation = MessageFormat.format(
                    bundle.getString("DemoManagerPanel.extractConfirm"),
                    jFileChooser.getSelectedFile());
            if (JOptionPaneUtil.showConfirmationDialog(confirmation, null)) {
                demoManager.writeDemo(jFileChooser.getSelectedFile());
            } else {
                final File selectedFile = jFileChooser.getSelectedFile();
                if (0 == selectedFile.listFiles().length) {
                    Assert.assertTrue(selectedFile.delete(),
                            "Could not delete directory {0}.", selectedFile);
                }
            }
        }
    }//GEN-LAST:event_extractDefaultJButtonActionPerformed

    /**
     * Extract the selected scenario from the list.
     * 
     * @return A <code>Scenario</code>.
     */
    private Scenario extractSelectedScenario() {
        return (Scenario) scenarioJList.getSelectedValue();
    }

    private JFileChooser getJFileChooser() {
        if (null == jFileChooser) {
            jFileChooser = new JFileChooser();
            jFileChooser.setMultiSelectionEnabled(Boolean.FALSE);
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(final File f) {
                    return f.isDirectory();
                }
                @Override
                public String getDescription() {
                    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
                    return bundle.getString("DemoManagerPanel.filterDescription");
                }
            });
        }
        return jFileChooser;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton browseJButton;
        javax.swing.JLabel eaJLabel;
        javax.swing.JButton exitJButton;
        javax.swing.JButton extractDefaultJButton;
        javax.swing.JButton initializeJButton;
        javax.swing.JButton resetJButton;
        javax.swing.JScrollPane scenaioJScrollPane;

        eaJLabel = new javax.swing.JLabel();
        demoRootJLabel = new javax.swing.JLabel();
        browseJButton = new javax.swing.JButton();
        scenaioJScrollPane = new javax.swing.JScrollPane();
        scenarioJList = new javax.swing.JList();
        resetJButton = new javax.swing.JButton();
        extractDefaultJButton = new javax.swing.JButton();
        exitJButton = new javax.swing.JButton();
        initializeJButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        eaJLabel.setText(bundle.getString("DemoManagerPanel.eaJLabel")); // NOI18N
        eaJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        demoRootJLabel.setText("${DEMO_ROOT}");

        browseJButton.setText(bundle.getString("DemoManagerPanel.browseJButton")); // NOI18N
        browseJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseJButtonActionPerformed(evt);
            }
        });

        scenarioJList.setModel(scenarioModel);
        scenarioJList.setCellRenderer(new ScenarioListCellRenderer());
        scenaioJScrollPane.setViewportView(scenarioJList);

        resetJButton.setText(bundle.getString("DemoManagerPanel.resetJButton")); // NOI18N
        resetJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetJButtonActionPerformed(evt);
            }
        });

        extractDefaultJButton.setText(bundle.getString("DemoManagerPanel.extractJButton")); // NOI18N
        extractDefaultJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractDefaultJButtonActionPerformed(evt);
            }
        });

        exitJButton.setText(bundle.getString("DemoManagerPanel.exitJButton")); // NOI18N
        exitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJButtonActionPerformed(evt);
            }
        });

        initializeJButton.setText(bundle.getString("DemoManagerPanel.initializeJButton")); // NOI18N
        initializeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initializeJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(exitJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(initializeJButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(demoRootJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 318, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(browseJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(extractDefaultJButton)
                            .add(resetJButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scenaioJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 258, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(browseJButton)
                    .add(demoRootJLabel))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scenaioJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(initializeJButton)
                            .add(exitJButton)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(15, 15, 15)
                        .add(resetJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(extractDefaultJButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initializeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initializeJButtonActionPerformed
        if(isInputValid()) {
            selectedScenario = extractSelectedScenario();
            disposeWindow();
        }
    }//GEN-LAST:event_initializeJButtonActionPerformed

    /**
     * Reload the panel.
     *
     */
    private void reload() {
        reloadDemoRoot();
        reloadDemoScenarios();
    }

    private void reloadDemoRoot() {
        demoRootJLabel.setText("");
        if (null != demoRoot) {
            demoRootJLabel.setText(demoRoot.getRoot().getName());
        } else {
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
            demoRootJLabel.setText(bundle.getString("DemoManagerPanel.internalDemoRoot"));
        }
    }

    private void reloadDemoScenarios() {
        scenarioModel.clear();
        final List<Scenario> scenarios = null == demoRoot ?
                demoProvider.readDemo().getScenarios() :
                    demoProvider.readDemo(demoRoot).getScenarios();
        for (final Scenario scenario : scenarios) {
            scenarioModel.addElement(scenario);
        }
        if (0 < scenarioModel.size()) {
            scenarioJList.setSelectedIndex(0);
        }
    }

    private void resetJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetJButtonActionPerformed
        demoRoot = null;
        reload();
    }//GEN-LAST:event_resetJButtonActionPerformed

    /** A scenario list cell renderer. */
    private class ScenarioListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(final JList list,
                final Object value, final int index, final boolean isSelected,
                final boolean cellHasFocus) {
            setText(((Scenario) value).getDisplayName());

            if (isSelected) {
                setForeground(Colors.Browser.List.LIST_SELECTION_FG);
                setBackground(Colors.Browser.List.LIST_SELECTION_BG);
            } else {
                setForeground(Colors.Browser.List.LIST_FG);
                if (0 == index % 2) {
                    setBackground(Colors.Browser.List.LIST_EVEN_BG);
                } else {
                    setBackground(Colors.Browser.List.LIST_ODD_BG);
                }
            }

            return this;
        }
    }
}
