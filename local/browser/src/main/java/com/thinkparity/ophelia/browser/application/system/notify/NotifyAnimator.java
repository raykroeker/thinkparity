/**
 * Created On: Feb 27, 2007 8:22:39 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.notify;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.Timer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public final class NotifyAnimator {

    /** The working <code>Timer</code>. */
    private Timer animator;

    /** A completion <code>Runnable</code>. */
    private Runnable animatorCompletion;

    /** The <code>JDialog</code>. */
    private final JDialog jDialog;

    /** The panel original location <code>Point</code>. */
    private final Point jDialogOriginalLocation;

    /** An apache logger wrapper. */
    private Log4JWrapper logger;

    /** The delay used by the <code>Timer</code>. */
    private final int timerDelay;

    /**
     * Create Animator.
     * 
     * @param jDialog
     *            The <code>JDialog</code> to animate.
     */
    public NotifyAnimator(final JDialog jDialog) {
        super();
        this.jDialog = jDialog;
        this.jDialogOriginalLocation = jDialog.getLocation();
        this.timerDelay = 5;
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
     * the dialog's original location.
     * 
     */
    public void reset() {
        if (null != animator && animator.isRunning()) {
            stopAnimator();
        }
        setLocation(jDialogOriginalLocation);
    }

    /**
     * Set logger.
     *
     * @param logger
     *      A Log4JWrapper.
     */
    public void setLogger(final Log4JWrapper logger) {
        this.logger = logger;
    }

    /**
     * Slide in the panel via a timer.
     * 
     * @param incrementY
     *            The <code>int</code> amount by which to increment the
     *            y location.
     * @param finalY
     *            The <code>int</code> final y location.
     * @param animatorCompletion
     *            The completion runnable to execute when finished.
     */
    public void slideIn(final int incrementY, final int finalY,
            final Runnable animatorCompletion) {
        if (null != animator && animator.isRunning()) {
            reset();
        }
        this.animatorCompletion = animatorCompletion;
        animator = new Timer(timerDelay, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (null != logger) logger.logApiId();
                incrementLocation(incrementY, finalY);
            }
        });
        animator.start();
    }

    /**
     * Increment the y location of the panel to a final location. This api is
     * intended to be used only by the animator's animate event as it will stop
     * the timer when the final y is hit.
     * 
     * @param incrementY
     *            The Y increment <code>int</code>.
     * @param finalY
     *            The final Y <code>int</code>.
     */
    private void incrementLocation(final int incrementY, final int finalY) {
        final Point location = jDialog.getLocation();
        location.y += incrementY;
        if ((incrementY < 0 && location.y <= finalY) ||
            (incrementY > 0 && location.y >= finalY)) {
            location.y = finalY;
            stopAnimator();
        }
        setLocation(location);
    }

    /**
     * Set the location of the panel.
     * 
     * @param location
     *            The new location <code>Point</code>.
     */
    private void setLocation(final Point location) {
        jDialog.setLocation(location);
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
