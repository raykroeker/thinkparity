/*
 * Created On: 6-Oct-06 2:12:01 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.border.Border;
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

    /** A flag indicating whether or not the mouse is hovering over the panel. */
    private Boolean mouseOver;

    /**
	 * Create DefaultTabPanel.
	 *
	 */
	public DefaultTabPanel() {
		super();
		this.id = JVMUniqueId.nextId();
        this.mouseOver = Boolean.FALSE;
        initComponents();
	}

    /**
     * Install a mouse over tracker which tracks the mouse and records the index
     * above which it is positioned. It also updates the underlying cell's
     * "mouseOver" property.
     * 
     */
    protected final void installMouseOverTracker(final Component component) {
        final MouseInputListener mouseOverListener = new MouseInputAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                setMouseOver(Boolean.TRUE);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                setMouseOver(Boolean.FALSE);
            }
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
        component.addMouseMotionListener(mouseOverListener);
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
	 */
	public Object getId() {
		return id;
	}

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Determine whether or not the mouse over flag is set.
     * 
     * @return The mouse over <code>Boolean</code>.
     */
    public Boolean isSetMouseOver() {
        return mouseOver;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#setMouseOver(java.lang.Boolean)
     */
    public void setMouseOver(final Boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
    
    /**
     * Prepare for repaint, for example, adjust colors.  
     */
    public void prepareForRepaint() {        
    }
    
    /**
     * Prepare for repaint, after validate().
     */
    public void prepareForRepaintAfterValidate() {
    }
    
    /**
     * Get the background color.
     * 
     * @return Background color.
     */
    public Color getBackgroundColor() { 
        return getBackground();
    }  
    
    /**
     * Get the preferred size.
     * 
     * @param last
     *          True if this is the last entity.
     * @return The preferred size <code>Dimension</code>.
     */   
    public Dimension getPreferredSize(final Boolean last) {
        return getPreferredSize();
    }
    
    /**
     * Get the border.
     * 
     * @param last
     *          True if this is the last entity.
     * @return The border.
     */
    public Border getBorder(final Boolean last) {
        return getBorder();
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
