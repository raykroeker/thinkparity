/*
 * Created On:  3-Oct-07 4:30:15 PM
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.Font;

import javax.swing.JComboBox;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ComboFactory extends ComponentFactory {

    private static final ComboFactory SINGLETON;

    static {
        SINGLETON = new ComboFactory();
    }

    /**
     * Create a combo box with a font.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return A <code>JComboBox</code>.
     */
    public static JComboBox create(final Font font) {
        return SINGLETON.createImpl(font);
    }

    /**
     * Create ComboFactory.
     *
     */
    private ComboFactory() {
        super();
    }

    /**
     * Create a combo box with a font.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return A <code>JComboBox</code>.
     */
    private JComboBox createImpl(final Font font) {
        final JComboBox jComboBox = new JComboBox();
        applyFont(jComboBox, font);
        return jComboBox;
    }
}
