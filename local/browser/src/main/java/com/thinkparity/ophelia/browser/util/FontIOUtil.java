/*
 * Apr 27, 2006
 */
package com.thinkparity.ophelia.browser.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.ResourceUtil;

/**
 * The font io util is a convenience class used to read fonts from the fonts
 * directory in the root of the classpath.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FontIOUtil {

    /** A singleton instance. */
    private static final FontIOUtil SINGLETON;

    static { SINGLETON = new FontIOUtil(); }

    /**
     * Derive a font from a file.
     * 
     * @param name
     *            The file name.
     * @param size
     *            The font size.
     * @return The font.
     */
    public static Font derive(final String fontName, final float size) {
        return SINGLETON.doDerive(fontName, size);
    }

    /**
     * Derive a font from a file.
     * 
     * @param name
     *            The file name.
     * @param size
     *            The font size.
     * @param style
     *            The font style.
     * @return The font.
     */
    public static Font derive(final String fontName, final float size,
            final int style) {
        return SINGLETON.doDerive(fontName, size, style);
    }

    /** An apache logger. */
    protected final Logger logger;

    /**
     * Create a FontIOUtil.
     */
    private FontIOUtil() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Derive a font from a file.
     * 
     * @param name
     *            The file name.
     * @param size
     *            The font size.
     * @return The font.
     */
    private Font doDerive(final String fontName, final float size) {
        final Font font = read(fontName);
        return font.deriveFont(size);
    }

    /**
     * Derive a font from a file.
     * 
     * @param name
     *            The file name.
     * @param size
     *            The font size.
     * @param style
     *            The font style.
     * @return The font.
     */
    private Font doDerive(final String fontName, final float size,
            final int style) {
        final Font font = read(fontName);
        return font.deriveFont(style, size);
    }

    /**
     * Open an input stream for a font.
     * 
     * @param fontName
     *            The font name.
     * @return An input stream.
     */
    private InputStream openStream(final String fontName) {
        return ResourceUtil.getInputStream("fonts/" + fontName);
    }

    /**
     * Read a font from a file.
     * 
     * @param name
     *            The file name.
     * @return The font.
     */
    private Font read(final String fontName) {
        logger.info("[LBROWSER] [PLATFORM] [UTIL] [FONT IO UTIL] [READ]");
        logger.debug(fontName);
        final InputStream is = openStream(fontName);
        try { return Font.createFont(Font.TRUETYPE_FONT, is); }
        catch(final FontFormatException ffx) {
            throw new RuntimeException(ffx);
        }
        catch(final IOException iox) {
            throw new RuntimeException(iox);
        }
        finally {
            try { is.close(); }
            catch(final IOException iox) { throw new RuntimeException(iox); }
        }
    }

}
