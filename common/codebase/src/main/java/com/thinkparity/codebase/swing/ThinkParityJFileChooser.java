/**
 * Created On: Mar 12, 2007 3:30:13 PM
 * $Id$
 */
package com.thinkparity.codebase.swing;

import javax.swing.JFileChooser;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityJFileChooser extends JFileChooser {

    private int state;

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
     * Set the state.
     * 
     * @param state
     *            The <code>int</code>state.
     */
    public void setState(final int state) {
        this.state = state;
    }
}
