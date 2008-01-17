/*
 * NetworkAvatar.java
 *
 * Created on December 12, 2007, 10:52 AM
 */

package com.thinkparity.ophelia.support.ui.avatar.network;

import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.network.ReloadTab;
import com.thinkparity.ophelia.support.ui.action.network.Test;
import com.thinkparity.ophelia.support.ui.avatar.AbstractAvatar;
import com.thinkparity.ophelia.support.ui.avatar.render.ProxyJListCellRenderer;
import com.thinkparity.ophelia.support.util.network.NetworkTestResult;

/**
 *
 * @author  raymond
 */
public class NetworkAvatar extends AbstractAvatar {
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JScrollPane outputJScrollPane;
    private javax.swing.JTextArea outputJTextArea;
    private javax.swing.JList proxyListJList;
    private javax.swing.JButton testJButton;
    private javax.swing.JTextField uriJTextField;
    // End of variables declaration//GEN-END:variables

    /** A dirty flag. */
    private boolean dirty;
    
    /** A list model for the proxy list. */
    private final DefaultListModel proxyJListModel;

    /** A list selection model for the proxy list. */
    private final DefaultListSelectionModel proxyJListSelectionModel;

    /** A list of proxies to display. */
    private final List<Proxy> proxyList;

    /** A network test result. */
    private NetworkTestResult testResult;

    /** A uri based upon which the proxy list is generated. */
    private URI uri;

    /**
     * Create NetworkAvatar.
     *
     */
    public NetworkAvatar() {
        super("/network/network");
        this.proxyJListModel = new DefaultListModel();
        this.proxyJListSelectionModel = new DefaultListSelectionModel();
        this.proxyJListSelectionModel.addListSelectionListener(new ListSelectionListener() {
            /**
             * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
             *
             */
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                reloadTestJButton();
            }
        });
        initComponents();
        this.uri = null;
        this.proxyList = new ArrayList<Proxy>();
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.avatar.AbstractAvatar#reload()
     *
     */
    @Override
    public void reload() {
        uriJTextField.setText(null);
        if (null != uri) {
            uriJTextField.setText(uri.toString());
        }
        final Proxy proxy = (Proxy) proxyListJList.getSelectedValue();
        proxyJListModel.clear();
        for (int i = 0; i < proxyList.size(); i++) {
            proxyJListModel.add(i, proxyList.get(i));
        }
        if (null == proxy) {
            if (0 < proxyJListModel.size()) {
                proxyListJList.setSelectedIndex(0);
            }
        } else {
            proxyListJList.setSelectedValue(proxy, true);
        }
        outputJTextArea.setText(null == testResult ? outputJTextArea.getText() : testResult.getText());
        outputJTextArea.setCaretPosition(0);
        dirty = false;
        reloadTestJButton();
    }

    /**
     * Set the proxy list.
     * 
     * @param proxyList
     *            A <code>List<Proxy></code>.
     */
    public void setProxyList(final List<Proxy> proxyList) {
        this.proxyList.clear();
        this.proxyList.addAll(proxyList);
    }

    /**
     * Set the test result.
     * 
     * @param testResult
     *            A <code>NetworkTestResult</code>.
     */
    public void setTestResult(final NetworkTestResult testResult) {
        this.testResult = testResult;
    }

    /**
     * Set the uri.
     * 
     * @param uri
     *            A <code>URI</code>.
     */
    public void setURI(final URI uri) {
        this.uri = uri;
    }

    private Input buildReloadInput() {
        final Input input = new Input(1);
        input.set(ReloadTab.InputKey.URI, extractURI());
        return input;
    }

    private Input buildTestInput() {
        final Input input = new Input(2);
        input.set(Test.InputKey.URI, extractURI());
        input.set(Test.InputKey.PROXY, extractProxy());
        return input;
    }

    private Proxy extractProxy() {
        return (Proxy) proxyListJList.getSelectedValue();
    }

    private URI extractURI() {
        return URI.create(uriJTextField.getText());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        javax.swing.JLabel uriJLabel = new javax.swing.JLabel();
        uriJTextField = new javax.swing.JTextField();
        javax.swing.JScrollPane proxyListJScrollPane = new javax.swing.JScrollPane();
        proxyListJList = new javax.swing.JList();
        javax.swing.JButton reloadJButton = new javax.swing.JButton();
        testJButton = new javax.swing.JButton();
        outputJScrollPane = new javax.swing.JScrollPane();
        outputJTextArea = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/UIMessages"); // NOI18N
        uriJLabel.setText(bundle.getString("avatar.network.networkavatar.urilabeltext")); // NOI18N

        uriJTextField.setText("!URI!");
        uriJTextField.getDocument().addDocumentListener(new DocumentListener() {

            /**
            * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
            *
            */
            @Override
            public void changedUpdate(final DocumentEvent e) {
                setDirty(true);
                reloadTestJButton();
            }

            /**
            * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
            *
            */
            @Override
            public void insertUpdate(final DocumentEvent e) {
                setDirty(true);
                reloadTestJButton();
            }

            /**
            * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
            *
            */
            @Override
            public void removeUpdate(final DocumentEvent e) {
                setDirty(true);
                reloadTestJButton();
            }
        });
        uriJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uriJTextFieldActionPerformed(evt);
            }
        });

        proxyListJList.setModel(proxyJListModel);
        proxyListJList.setSelectionModel(proxyJListSelectionModel);
        proxyListJList.setCellRenderer(new ProxyJListCellRenderer());
        proxyListJScrollPane.setViewportView(proxyListJList);

        reloadJButton.setText(bundle.getString("avatar.network.networkavatar.reloadbuttontext")); // NOI18N
        reloadJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadJButtonActionPerformed(evt);
            }
        });

        testJButton.setText(bundle.getString("avatar.network.networkavatar.testbuttontext")); // NOI18N
        testJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testJButtonActionPerformed(evt);
            }
        });

        outputJScrollPane.setEnabled(false);

        outputJTextArea.setColumns(20);
        outputJTextArea.setRows(5);
        outputJScrollPane.setViewportView(outputJTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(proxyListJScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(uriJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uriJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reloadJButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(outputJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(testJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uriJLabel)
                    .addComponent(reloadJButton)
                    .addComponent(uriJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(proxyListJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outputJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(testJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void reloadJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadJButtonActionPerformed
        getContext().newActionRunner("/network/reloadtab").run(buildReloadInput());
    }//GEN-LAST:event_reloadJButtonActionPerformed

    private void reloadTestJButton() {
        testJButton.setEnabled(false == dirty && -1 < proxyListJList.getSelectedIndex());
    }

    private void setDirty(final boolean dirty) {
        this.dirty = dirty;
    }

    private void testJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testJButtonActionPerformed
        getContext().newActionRunner("/network/test").run(buildTestInput());
    }//GEN-LAST:event_testJButtonActionPerformed

    private void uriJTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uriJTextFieldActionPerformed
        getContext().newActionRunner("/network/reloadtab").run(buildReloadInput());
    }//GEN-LAST:event_uriJTextFieldActionPerformed
}
