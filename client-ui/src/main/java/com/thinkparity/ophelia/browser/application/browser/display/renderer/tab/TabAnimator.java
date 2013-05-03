/*
 * Created On:  6-Dec-06 10:46:22 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TabAnimator {

    /** The working <code>Timer</code>. */
    private Timer animator;

    /** A flag indicating the animation is done. */
    private Boolean animationDone;

    /** A completion <code>Runnable</code>. */
    private Runnable animatorCompletion;

    /** The <code>JPanel</code>. */
    private final JPanel jPanel;

    /** The panel original height <code>int</code>. */
    private final int jPanelOriginalHeight;

    /** An apache logger wrapper. */
    private Log4JWrapper logger;

    /** The delay used by the <code>Timer</code>. */
    private final int timerDelay;

    /**
     * Create Animator.
     * 
     * @param jPanel
     *            The <code>JPanel</code> to animate.
     */
    public TabAnimator(final JPanel jPanel) {
        super();
        this.jPanel = jPanel;
        this.jPanelOriginalHeight = jPanel.getPreferredSize().height;
        this.timerDelay = 1;
    }

    /**
     * Collapse the panel's height via a timer.
     * 
     * @param heightDecrement
     *            The <code>int</code> amount by which to decrement the
     *            height.
     * @param heightBound
     *            The lower bound <code>int</code> of the jpanel height.
     */
    public void collapse(final int heightDecrement, final int heightBound) {
        collapse(heightDecrement, heightBound, new Runnable() {
            public void run() {}
        });
    }

    /**
     * Collapse the panel's height via a timer.
     * 
     * @param heightDecrement
     *            The <code>int</code> amount by which to decrement the
     *            height.
     * @param heightBound
     *            The lower bound <code>int</code> of the panel height.
     * @param animatorCompletion
     *            The runnable to execute upon completion of the animation.
     */
    public void collapse(final int heightDecrement, final int heightBound,
            final Runnable animatorCompletion) {
        if (null != animator && animator.isRunning()) {
            reset();
        }
        this.animationDone = Boolean.FALSE;
        this.animatorCompletion = animatorCompletion;
        animator = new Timer(timerDelay, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (null != logger) logger.logApiId();
                if (animationDone) {
                    stopAnimator();
                } else {
                    decrementHeight(heightDecrement, heightBound);
                    jPanel.revalidate();
                }
            }
        });
        animator.start();
    }

    /**
     * Expand the panel's height via a timer.
     * @param heightIncrement
     *            The <code>int</code> amount by which to increment the
     *            height.
     * @param heightBound
     *            The upper bound <code>int</code> of the height.
     */
    public void expand(final int heightIncrement, final int heightBound) {
        expand(heightIncrement, heightBound, new Runnable() {
            public void run() {}
        });
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
     * @param animatorCompletion
     *            The completion runnable to execute when finished.
     */
    public void expand(final int heightIncrement, final int heightBound,
            final Runnable animatorCompletion) {
        if (null != animator && animator.isRunning()) {
            reset();
        }
        this.animationDone = Boolean.FALSE;
        this.animatorCompletion = animatorCompletion;
        animator = new Timer(timerDelay, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (null != logger) logger.logApiId();
                if (animationDone) {
                    stopAnimator();
                } else {
                    incrementHeight(heightIncrement, heightBound);
                    jPanel.revalidate();
                    // ensure the panel is visible
                    final Rectangle rectangle = jPanel.getBounds();
                    rectangle.x = rectangle.y = 0;
                    jPanel.scrollRectToVisible(rectangle);
                }
            }
        });
        animator.start();
    }

    /**
     * Obtain logger.
     *
     * @return A Log4JWrapper.
     */
    public Log4JWrapper getLogger() {
        return logger;
    }

    /**
     * Determine whether or not the animator is running.
     * 
     * @return True if it is running.
     */
    public Boolean isRunning() {
        return animator.isRunning();
    }

    /**
     * Reset the animator. This will stop the timer (if it is running) and reset
     * the panel's original height.
     * 
     */
    public void reset() {
        if (null != animator && animator.isRunning()) {
            stopAnimator();
        }
        setHeight(jPanelOriginalHeight);
    }

    /**
     * Set logger.
     *
     * @param logger
     *		A Log4JWrapper.
     */
    public void setLogger(final Log4JWrapper logger) {
        this.logger = logger;
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
            animationDone = true;
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
            animationDone = true;
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

    /**
     * Stop the animator.
     *
     */
    private void stopAnimator() {
        animator.stop();
        animator = null;
        try {
            animatorCompletion.run();
        } finally {
            animatorCompletion = null;
        }
    }
}
