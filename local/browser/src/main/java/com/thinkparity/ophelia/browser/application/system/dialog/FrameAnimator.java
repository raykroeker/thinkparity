/**
 * Created On: Mar 14, 2007 10:50:20 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.Timer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public final class FrameAnimator {

    /** The working <code>Timer</code>. */
    private Timer animator;

    /** A completion <code>Runnable</code>. */
    private Runnable animatorCompletion;

    /** The <code>JDialog</code>. */
    private final JDialog jDialog;

    /** The JDialog current location <code>Point</code>. */
    private Point jDialogLocation;

    /** The JDialog original location <code>Point</code>. */
    private final Point jDialogOriginalLocation;

    /** The JDialog original size <code>Dimension</code>. */
    private final Dimension jDialogOriginalSize;

    /** The JDialog current size <code>Dimension</code>. */
    private Dimension jDialogSize;

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
    public FrameAnimator(final JDialog jDialog) {
        super();
        this.jDialog = jDialog;
        this.jDialogOriginalLocation = jDialog.getLocation();
        this.jDialogOriginalSize = jDialog.getSize();
        this.timerDelay = 10;
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
     * the dialog's original location and size.
     * 
     */
    public void reset() {
        if (null != animator && animator.isRunning()) {
            stopAnimator();
        }
        setBounds(jDialogOriginalLocation, jDialogOriginalSize);
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
     * Slide in the frame via a timer.
     * 
     * @param incrementY
     *            The <code>int</code> amount by which to increment the
     *            y location.
     * @param finalY
     *            The <code>int</code> final y location.
     * @param finalHeight
     *            The <code>int</code> final height.
     * @param animatorCompletion
     *            The completion runnable to execute when finished.
     */
    public void slideIn(final int incrementY, final int finalY,
            final int finalHeight,
            final Runnable animatorCompletion) {
        if (null != animator && animator.isRunning()) {
            reset();
        }
        this.jDialogLocation = jDialog.getLocation();
        this.jDialogSize = jDialog.getSize();
        this.animatorCompletion = animatorCompletion;
        animator = new Timer(timerDelay, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (null != logger) logger.logApiId();
                incrementLocationAndHeight(incrementY, finalY, finalHeight);
            }
        });
        animator.start();
    }

    /**
     * Increment the y location of the frame to a final location.
     * This api is intended to be used only by the animator's
     * animate event as it will stop the timer when the final y is hit.
     * 
     * @param incrementY
     *            The Y increment <code>int</code>.
     * @param finalY
     *            The <code>int</code> final y location.
     */
    private void incrementLocation(final int incrementY, final int finalY) {
        boolean done = false;
        jDialogLocation.y += incrementY;
        if ((incrementY < 0 && jDialogLocation.y <= finalY) ||
            (incrementY > 0 && jDialogLocation.y >= finalY)) {
            jDialogLocation.y = finalY;
            done = true;
        }
        setLocation(jDialogLocation);
        if (done) {
            stopAnimator();
        }
    }

    /**
     * Increment the y location and height of the frame to a final location
     * and height. This api is intended to be used only by the animator's
     * animate event as it will stop the timer when the final y is hit.
     * 
     * @param incrementY
     *            The Y increment <code>int</code>.
     * @param finalY
     *            The <code>int</code> final y location.
     * @param finalHeight
     *            The <code>int</code> final height.
     */
    private void incrementLocationAndHeight(final int incrementY,
            final int finalY, final int finalHeight) {
        boolean done = false;
        if (jDialogSize.height == finalHeight) {
            incrementLocation(incrementY, finalHeight);
        }
        jDialogLocation.y += incrementY;
        jDialogSize.height += (incrementY>0 ? incrementY : -incrementY);
        if (jDialogSize.height > finalHeight) {
            jDialogSize.height = finalHeight;
        }
        if ((incrementY < 0 && jDialogLocation.y <= finalY) ||
            (incrementY > 0 && jDialogLocation.y >= finalY)) {
            jDialogLocation.y = finalY;
            done = true;
        }
        setBounds(jDialogLocation, jDialogSize);
        jDialog.validate();
        if (done) {
            stopAnimator();
        }
    }

    /**
     * Set the bounds of the frame.
     * 
     * @param location
     *            The new location <code>Point</code>.
     * @param size
     *            The new size <code>Dimension</code>.
     */
    private void setBounds(final Point location, final Dimension size) {
        final Rectangle bounds = new Rectangle(location, size);
        jDialog.setBounds(bounds);
    }

    /**
     * Set the location of the frame.
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
