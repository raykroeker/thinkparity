/*
 * Created On:  6-Dec-06 10:46:22 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class Animator {

    /** The working <code>Timer</code>. */
    private Timer animator;

    /** The <code>JPanel</code>. */
    private final JPanel jPanel;

    /** The panel original height <code>int</code>. */
    private final int jPanelOriginalHeight;

    /** The delay used by the <code>Timer</code>. */
    private final int timerDelay;

    /**
     * Create Animator.
     * 
     * @param jPanel
     *            The <code>JPanel</code> to animate.
     * @param adjustmentRate
     *            The number of adjustments that are made per second.
     */
    Animator(final JPanel jPanel, final int adjustmentRate) {
        super();
        this.jPanel = jPanel;
        this.jPanelOriginalHeight = jPanel.getPreferredSize().height;
        this.timerDelay = adjustmentRate / 1000;
    }

    /**
     * Collapse the panel's height via a timer.
     * @param heightDecrement The <code>int</code> amount by which to decrement the height.
     * @param heightBound The lower bound <code>int</code> of the 
     */
    void collapse(final int heightDecrement, final int heightBound) {
        if (null != animator && animator.isRunning()) {
            reset();
        }
        animator = new Timer(timerDelay, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                decrementHeight(heightDecrement, heightBound);
                jPanel.revalidate();
            }
        });
        animator.start();
    }

    /**
     * Expand the panel's height via a timer.
     *
     * 
     * @param heightIncrement
     *            The <code>int</code> amount by which to increment the
     *            height.
     * @param heightBound
     *            The upper bound <code>int</code> of the height.
     */
    void expand(final int heightIncrement, final int heightBound) {
        if (null != animator && animator.isRunning()) {
            reset();
        }
        animator = new Timer(timerDelay, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                incrementHeight(heightIncrement, heightBound);
                jPanel.revalidate();
            }
        });
        animator.start();
    }

    /**
     * Reset the animator. This will stop the timer (if it is running) and reset
     * the panel's original height.
     * 
     */
    void reset() {
        if (null != animator && animator.isRunning()) {
            animator.stop();
            animator = null;
        }
        setHeight(jPanelOriginalHeight);
    }

    /**
     * Decrement the height of the panel to a minimum amount. This api is
     * intended to be used only by the animator's animate event as it will stop
     * the timer when the bound is hit.
     * 
     * @param increment
     *            The increment <code>int</code>.
     * @param bound
     *            The upper bound <code>int</code>.
     */
    private void decrementHeight(final int decrement, final int bound) {
        final Dimension size = jPanel.getPreferredSize();
        size.height -= decrement;
        if (bound >= size.height) {
            size.height = bound;
            animator.stop();
        }
        setHeight(size.height);
    }

    /**
     * Increment the height of the panel to a maximum amount. This api is
     * intended to be used only by the animator's animate event as it will stop
     * the timer when the bound is hit.
     * 
     * @param increment
     *            The increment <code>int</code>.
     * @param bound
     *            The upper bound <code>int</code>.
     */
    private void incrementHeight(final int increment, final int bound) {
        final Dimension size = jPanel.getPreferredSize();
        size.height += increment;
        if (bound <= size.height) {
            size.height = bound;
            animator.stop();
        }
        setHeight(size.height);
    }

    /**
     * Set the height of the panel.
     * 
     * @param height
     *            The new height <code>int</code>.
     */
    private void setHeight(int height) {
        final Dimension size = jPanel.getPreferredSize();
        size.height = height;
        jPanel.setPreferredSize(size);
    }
}
