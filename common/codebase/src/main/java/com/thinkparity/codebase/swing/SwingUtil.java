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
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
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

    public static BufferedImage createImage(final int width, final int height) {
        initScreenDevices();
        return screenDevices[0].getDefaultConfiguration()
                .createCompatibleImage(width, height);
    }

    public static void insertWrappable(final JLabel jLabel, final String text) {
        SINGLETON.doInsertWrappable(jLabel, text);
    }

    private void doInsertWrappable(final JLabel jLabel, final String text) {
        jLabel.setText(MessageFormat.format("<html>{0}</html>", text));
    }

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

    public static void setCursor(final javax.swing.JComponent jComponent, final int cursor) {
        SINGLETON.doSetCursor(jComponent, cursor);
    }

    public static void setCursor(final java.awt.Container container, final int cursor) {
        SINGLETON.doSetCursor(container, cursor);
    }

    public static void setCursor(final java.awt.Container container, final java.awt.Cursor cursor) {
        SINGLETON.doSetCursor(container, cursor);
    }

    public static void setCursor(final javax.swing.JComponent jComponent, final java.awt.Cursor cursor) {
        SINGLETON.doSetCursor(jComponent, cursor);
    }

    private void doSetCursor(final java.awt.Container container, final java.awt.Cursor cursor) {
        javax.swing.SwingUtilities.getWindowAncestor(container).setCursor(cursor);
    }

    private void doSetCursor(final java.awt.Container container, final int cursor) {
        doSetCursor(container, java.awt.Cursor.getPredefinedCursor(cursor));
    }

    private void doSetCursor(final javax.swing.JComponent jComponent, final int cursor) {
        doSetCursor(jComponent, java.awt.Cursor.getPredefinedCursor(cursor));
    }

    private void doSetCursor(final javax.swing.JComponent jComponent, final java.awt.Cursor cursor) {
        javax.swing.SwingUtilities.getWindowAncestor(jComponent).setCursor(cursor);
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
     * Obtain the primary desktop bounds.
     * 
     * @return The primary desktop bounds <code>Rectangle</code>.
     */
    public static Rectangle getPrimaryDesktopBounds() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
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
        initScreenDevices();
        for (final GraphicsDevice screenDevice : screenDevices) {
            if (regionContains(screenDevice.getDefaultConfiguration().getBounds(), p))
                    return screenDevice;
        }
        throw Assert.createUnreachable("Point not within any display device.");
    }

    static Rectangle getScreenBounds(final GraphicsDevice screenDevice) {
        return screenDevice.getDefaultConfiguration().getBounds();
    }

    private static void initScreenDevices() {
        final GraphicsEnvironment graphicsEnvironment =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        screenDevices = graphicsEnvironment.getScreenDevices();
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
