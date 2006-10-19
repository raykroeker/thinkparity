/*
 * ScenarioManagerWindow.java
 *
 * Created on October 16, 2006, 10:43 AM
 */

package com.thinkparity.ophelia.browser.mode.demo;

import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DemoManagerWindow extends OpheliaJFrame {
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.ophelia.browser.mode.demo.DemoManagerPanel demoManagerPanel;
    // End of variables declaration//GEN-END:variables

    /** Create ScenarioManagerWindow. */
    public DemoManagerWindow() {
        super(null);
        initComponents();
    }

    /**
     * Obtain the selected scenario.
     * 
     * @return A <code>Scenario</code>.
     */
    Scenario getSelectedScenario() {
        return demoManagerPanel.getSelectedScenario();
    }

    /**
     * Set the scenario provider.
     * 
     * @param scenarioProvider
     *            A <code>ScenarioProvider</code>.
     */
    void setDemoManager(final DemoManager demoManager) {
        demoManagerPanel.setDemoManager(demoManager);
    }

    /**
     * Set the scenario provider.
     * 
     * @param scenarioProvider
     *            A <code>ScenarioProvider</code>.
     */
    void setDemoProvider(final DemoProvider demoProvider) {
        demoManagerPanel.setDemoProvider(demoProvider);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        demoManagerPanel = new com.thinkparity.ophelia.browser.mode.demo.DemoManagerPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(demoManagerPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(demoManagerPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
}
