/**
 * Created On: Mar 12, 2007 3:30:13 PM
 * $Id$
 */
package com.thinkparity.codebase.swing;

import javax.swing.JFileChooser;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityJFileChooser extends JFileChooser {

    /** The JFileChooser state. */
    private int state;

    /**
     * Create ThinkParityJFileChooser.
     */
    public ThinkParityJFileChooser() {
        super();
        initialize();
        installAncestorListener();
    }

    /**
     * @see javax.swing.JFileChooser#approveSelection()
     */
    @Override
    public void approveSelection() {
        state = JFileChooser.APPROVE_OPTION;
        super.approveSelection();
    }

    /**
     * @see javax.swing.JFileChooser#cancelSelection()
     */
    @Override
    public void cancelSelection() {
        state = JFileChooser.CANCEL_OPTION;
        super.cancelSelection();
    }

    /**
     * Get the state. Note that JFileChooser.ERROR_OPTION may be returned
     * if the user closes the parent dialog via the X button.
     * 
     * @return The state <code>int</code>.
     */
    public int getState() {
        return state;
    }

    /**
     * Initialize.
     */
    private void initialize() {
        setState(JFileChooser.CANCEL_OPTION);
        rescanCurrentDirectory();
    }

    /**
     * Install an ancestor listener.
     * When the JFileChooser is added to a window, initialize.
     */
    private void installAncestorListener() {
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(final AncestorEvent event) {
                initialize();
            }
            public void ancestorMoved(final AncestorEvent event) {}
            public void ancestorRemoved(final AncestorEvent event) {}
        });
    }

    /**
     * Set the state.
     * 
     * @param state
     *            The <code>int</code>state.
     */
    private void setState(final int state) {
        this.state = state;
    }
}
