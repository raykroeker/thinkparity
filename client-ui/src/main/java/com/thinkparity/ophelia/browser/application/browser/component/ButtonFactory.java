/*
 * Jan 13, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * <b>Title:</b>thinkParity OpheliaUI Button Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class ButtonFactory extends ComponentFactory {

	/** A SINGLETON button factory. */
    private static final ButtonFactory SINGLETON;

    static {
        SINGLETON = new ButtonFactory();
    }

    /**
     * Create a swing button. A default font will be applied and the opacity of
     * the button will be set to false.
     * 
     * @return A new <code>JButton</code>.
     */
    public static JButton create() {
        return SINGLETON.doCreate();
    }

    /**
     * Create a swing button. The specified font will be applied.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return A new <code>JButton</code>.
     * @see ButtonFactory#create()
     */
    public static JButton create(final Font font) {
        return SINGLETON.doCreate(font);
    }

    /**
     * Create a swing button. A default font will be applied as well as the
     * specified icon.
     * 
     * @param icon
     *            An <code>Icon</code>.
     * @return A new <code>JButton</code>.
     * @see ButtonFactory#create()
     */
    public static JButton create(final Icon icon) {
        return SINGLETON.doCreate(icon);
    }

    /**
     * Create a swing button. A default font will be applied as well as the
     * specified text.
     * 
     * @param text
     *            A text <code>String</code>.
     * @return A new <code>JButton</code>.
     * @see ButtonFactory#create()
     */
    public static JButton create(final String text) {
        return SINGLETON.doCreate(text);
    }

    /**
     * Create ButtonFactory.
     * 
     */
    private ButtonFactory() {
        super();
    }

    /**
     * Apply an icon to a swing button.
     * 
     * @param button
     *            An <code>AbstractButton</code>.
     * @param icon
     *            An <code>Icon</code>.
     */
    private void applyIcon(final AbstractButton button, final Icon icon) {
        button.setIcon(icon);
    }

    /**
     * Apply the text to a swing button.
     * 
     * @param button
     *            An <code>AbstractButton</code>.
     * @param text
     *            The text <code>String</code>.
     */
    private void applyText(final AbstractButton button, final String text) {
        button.setText(text);
    }

    /**
     * Create a swing button. A default font will be applied and the opacity of
     * the button will be set to false.
     * 
     * @return A new <code>JButton</code>.
     */
    private JButton doCreate() {
        final JButton jButton = new JButton();
        applyDefaultFont(jButton);
        jButton.setOpaque(false);
        return jButton;
    }

    /**
     * Create a swing button. The specified font will be applied.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return A new <code>JButton</code>.
     * @see ButtonFactory#create()
     */
    private JButton doCreate(final Font font) {
        final JButton jButton = create();
        applyFont(jButton, font);
        return jButton;
    }

    /**
     * Create a swing button. A default font will be applied as well as the
     * specified icon.
     * 
     * @param icon
     *            An <code>Icon</code>.
     * @return A new <code>JButton</code>.
     * @see ButtonFactory#create()
     */
    private JButton doCreate(final Icon icon) {
        final JButton jButton = create();
        applyIcon(jButton, icon);
        return jButton;
    }

    /**
     * Create a swing button. A default font will be applied as well as the
     * specified text.
     * 
     * @param text
     *            A text <code>String</code>.
     * @return A new <code>JButton</code>.
     * @see ButtonFactory#create()
     */
    private JButton doCreate(final String text) {
        final JButton jButton = create();
        applyText(jButton, text);
        return jButton;
    }
}
