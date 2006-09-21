/*
 * MainCellImageCache.java
 *
 * Created on April 12, 2006, 1:13 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.main;

import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * A cache of images for the main cell.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainCellImageCache {

    /** An apache logger. */
    protected static final Logger slogger;
    
    /** A cache of image icons. */
    private static final Map<String, Object> ICON_CACHE;

    /** A cache of buffered images. */
    private static final Map<String, Object> IMAGE_CACHE;

    static {
        slogger = Logger.getLogger(MainCellImageCache.class);

        ICON_CACHE = new Hashtable<String, Object>(20, 0.75F);
        IMAGE_CACHE = new Hashtable<String, Object>(20, 0.75F);

        cacheIcons();
        cacheImages();
    }

    /** Cache all main cell icons. */
    private static void cacheIcons() {
        synchronized(ICON_CACHE) {
            ICON_CACHE.clear();
            
            for (final TabCellIcon tci : TabCellIcon.values()) {
                if(!ICON_CACHE.containsKey(tci.iconName)) {
                    slogger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE CACHE] " +
                            "[CACHING " + tci.iconName + "]");
                    ICON_CACHE.put(
                            tci.iconName,
                            ImageIOUtil.readIcon(tci.iconName));
                }
            }
        }
    }

    /** Cache all tab cell images. */
    private static void cacheImages() {
        synchronized(IMAGE_CACHE) {
            IMAGE_CACHE.clear();

            for(final TabCellImage tci: TabCellImage.values()) {
                if(!IMAGE_CACHE.containsKey(tci.imageName)) {
                    slogger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE CACHE] " +
                            "[CACHING " + tci.imageName + "]");
                    IMAGE_CACHE.put(
                            tci.imageName,
                            ImageIOUtil.read(tci.imageName));
                }
            }
        }
    }

    /** An apache logger. */
    protected final Logger logger;

    /** Creates a new instance of MainCellImageCache */
    public MainCellImageCache() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

    /** Debug the cache. */
    void debug() {
        debug("Icon Cache", ICON_CACHE);
        debug("Image Cache", IMAGE_CACHE);
    }
    
    /**
     * Read a tab cell icon from the cache.
     * 
     * @param icon
     *            The icon.
     * @return The icon.
     * @see ImageIOUtil#readIcon(java.lang.String)
     */
    public ImageIcon read(final TabCellIcon icon) {
        return (ImageIcon) read(ICON_CACHE, icon.iconName);
    }

    /**
     * Read tab cell image from the cache.
     * 
     * @param image
     *            The image.
     * @return The image.
     * @see ImageIOUtil.read(java.lang.String)
     */
    public BufferedImage read(final TabCellImage image) {
        return (BufferedImage) read(IMAGE_CACHE, image.imageName);
    }

    /**
     * Debug a cache.
     * 
     * @param cacheName
     *            A name for the cache.
     * @param cache
     *            The cache.
     */
    private void debug(final String cacheName, final Map<String, Object> cache) {
        synchronized(cache) {
            for(final String key : cache.keySet()) {
                logger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE IO] [" +
                        cacheName + "] [" + key + "]");
            }
        }
    }

    /**
     * Read from the cache.
     * 
     * @param cache
     *            The cache.
     * @param cacheKey
     *            The cache key.
     * @return The cached object.
     */
    private Object read(final Map<String, Object> cache,
            final String cacheKey) {
        synchronized(cache) { return cache.get(cacheKey); }
    }
    
    /** All tab cell icons. */
    public enum TabCellIcon {
        INFO("Invisible20x20.png"),
        NODE_NOCHILDREN("Invisible9x9.png"),
        
        NODE_COLLAPSED("MainCellExpand.png"),
        NODE_EXPANDED("MainCellCollapse.png"),

        NODE_SEL_COLLAPSED("MainCellExpand.png"),
        NODE_SEL_EXPANDED("MainCellCollapse.png");
        
        /** The icon file name. */
        private final String iconName;

        /**
         * Create a TabCellIcon.
         * 
         * @param iconName
         *            The icon file name.
         */
        private TabCellIcon(final String iconName) {
            this.iconName = iconName;
        }            
    }

    /** All tab cell cell images. */
    public enum TabCellImage {

        BG_CLOSED("MainCellGray.png"),
        BG_DEFAULT("MainCellDefault.png"),
        BG_URGENT("MainCellOrange.png"),

        BG_SEL_CLOSED("MainCellGraySelected.png"),
        BG_SEL_DEFAULT("MainCellDefaultSelected.png"),
        BG_SEL_URGENT("MainCellOrangeSelected.png");

        /** The image file name. */
        private final String imageName;

        /**
         * Create a TabCellImage.
         * 
         * @param imageName
         *            The image file name.
         */
        private TabCellImage(final String imageName) {
            this.imageName = imageName;
        }
    }
}
