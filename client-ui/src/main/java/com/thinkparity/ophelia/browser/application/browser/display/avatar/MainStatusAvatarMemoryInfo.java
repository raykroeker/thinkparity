/*
 * Created On:  12-Sep-07 4:33:49 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.TimerTask;

import javax.swing.JLabel;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.BytesFormat.Unit;

/**
 * <b>Title:</b>thinkParity Ophelia UI Main Status Avatar Memory Info<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class MainStatusAvatarMemoryInfo {

    /** A bytes format. */
    private static final BytesFormat BYTES_FORMAT;

    /** The capture timer's initial delay. */
    private static final long CAPTURE_TIMER_DELAY;

    /** The capture timer's period. */
    private static final long CAPTURE_TIMER_PERIOD;

    /** The display timer's initial/periodic delay. */
    private static final int DISPLAY_TIMER_DELAY;

    /** The text pattern to use for extended info. */
    private static final String EXTENDED_TEXT_PATTERN;

    /** A runtime. */
    private static final Runtime RUNTIME;

    /** The text pattern to use for info. */
    private static final String TEXT_PATTERN;

    static {
        BYTES_FORMAT = new BytesFormat();
        CAPTURE_TIMER_DELAY = 500L;
        CAPTURE_TIMER_PERIOD = 5000L;
        DISPLAY_TIMER_DELAY = 5000;
        EXTENDED_TEXT_PATTERN = "<html><b>Max</b>:{3} [{4}-{5}]  <b>Total</b>:{6} [{7}-{8}]  <b>Free</b>:{0} [{1}-{2}]  <b>Used</b>:{9} [{10}-{11}]</html>";
        RUNTIME = Runtime.getRuntime();
        TEXT_PATTERN = "<html><b>Max:</b>{1}  <b>Total</b>:{2}  <b>Free</b>:{0}  <b>Used</b>:{3}</html>";
    }

    /**
     * Format the memory bytes as MB.
     * 
     * @param bytes
     *            A <code>long</code>.
     * @return A <code>String</code>.
     */
    private static String format(final long bytes) {
        return BYTES_FORMAT.format(Unit.MB, bytes);
    }

    /** A timer used to capture the memory information. */
    private final java.util.Timer captureTimer;

    /** A timer used to display the memory information. */
    private final javax.swing.Timer displayTimer;

    /** An extended flag. */
    private boolean extended;

    /** The current free/total/used/max memory information. */
    private long free = RUNTIME.freeMemory(), total = RUNTIME.totalMemory(),
        used = total - free, max = RUNTIME.maxMemory();

    /** The lower bound free/total/used/max memory information. */
    private long lFree = free, lTotal = total, lUsed = used, lMax = max;

    /** The memory label. */
    private final JLabel memoryJLabel;

    /** A pause flag. */
    private boolean paused;

    /** The upper bound free/total/used/max memory information. */
    private long uFree = free, uTotal = total, uUsed = used, uMax = max;

    /**
     * Create MainStatusAvatarDevelopmentInfo.
     *
     */
    MainStatusAvatarMemoryInfo(final JLabel memoryJLabel) {
        super();
        this.memoryJLabel = memoryJLabel;
        this.memoryJLabel.addMouseListener(new MouseAdapter() {
            /**
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             *
             */
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (2 == e.getClickCount()) {
                    gc();
                    capture();
                    display();
                }
            }
            /**
             * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
             *
             */
            @Override
            public void mouseEntered(final MouseEvent e) {
                pause();
                toggleExtended();
                display();
            }
            /**
             * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
             *
             */
            @Override
            public void mouseExited(final MouseEvent e) {
                resume();
                toggleExtended();
                display();
            }
        });
        this.captureTimer = new java.util.Timer("TPS-OpheliaUI-MemoryCapture", true);
        this.displayTimer = new javax.swing.Timer(DISPLAY_TIMER_DELAY, new ActionListener() {
            /**
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             * 
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!paused) {
                    display();
                }
            }
        });
        this.extended = false;
        this.memoryJLabel.setText(null);
        this.paused = false;

        this.captureTimer.schedule(new TimerTask() {
            /**
             * @see java.util.TimerTask#run()
             *
             */
            @Override
            public void run() {
                if (!paused) {
                    capture();
                }
            }
        }, CAPTURE_TIMER_DELAY, CAPTURE_TIMER_PERIOD);
        this.displayTimer.start();
    }

    /**
     * Capture the memory information and store the memory text.
     * 
     */
    private void capture() {
        free = RUNTIME.freeMemory();
        uFree = Math.max(free, uFree);
        lFree = Math.min(free, lFree);
        max = RUNTIME.maxMemory();
        uMax = Math.max(max, uMax);
        lMax = Math.min(max, lMax);
        total = RUNTIME.totalMemory();
        uTotal = Math.max(total, uTotal);
        lTotal = Math.min(total, lTotal);
        used = total - free;
        uUsed = Math.max(used, uUsed);
        lUsed = Math.min(used, lUsed);
    }

    /**
     * Display the memory text.
     * 
     */
    private void display() {
        final String memoryText;
        if (extended) {
            memoryText = MessageFormat.format(EXTENDED_TEXT_PATTERN,
                    format(free), format(lFree), format(uFree), format(max),
                    format(lMax), format(uMax), format(total), format(lTotal),
                    format(uTotal), format(used), format(lUsed), format(uUsed));
        } else {
            memoryText = MessageFormat.format(TEXT_PATTERN, format(free),
                    format(max), format(total), format(used));
        }
        memoryJLabel.setText(memoryText);
    }

    /**
     * Run the garbage collector.
     * 
     */
    private void gc() {
        RUNTIME.gc();
    }

    /**
     * Pause the capture/display.
     * 
     */
    private void pause() {
        paused = true;
    }

    /**
     * Resume the capture/display.
     * 
     */
    private void resume() {
        paused = false;
    }

    /**
     * Toggle whether or not to display extended information.
     * 
     */
    private void toggleExtended() {
        extended = !extended;
    }
}
