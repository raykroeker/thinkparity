/**
 * Created On: 25-Oct-06 3:09:08 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.main;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class MainPanelImageCache {
    
    /** An apache logger. */
    protected static final Log4JWrapper slogger;
    
    /** A cache of image icons. */
    private static final Map<String, Object> ICON_CACHE;

    static {
        slogger = new Log4JWrapper();

        ICON_CACHE = new Hashtable<String, Object>(20, 0.75F);

        cacheIcons();
    }
    
    /** Cache all main panel icons. */
    private static void cacheIcons() {
        slogger.logApiId();
        synchronized(ICON_CACHE) {
            ICON_CACHE.clear();
            
            for (final TabPanelIcon panelIcon : TabPanelIcon.values()) {
                slogger.logVariable("panelIcon", panelIcon);
                if (!ICON_CACHE.containsKey(panelIcon.iconName)) {
                    slogger.logVariable("panelIcon", panelIcon);
                    ICON_CACHE.put(panelIcon.iconName, ImageIOUtil.readIcon(panelIcon.iconName));
                }
            }
        }
    }

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** Creates a new instance of MainPanelImageCache */
    public MainPanelImageCache() {
        super();
        this.logger = new Log4JWrapper();
    }

    /** Debug the cache. */
    void debug() {
        debug("Icon Cache", ICON_CACHE);
    }
    
    /**
     * Read a tab panel icon from the cache.
     * 
     * @param icon
     *            The icon.
     * @return The icon.
     * @see ImageIOUtil#readIcon(java.lang.String)
     */
    public ImageIcon read(final TabPanelIcon icon) {
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
        logger.logApiId();
        logger.logVariable("cacheName", cacheName);
        synchronized (cache) {
            for(final String key : cache.keySet()) {
                logger.logVariable("key", key);
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
    
    /** All tab panel icons. */
    public enum TabPanelIcon {
        INVISIBLE("Invisible16x16.png"),
        CONTAINER("IconContainer.png"),
        CONTAINER_ROLLOVER("IconContainer_Rollover.png"),
        CONTAINER_BOOKMARK("IconContainer_Bookmark.png"),
        CONTAINER_BOOKMARK_ROLLOVER("IconContainer_Bookmark_Rollover.png"),
        DRAFT("IconDraft.png"),
        VERSION("IconVersion.png"),
        VERSION_WITH_COMMENT("IconVersionWithComment.png"),
        USER("IconUser.png"),
        USER_NOT_RECEIVED("IconUserNotReceived.png"),
        FILE_TXT("IconFileTxt.png"),
        FILE_DOC("IconFileDoc.png"),
        FILE_XLS("IconFileXls.png"),
        FILE_PDF("IconFilePdf.png"),
        FILE_DEFAULT("IconFileDefault.png"),
        COMMENT("IconComment.png"),
        SELECTED("IconSelected.png"),
        
        // Column sort icons
        SORT_NONE("SortNone.png"),
        SORT_NONE_ROLLOVER("SortNoneRollover.png"),
        SORT_DOWN("SortDown.png"),
        SORT_DOWN_ROLLOVER("SortDownRollover.png"),
        SORT_UP("SortUp.png"),
        SORT_UP_ROLLOVER("SortUpRollover.png");
        
        /** The icon file name. */
        private final String iconName;

        /**
         * Create a TabPanelIcon.
         * 
         * @param iconName
         *            The icon file name.
         */
        private TabPanelIcon(final String iconName) {
            this.iconName = iconName;
        }            
    }
}
