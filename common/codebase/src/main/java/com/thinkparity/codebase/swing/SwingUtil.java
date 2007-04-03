/*
 * Created On: Jan 13, 2006
 */
package com.thinkparity.codebase.swing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

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

    /**
     * Ensure the runnable is executed on the AWT event dispatch thread.
     * 
     * @param runnable
     *            A <code>Runnable</code>.
     */
    public static void ensureDispatchThread(final Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
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

    /**
     * Extract the value of a text area.
     * 
     * @param jTextArea
     *            A <code>JTextArea</code>.
     * @param trim
     *            Whether or not to trim the value before returning.
     * @return The text of the text area.
     */
    public static String extract(final JTextArea jTextArea, final Boolean trim) {
        return SINGLETON.doExtract(jTextArea, trim);
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
     * Extract the value of a text field.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @param trim
     *            Whether or not the value should be trimmed before returning.
     * @return The <code>String</code> value of the text field.
     */
    public static String extract(final JTextField jTextField, final Boolean trim) {
        return SINGLETON.doExtract(jTextField, trim);
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
     * Get the string width in the context of Graphics.
     * 
     * @param text
     *            The text <code>String</code>.
     * @param g
     *            The <code>Graphics</code>.
     * @return The width <code>int</code>.
     */
    public static int getStringWidth(final String text, final Graphics g) {
        final FontMetrics fontMetrics = g.getFontMetrics();
        return fontMetrics.stringWidth(text);
    }

    /**
     * Insert the content of a stream into a text area.
     * 
     * @param jTextArea
     *            A <code>JTextArea</code>.
     * @param stream
     *            An <code>InputStream</code>.
     */
    public static void insert(final JTextArea jTextArea,
            final InputStream stream) {
        SINGLETON.doInsert(jTextArea, stream);
    }

    public static void insertWrappable(final JLabel jLabel, final String text) {
        SINGLETON.doInsertWrappable(jLabel, text);
    }

    /**
     * Determine if the component is in a maximized JFrame window.
     * 
     * @param component
     *            A <code>Component</code>.
     */
    public static Boolean isInMaximizedWindow(final Component component) {
        return SINGLETON.isInMaximizedWindowImpl(component);
    }

    /**
     * Limit the string width in the context of Graphics, using an ellipsis at the end of the string.
     * 
     * @param text
     *            The text <code>String</code>.
     * @param maxWidth
     *            The maximum width <code>int</code>.
     * @param g
     *            The <code>Graphics</code>.
     * @return The adjusted <code>String</code>, or null if the text cannot fit.
     */
    public static String limitWidthWithEllipsis(final String text,
            final int maxWidth, final Graphics g) {
        return SINGLETON.doLimitWidthWithEllipsis(text, maxWidth, g);
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

    public static void setCursor(final java.awt.Component component, final int cursor) {
        SINGLETON.doSetCursor(component, cursor);
    }

    public static void setCursor(final java.awt.Component component, final java.awt.Cursor cursor) {
        SINGLETON.doSetCursor(component, cursor);
    }
    
    public static void setCursor(final java.awt.Container container, final int cursor) {
        SINGLETON.doSetCursor(container, cursor);
    }

    public static void setCursor(final java.awt.Container container, final java.awt.Cursor cursor) {
        SINGLETON.doSetCursor(container, cursor);
    }

    public static void setCursor(final javax.swing.JComponent jComponent, final int cursor) {
        SINGLETON.doSetCursor(jComponent, cursor);
    }

    public static void setCursor(final javax.swing.JComponent jComponent, final java.awt.Cursor cursor) {
        SINGLETON.doSetCursor(jComponent, cursor);
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
        return doExtract(jTextArea, Boolean.FALSE);
    }

    /**
     * Extract the value of a text area.
     * 
     * @param jTextArea
     *            A <code>JTextArea</code>.
     * @return The text of the text area.
     */
    private String doExtract(final JTextArea jTextArea, final Boolean trim) {
        return doExtract(jTextArea.getText(), trim.booleanValue());
    }

    /**
     * Extract the value of a text field.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @return The <code>String</code> value of the text field.
     */
    private String doExtract(final JTextField jTextField) {
        return doExtract(jTextField, Boolean.FALSE);
    }

    /**
     * Extract the value of a text field.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @param trim
     *            Whether or not the value should be trimmed before returning.
     * @return The <code>String</code> value of the text field.
     */
    private String doExtract(final JTextField jTextField, final Boolean trim) {
        return doExtract(jTextField.getText(), trim.booleanValue());
    }

    /**
     * Extract the value of a string. If it is of 0 length; null will be
     * returned.
     * 
     * @param string
     *            A <code>String</code>.
     * @return The value of the <code>String</code>.
     */
	private String doExtract(final String string, final boolean trim) {
		if (null == string) {
            return null;
		} else if (0 == string.length()) {
            return null;
		} else {
            if (trim) {
                final String trimmed = string.trim();
                if (0 == trimmed.length()) {
                    return null;
                } else {
                    return trimmed;
                }
            } else {
                return string;
            }
        }
	}

    /**
     * Insert the stream into the text area using the buffer.
     * 
     * @param jTextArea
     *            A <code>JTextArea</code>.
     * @param stream
     *            An <code>InputStream</code>.
     */
    private void doInsert(final JTextArea jTextArea, final InputStream stream) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            for (String line = reader.readLine(); line != null;
                    line = reader.readLine()) {
                jTextArea.insert(line, jTextArea.getDocument().getLength());
                jTextArea.insert(Separator.SystemNewLine.toString(),
                        jTextArea.getDocument().getLength());
            }
        } catch (final IOException iox) {
            new Log4JWrapper().logError(iox, "Could not load text-area {0}.",
                    jTextArea.getName());
        }
    }

    private void doInsertWrappable(final JLabel jLabel, final String text) {
        jLabel.setText(MessageFormat.format("<html>{0}</html>", text));
    }

    /**
     * Limit the string width in the context of Graphics, using an ellipsis at the end of the string.
     * 
     * @param text
     *            The text <code>String</code>.
     * @param maxWidth
     *            The maximum width <code>int</code>.
     * @param g
     *            The <code>Graphics</code>.
     * @return The adjusted <code>String</code>, or null if the text cannot fit.
     */
    private String doLimitWidthWithEllipsis(final String text,
            final int maxWidth, final Graphics g) {
        String clippedText = text;
        int clipChars = 0;        
        while (maxWidth < getStringWidth(clippedText, g)
                && clipChars < text.length()) {
            clipChars++;
            final StringBuffer buffer = new StringBuffer(text.substring(0,
                    text.length() - clipChars));
            buffer.append("...");
            clippedText = buffer.toString();
        }
        
        if (clipChars == text.length()) {
            return null;
        } else {
            return clippedText;
        }
    }

    private void doSetCursor(final java.awt.Component component, final int cursor) {
        doSetCursor(component, java.awt.Cursor.getPredefinedCursor(cursor));
    }

    private void doSetCursor(final java.awt.Component component, final java.awt.Cursor cursor) {
        final Window window = javax.swing.SwingUtilities.getWindowAncestor(component);
        if (null != window) {
            window.setCursor(cursor);
        } else {
            component.setCursor(cursor);
        }
    }

    /**
     * Get the Window ancestor of the Component.
     * 
     * @param component
     *            A <code>Component</code>.
     * @return A <code>Window</code>.
     */
    private Window getWindowAncestor(final Component component) {
        final Window window;
        if (component instanceof Window) {
            window = (Window)component;
        } else {
            window = javax.swing.SwingUtilities.getWindowAncestor(component);
        }
        return window;
    }

    /**
     * Determine if the component is in a maximized JFrame window.
     * 
     * @param component
     *            A <code>Component</code>.
     */
    private Boolean isInMaximizedWindowImpl(final Component component) {
        final Window window = getWindowAncestor(component);
        if (null != window && window instanceof JFrame) {
            return (((JFrame)window).getExtendedState() & JFrame.MAXIMIZED_BOTH) > 0;
        } else {
            return Boolean.FALSE;
        }
    }
}
