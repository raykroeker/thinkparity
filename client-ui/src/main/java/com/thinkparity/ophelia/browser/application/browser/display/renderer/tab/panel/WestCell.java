/*
 * Created On:  9-Dec-06 8:48:58 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;


/**
 * <b>Title:</b>thinkParity Tab Panel West Cell<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class WestCell implements Cell {

    /** A list of <code>EastCell</code>s tied to the context of this cell. */
    private final List<EastCell> eastCells;

    /**
     * Create WestCell.
     *
     */
    public WestCell() {
        super();
        this.eastCells = new ArrayList<EastCell>();
    }

    /**
     * Add a cell.
     * 
     * @param cell
     *            An <code>EastCell</code>.
     */
    public void add(final EastCell cell) {
        eastCells.add(cell);
    }

    /**
     * Add a cell at a given index.
     * 
     * @param cell
     *            An <code>EastCell</code>.
     */
    public void add(final int index, final EastCell cell) {
        eastCells.add(index, cell);
    }

    /**
     * Obtain the cell at an index.
     * 
     * @param index
     *            An index.
     * @return An <code>EastCell</code>.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range (index &lt; 0 || index &gt;=
     *             size()).
     */
    public EastCell get(final int index) {
        return eastCells.get(index);
    }

    /**
     * Obtain the east cells.
     * 
     * @return An <code>EastCell</code> list.
     */
    public List<EastCell> getEastCells() {
        return Collections.unmodifiableList(eastCells);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#getIcon()
     * 
     */
    public Icon getIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#getText()
     * 
     */
    public String getText() {
        return null;
    }

    /**
     * Obtain the index of the cell.
     * 
     * @param cell
     *            An <code>EastCell</code>.
     * @return The index of the cell; or -1 if the cell does not exist.
     */
    public int indexOf(final EastCell cell) {
        return eastCells.indexOf(cell);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#invokeAction()
     * 
     */
    public void invokeAction() {}

    /**
     * Remove the cell.
     * 
     * @param cell
     *            An <code>EastCell</code>.
     */
    public void remove(final EastCell cell) {
        eastCells.remove(cell);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#showPopup()
     * 
     */
    public void showPopup() {}
}
