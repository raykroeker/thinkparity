/*
 * Created On: Jan 13, 2006
 */
package com.thinkparity.codebase.swing;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
public class SwingUtil {

	/** An array of the screen devices. */
    private static GraphicsDevice[] screenDevices;

	/** A singleton instance. */
	private static final SwingUtil SINGLETON;

    static { SINGLETON = new SwingUtil(); }

    /**
     * Extract the value of a checkbox.
     * 
     * @param jCheckBox
     *            A <code>JCheckBox</code>.
     * @return True if the checkbox is selected; false otherwise.
     */
    public static Boolean extract(final JCheckBox jCheckBox) {
        return SINGLETON.doExtract(jCheckBox);
    }

    /**
     * Extract the value of a text area.
     * 
     * @param jTextArea
     *            A <code>JTextArea</code>.
     * @return The text of the text area.
     */
    public static String extract(final JTextArea jTextArea) {
        return SINGLETON.doExtract(jTextArea);
    }

    /**
     * Extract the value of a text field.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @return The <code>String</code> value of the text field.
     */
	public static String extract(final JTextField jTextField) {
		return SINGLETON.doExtract(jTextField);
	}

    /**
     * Determine whether or not the region contains the point.
     * 
     * @param region
     *            A <code>Rectangle</code> region.
     * @param point
     *            A 2-dimensional <code>Point</code>.
     * @return True if the point lies within the region; false otherwise.
     */
	public static Boolean regionContains(final Rectangle region,
            final Point point) {
		return SINGLETON.doesRegionContain(region, point);
	}

    /**
     * Obtain the primary screen size.
     * 
     * @return A <code>Dimension</code>.
     */
    static Dimension getPrimaryScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    static GraphicsDevice getScreen(final Point p) {
        if (null == screenDevices) {
            final GraphicsEnvironment graphicsEnvironment =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
            screenDevices = graphicsEnvironment.getScreenDevices();
        }
        for (final GraphicsDevice screenDevice : screenDevices) {
            if (regionContains(screenDevice.getDefaultConfiguration().getBounds(), p))
                    return screenDevice;
        }
        throw Assert.createUnreachable("Point not within any display device.");
    }

    static Rectangle getScreenBounds(final GraphicsDevice screenDevice) {
        return screenDevice.getDefaultConfiguration().getBounds();
    }

    /** Create SwingUtil. */
	private SwingUtil() { super(); }

    /**
     * Determine whether or not the region contains the point.
     * 
     * @param region
     *            A <code>Rectangle</code> region.
     * @param point
     *            A 2-dimensional <code>Point</code>.
     * @return True if the point lies within the region; false otherwise.
     */
	private Boolean doesRegionContain(final Rectangle region, final Point point) {
		if(null == region) { return Boolean.FALSE; }
		if(null == point) { return Boolean.FALSE; }
        return (point.x >= region.x)
        	&& (point.x < region.x + region.width)
        	&& (point.y >= region.y)
        	&& (point.y < region.y + region.height);
	}

    /**
     * Extract the value of a checkbox.
     * 
     * @param jCheckBox
     *            A <code>JCheckBox</code>.
     * @return True if the checkbox is selected; false otherwise.
     */
    private Boolean doExtract(final JCheckBox jCheckBox) {
        return jCheckBox.isSelected();
    }

    /**
     * Extract the value of a text area.
     * 
     * @param jTextArea
     *            A <code>JTextArea</code>.
     * @return The text of the text area.
     */
	private String doExtract(final JTextArea jTextArea) {
        return doExtract(jTextArea.getText());
    }

    /**
     * Extract the value of a text field.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @return The <code>String</code> value of the text field.
     */
	private String doExtract(final JTextField jTextField) {
		return doExtract(jTextField.getText());
	}

    /**
     * Extract the value of a string. If it is of 0 length; null will be
     * returned.
     * 
     * @param string
     *            A <code>String</code>.
     * @return The value of the <code>String</code>.
     */
	private String doExtract(final String string) {
		if (null == string) {
            return null;
		} else if (0 == string.length()) {
            return null;
		} else {
		    return string;
        }
	}
}
