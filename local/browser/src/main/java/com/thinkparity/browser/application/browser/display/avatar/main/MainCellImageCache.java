/*
 * MainCellImageCache.java
 *
 * Created on April 12, 2006, 1:13 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.ImageIOUtil;

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
            
            for(final ContactIcon ci : ContactIcon.values()) {
                if(!ICON_CACHE.containsKey(ci.iconName)) {
                    slogger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE CACHE] " +
                            "[CACHING " + ci.iconName + "]");
                    ICON_CACHE.put(
                            ci.iconName,
                            ImageIOUtil.readIcon(ci.iconName));
                }
            }
            for(final DocumentIcon di : DocumentIcon.values()) {
                if(!ICON_CACHE.containsKey(di.iconName)) {
                    slogger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE CACHE] " +
                            "[CACHING " + di.iconName + "]");
                    ICON_CACHE.put(
                            di.iconName,
                            ImageIOUtil.readIcon(di.iconName));
                }
            }
            for(final HistoryItemIcon hii : HistoryItemIcon.values()) {
                if(!ICON_CACHE.containsKey(hii.iconName)) {
                    slogger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE CACHE] " +
                            "[CACHING " + hii.iconName + "]");
                    ICON_CACHE.put(
                            hii.iconName,
                            ImageIOUtil.readIcon(hii.iconName));
                }
            }
        }
    }

    /** Cache all main cell images. */
    private static void cacheImages() {
        synchronized(IMAGE_CACHE) {
            IMAGE_CACHE.clear();

            for(final DocumentImage di: DocumentImage.values()) {
                if(!IMAGE_CACHE.containsKey(di.imageName)) {
                    slogger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE CACHE] " +
                            "[CACHING " + di.imageName + "]");
                    IMAGE_CACHE.put(
                            di.imageName,
                            ImageIOUtil.read(di.imageName));
                }
            }
            for(final HistoryItemImage hii: HistoryItemImage.values()) {
                if(!IMAGE_CACHE.containsKey(hii.imageName)) {
                    slogger.debug("[BROWSER2] [APP] [B2] [MAIN CELL IMAGE CACHE] " +
                            "[CACHING " + hii.imageName + "]");
                    IMAGE_CACHE.put(
                            hii.imageName,
                            ImageIOUtil.read(hii.imageName));
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
     * Read a contact icon from the cache.
     * 
     * @param icon
     *            The icon.
     * @return The icon.
     * @see ImageIOUtil#readIcon(java.lang.String)
     */
    public ImageIcon read(final ContactIcon icon) {
        return (ImageIcon) read(ICON_CACHE, icon.iconName);
    }

    /**
     * Read a document icon from the cache.
     * 
     * @param icon
     *            The icon.
     * @return The icon.
     * @see ImageIOUtil#readIcon(java.lang.String)
     */
    public ImageIcon read(final DocumentIcon icon) {
        return (ImageIcon) read(ICON_CACHE, icon.iconName);
    }

    /**
     * Read document image from the cache.
     * 
     * @param image
     *            The image.
     * @return The image.
     * @see ImageIOUtil.read(java.lang.String)
     */
    public BufferedImage read(final DocumentImage image) {
        return (BufferedImage) read(IMAGE_CACHE, image.imageName);
    }

    /**
     * Read a history item icon from the cache.
     * 
     * @param icon
     *            The icon.
     * @return The icon.
     * @see ImageIOUtil#readIcon(String)
     */
    ImageIcon read(final HistoryItemIcon icon) {
        return (ImageIcon) read(ICON_CACHE, icon.iconName);
    }

    /**
     * Read history image from the cache.
     * 
     * @param image
     *            The image.
     * @return The image.
     * @see ImageIOUtil.read(java.lang.String)
     */
    BufferedImage read(final HistoryItemImage image) {
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
    
    /** All contact cell icons. */
    public enum ContactIcon {
        INFO("Invisible20x20.png"),
        NODE("Invisible9x9.png"),
        NODE_SELECTED("Invisible9x9.png");
        
        /** The icon file name. */
        private final String iconName;

        /**
         * Create a ContactIcon.
         * 
         * @param iconName
         *            The icon file name.
         */
        private ContactIcon(final String iconName) {
            this.iconName = iconName;
        }        
    }

    /** All document cell icons. */
    public enum DocumentIcon {

        INFO_IS_KEYHOLDER("Invisible20x20.png"),
        INFO_IS_NOT_KEYHOLDER("Lock.png"),

        NODE_DEFAULT("Invisible9x9.png"),
        NODE_EXPANDED("MainCellCollapse.png"),

        NODE_SEL_DEFAULT("MainCellExpand.png"),
        NODE_SEL_EXPANDED("MainCellCollapse.png");

        /** The icon file name. */
        private final String iconName;

        /**
         * Create a DocumentIcon.
         * 
         * @param iconName
         *            The icon file name.
         */
        private DocumentIcon(final String iconName) {
            this.iconName = iconName;
        }
    }

    /** All document cell images. */
    public enum DocumentImage {

        BG_CLOSED("MainCellGray.png"),
        BG_DEFAULT("MainCellDefault.png"),
        BG_URGENT("MainCellOrange.png"),

        BG_SEL_CLOSED("MainCellGraySelected.png"),
        BG_SEL_DEFAULT("MainCellDefaultSelected.png"),
        BG_SEL_URGENT("MainCellOrangeSelected.png");

        /** The image file name. */
        private final String imageName;

        /**
         * Create a DocumentImage.
         * 
         * @param imageName
         *            The image file name.
         */
        private DocumentImage(final String imageName) {
            this.imageName = imageName;
        }
    }

    /** All history item cell icons. */
    enum HistoryItemIcon {

        INFO_DEFAULT("Invisible20x20.png"),
        INFO_PENDING("MainCellInfoPending.png"),

        NODE_DEFAULT("Invisible12x12.png"),
        NODE_DEFAULT_SELECTED("Invisible12x12.png");

        /** The icon file name. */
        private final String iconName;

        /**
         * Create a HistoryItemIcon.
         * 
         * @param iconName
         *            The icon file name.
         */
        private HistoryItemIcon(final String iconName) {
            this.iconName = iconName;
        }
    }

    /** All history item cell images. */
    enum HistoryItemImage {

        BG_CLOSED("MainCellGray.png"),
        BG_DEFAULT("MainCellGreen.png"),
        BG_URGENT("MainCellOrange.png"),
        
        BG_SEL_CLOSED("MainCellGraySelected.png"),
        BG_SEL_DEFAULT("MainCellGreenSelected.png"),
        BG_SEL_URGENT("MainCellOrangeSelected.png");

        /** The image file name. */
        private final String imageName;

        /**
         * Create a HistoryItemImage.
         * 
         * @param imageName
         *            The image file name.
         */
        private HistoryItemImage(final String imageName) {
            this.imageName = imageName;
        }
    }
}
