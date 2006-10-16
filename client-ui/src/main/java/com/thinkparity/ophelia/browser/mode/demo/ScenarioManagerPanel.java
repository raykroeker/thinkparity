/*
 * Created On:  October 16, 2006, 10:44 AM
 */
package com.thinkparity.ophelia.browser.mode.demo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.ophelia.browser.Constants.Colors;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ScenarioManagerPanel extends AbstractJPanel {
    
    /** The scenario <code>DefaultListModel</code>. */
    private final DefaultListModel scenarioModel;

    /** The <code>Scenario</code> selected by the user. */
    private Scenario selectedScenario;

    /** Create ScenarioManagerPanel. */
    public ScenarioManagerPanel() {
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
     * Set the list of scenarios.
     * 
     * @param scenarios
     *            A <code>List&lt;Scenario&gt;</code>.
     */
    void setScenarios(final List<Scenario> scenarios) {
        scenarioModel.clear();
        for (final Scenario scenario : scenarios) {
            scenarioModel.addElement(scenario);
        }
        if (0 < scenarioModel.size()) {
            scenarioJList.setSelectedIndex(0);
        }
    }

    private void exitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitJButtonActionPerformed
        selectedScenario = null;
        disposeWindow();
    }//GEN-LAST:event_exitJButtonActionPerformed

    /**
     * Extract the selected scenario from the list.
     * 
     * @return A <code>Scenario</code>.
     */
    private Scenario extractSelectedScenario() {
        return (Scenario) scenarioJList.getSelectedValue();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel eaJLabel;
        javax.swing.JButton exitJButton;
        javax.swing.JButton initializeJButton;
        javax.swing.JScrollPane scenaioJScrollPane;

        eaJLabel = new javax.swing.JLabel();
        scenaioJScrollPane = new javax.swing.JScrollPane();
        scenarioJList = new javax.swing.JList();
        initializeJButton = new javax.swing.JButton();
        exitJButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        eaJLabel.setText(bundle.getString("ScenarioManagerPanel.eaJLabel")); // NOI18N

        scenarioJList.setModel(scenarioModel);
        scenarioJList.setCellRenderer(new ScenarioListCellRenderer());
        scenaioJScrollPane.setViewportView(scenarioJList);

        initializeJButton.setText(bundle.getString("ScenarioManagerPanel.initializeJButton")); // NOI18N
        initializeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initializeJButtonActionPerformed(evt);
            }
        });

        exitJButton.setText(bundle.getString("ScenarioManagerPanel.exitJButton")); // NOI18N
        exitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(exitJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(initializeJButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scenaioJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 258, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scenaioJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(initializeJButton)
                    .add(exitJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initializeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initializeJButtonActionPerformed
        if(isInputValid()) {
            selectedScenario = extractSelectedScenario();
            disposeWindow();
        }
    }//GEN-LAST:event_initializeJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList scenarioJList;
    // End of variables declaration//GEN-END:variables

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
