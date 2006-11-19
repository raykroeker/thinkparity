/*
 * ThinkParityProgressBarUITestJFrame.java
 *
 * Created on November 18, 2006, 8:47 AM
 */

package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Cursor;
import java.util.Random;

import javax.swing.BorderFactory;

import com.thinkparity.codebase.swing.SwingWorker;

import com.thinkparity.ophelia.browser.Constants.Colors.Browser;

/**
 * <b>Title:</b>thinkParity Progress Bar UI Test<br>
 * <b>Description:</b>A simple JFrame to test the thinkParity progress bar ui.
 * To run the test; execute main an use the buttons.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ThinkParityProgressBarUITestJFrame extends
        javax.swing.JFrame {
    
    /**
     * Create ThinkParityProgressBarUITestJFrame.
     *
     */
    public ThinkParityProgressBarUITestJFrame() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        goDeterminateJButton = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jProgressBar1.setUI(new ThinkParityProgressBarUI());
        goIndeterminateJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        goDeterminateJButton.setText("Go Determinate");
        goDeterminateJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goDeterminateJButtonActionPerformed(evt);
            }
        });

        jProgressBar1.setBorder(BorderFactory.createLineBorder(Browser.ProgressBar.BORDER, 1));

        goIndeterminateJButton.setText("Go Indeterminate");
        goIndeterminateJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goIndeterminateJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(goIndeterminateJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(goDeterminateJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 40, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(goDeterminateJButton)
                    .add(goIndeterminateJButton))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goIndeterminateJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goIndeterminateJButtonActionPerformed
        final SwingWorker task = new IndeterminateTask();
        task.start();
    }//GEN-LAST:event_goIndeterminateJButtonActionPerformed

    private void goDeterminateJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goDeterminateJButtonActionPerformed
        final SwingWorker task = new DeterminateTask();
        task.start();
    }//GEN-LAST:event_goDeterminateJButtonActionPerformed

    /**
     * Start a task. Set the progress bar determination; disable controls and
     * set the cursor.
     * 
     * @param indeterminate
     *            The progress bar determination.
     */
    private void fireTaskBegin(final boolean indeterminate) {
        jProgressBar1.setIndeterminate(indeterminate);
        goDeterminateJButton.setEnabled(false);
        goIndeterminateJButton.setEnabled(false);
        getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    /**
     * End a task. Set the progress bar value to completion; reset the cursor and
     * enable the buttons.
     * 
     */
    private void fireTaskEnd() {
        jProgressBar1.setValue(jProgressBar1.getMaximum());
        getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        goDeterminateJButton.setEnabled(true);
        goIndeterminateJButton.setEnabled(true);
    }

    /**
     * Update a task.  Set the progress bar value.
     * 
     * @param value
     *            The progress bar value.
     */
    private void fireTaskUpdate(final int value) {
        jProgressBar1.setValue(value);
    }

    /**
     * <b>Title:</b>Indeterminate Task<br>
     * <b>Description:</b>An indeterminate task that works to 35% of completion
     * before becoming determinate.<br>
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    class IndeterminateTask extends SwingWorker {
        @Override
        public Object construct() {
            fireTaskBegin(true);
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            fireTaskUpdate(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                fireTaskUpdate(Math.min(progress, 100));
                if (progress >= 35) {
                    jProgressBar1.setValue(progress);
                    jProgressBar1.setIndeterminate(false);
                }
            }
            fireTaskEnd();
            return null;
        }
    }

    /**
     * <b>Title:</b>Determinate Task<br>
     * <b>Description:</b>An determinate task that works to completion.<br>
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    class DeterminateTask extends SwingWorker {
        @Override
        public Object construct() {
            fireTaskBegin(false);
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            fireTaskUpdate(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                fireTaskUpdate(Math.min(progress, 100));
            }
            fireTaskEnd();
            return null;
        }
    }

    /**
     * Execute the progress bar test.
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThinkParityProgressBarUITestJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton goDeterminateJButton;
    private javax.swing.JButton goIndeterminateJButton;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
