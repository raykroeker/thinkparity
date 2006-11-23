/*
 * Created On: Jan 17, 2006
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * An abstraction of a swing JPanel.  Used by all thinkParity panels as a root
 * class.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class AbstractJPanel extends JPanel {

	/** Default background color. */
	private static final Color DEFAULT_BACKGROUND;

	static {
		// COLOR 249, 249, 249, 255
		DEFAULT_BACKGROUND = new Color(249, 249, 249, 255);
	}

    /**
     * Obtain the enter key stroke.
     *
     * @return A key stroke.
     */
    private static KeyStroke getEnterKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    }

    /**
     * Obtain the escape key stroke.
     *
     * @return A key stroke.
     */
    private static KeyStroke getEscapeKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    }

	/** An apache logger. */
	protected final Log4JWrapper logger;

    /** Swing container tools. */
	private final ContainerTools containerTools;

    /**
	 * The debug mouse adapter for a jpanel. This mouse adapter will print the
	 * geometry and component composition to the logger.
	 * 
	 */
	private final MouseAdapter debugMouseAdapter = new MouseAdapter() {
		public void mouseClicked(final MouseEvent e) {
			if (2 == e.getClickCount()) {
				if (e.isShiftDown()) {
					debug();
				}
			}
		}
	};

	/** A helper class dedicated to encapsulation of a move visitor. */
    private AbstractJPanelMoveHelper moveHelper;

	/**
	 * Create a AbstractJPanel.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 */
	protected AbstractJPanel() {
        this(null);
	}

	/**
	 * Create a AbstractJPanel.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param background
	 *            The background.
	 */
	protected AbstractJPanel(final Color background) {
		super();
		this.containerTools = new ContainerTools(this);
        this.logger = new Log4JWrapper(getClass());
		addMouseListener(debugMouseAdapter);
		setOpaque(true);
		setBackground(background);
	}

	/**
	 * Debug the list of components attached to this JPanel. This api is
	 * recursive if the component is an AbstractJPanel implementation.
	 * 
	 */
	public void debug() { containerTools.debug(); }

	/**
	 * Determine whether the user input for the frame is valid.
	 * 
	 * @return True if the input is valid; false otherwise.
	 */
	public Boolean isInputValid() { return Boolean.TRUE; }

    /**
     * Add a move listener to the component for the panel. A move listener will
     * allow the user will be able to click on and drag the component in order
     * to move the underlying window ancestor.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    protected final void addMoveListener(final JComponent jComponent) {
        if (null == moveHelper) {
            moveHelper = new AbstractJPanelMoveHelper(this, 25);
        }
        moveHelper.addListener(jComponent);
    }

    /**
     * Add a resize listener to the component for the panel. A resize listener
     * will allow the user to resize the underlying window ancestor by grabbing
     * an edge within the panel dragging.
     * 
     * @param edge
     *            An <code>Edge</code> to add.
     */
    protected final void addResizeListener(final Edge edge) {
        throw Assert.createNotYetImplemented("");
    }

    /**
     * Bind the enter key to the action.
     *
     * @param action
     *      The action to perform when enter is pressed.
     */
    protected void bindEnterKey(final String command, final Action action) {
        bindKey(getEnterKeyStroke(), command, action);
    }

    /**
     * Bind the escape key to an action.
     *
     * @param action
     *      The action to perform when escape is pressed.
     */
    protected void bindEscapeKey(final String command, final Action action) {
        bindKey(getEscapeKeyStroke(), command, action);
    }

    /**
     * Dispose of the window.
     * 
     */
    protected void disposeWindow() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

	/**
     * Remove a move listener from a component for the panel.
     * 
     * @param jComponent
     *            A <code>JPanel</code>.
     * @see AbstractJPanel#addMoveListener(JComponent)
     */
    protected final void removeMoveListener(final JComponent jComponent) {
        if (null == moveHelper)
            return;
        moveHelper.removeListener(jComponent);
    }

	/**
     * Remove a resize listener from a component for the panel.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    protected final void removeResizeListener(final Edge edge) {
        throw Assert.createNotYetImplemented("");
    }

	/**
	 * Set a default background color. 
	 *
	 */
	protected void setDefaultBackground() {
		setBackground(DEFAULT_BACKGROUND);
	}

    /**
     * Bind a key stroke to an action through a binding.
     *
     * @param keyStroke
     *      A key stroke.
     * @param action
     *      An action.
     */
    private void bindKey(final KeyStroke keyStroke, final String command, final Action action) {
        final ActionMap actionMap = getActionMap();
        final InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    
        actionMap.put(command, action);
        inputMap.put(keyStroke, command);
    }

    /** An enumerated definition of a panel edge. */
    public enum Edge { EAST, NORTH, SOUTH, WEST }
}
