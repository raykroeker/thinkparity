/*
 * Created On: 6-Oct-06 2:12:01 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;

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
		this.id = JVMUniqueId.nextId();
        initComponents();
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
	 * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getId()
     * 
	 */
	public Object getId() {
		return id;
	}

    /**
     * @see java.lang.Object#hashCode()
     * 
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
    
    /**
     * The panel's mouse clicked event handler.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void formMouseClicked(final MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (!MenuFactory.isPopupMenu()) {
            if (e.getButton()==MouseEvent.BUTTON1) {
                // Every click (regardless of getClickCount() triggers a single click.
                triggerSingleClick(e);

                // Interesting fact about getClickCount() is that the count continues to 3, 4 and beyond if
                // the user keeps clicking with less than (say) 1/2 second delay between clicks.
                // So, a click count of 2, 4, 6, etc. triggers double click event.
                if (0 == e.getClickCount() % 2) {
                    triggerDoubleClick(e);
                }
                e.consume();
            }
        }
    }
    
    /**
     * The panel's mouse pressed event.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void formMousePressed(final MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            triggerPopup(this, e);
            e.consume();
        }
    }

    /**
     * The panel's mouse released event.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void formMouseReleased(final MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            triggerPopup(this, e);
            e.consume();
        }
    }

    protected final void installMouseOverTracker(final Component component) {
        final MouseInputListener mouseOverListener = new MouseInputAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                formMouseClicked(e);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                formMousePressed(e);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                formMouseReleased(e);
            }
        };
        component.addMouseListener(mouseOverListener);
    }

    /**
     * The mouse event handler for the panel's double click event.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void triggerDoubleClick(final MouseEvent e) {
    }

    /**
     * The event handler called to display the popup menu.
     * 
     * @param invoker
     *            The invoker <code>Component</code> upon which to display the
     *            popup menu.
     * @param e
     *            The <code>MouseEvent</code>.
     */
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
    }

    /**
     * The mouse event handler for the panel's single click event.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void triggerSingleClick(final MouseEvent e) {
    }
    
    /**
     * Initialize the panel's swing\awt components. In this case; event handlers
     * are created\registered for the panel's mouse events.
     * 
     */
    private void initComponents() {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                formMouseClicked(e);
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                formMousePressed(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                formMouseReleased(e);
            }
        });
    }
}
