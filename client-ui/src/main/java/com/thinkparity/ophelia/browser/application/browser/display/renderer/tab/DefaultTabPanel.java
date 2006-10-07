/*
 * Created On: 6-Oct-06 2:12:01 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.swing.AbstractJPanel;

/**
 * <b>Title:</b>thinkParity Default Tab Panel<br>
 * <b>Description:</b>A default implementation of a tab panel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultTabPanel extends AbstractJPanel implements TabPanel {

	/** A unique tab panel id. */
	private final Object id;

	/**
	 * Create DefaultTabPanel.
	 *
	 */
	public DefaultTabPanel() {
		super();
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {}
		});
		this.id = JVMUniqueId.nextId();
	}

	/**
	 * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getId()
	 */
	public Object getId() {
		return id;
	}

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof DefaultTabPanel) {
            return ((DefaultTabPanel) obj).getId().equals(getId());
        } else {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
