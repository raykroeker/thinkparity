/**
 * Created On: 21-Sep-06 5:22:25 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.main;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class MainCellImageCacheTest {

    /** An apache logger. */
    protected static final Logger slogger;
    
    /** A cache of image icons. */
    private static final Map<String, Object> ICON_CACHE;

    static {
        slogger = Logger.getLogger(MainCellImageCacheTest.class);

        ICON_CACHE = new Hashtable<String, Object>(20, 0.75F);

        cacheIcons();
    }

    /** Cache all main cell icons. */
    private static void cacheIcons() {
        synchronized(ICON_CACHE) {
            ICON_CACHE.clear();
            
            for (final TabCellIconTest tci : TabCellIconTest.values()) {
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

    /** An apache logger. */
    protected final Logger logger;

    /** Creates a new instance of MainCellImageCacheTest */
    public MainCellImageCacheTest() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

    /** Debug the cache. */
    void debug() {
        debug("Icon Cache", ICON_CACHE);
    }
    
    /**
     * Read a tab cell icon from the cache.
     * 
     * @param icon
     *            The icon.
     * @return The icon.
     * @see ImageIOUtil#readIcon(java.lang.String)
     */
    public ImageIcon read(final TabCellIconTest icon) {
        return (ImageIcon) read(ICON_CACHE, icon.iconName);
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
    public enum TabCellIconTest {
        INFO("Invisible20x20.png"),
        CONTAINER("IconContainer.png"),
        CONTAINER32x32("IconContainer32x32.png"),
        DRAFT("IconDraft.png"),
        DRAFT32x32("IconDraft32x32.png"),
        VERSION("IconVersion.png"),
        VERSION32x32("IconVersion32x32.png"),
        FOLDER_OPEN("IconFolderOpen.png"),
        FOLDER_CLOSED("IconFolderClosed.png"),
        CONTACT("IconContact.png"),
        DOCUMENT_WORD("IconWord.png"),
        DOCUMENT_EXCEL("IconExcel.png"),
        DOCUMENT_NOTEPAD("IconNotepad.png");
        
        /** The icon file name. */
        private final String iconName;

        /**
         * Create a TabCellIcon.
         * 
         * @param iconName
         *            The icon file name.
         */
        private TabCellIconTest(final String iconName) {
            this.iconName = iconName;
        }            
    }
}
