/**
 * Created On: 7-Nov-06 10:42:10 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.main;

import java.awt.Image;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import org.jdesktop.jdic.filetypes.Association;
import org.jdesktop.jdic.filetypes.AssociationService;
import org.jdesktop.jdic.icons.IconService;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class FileIconReader {
    
    /** An apache logger. */
    private static final Log4JWrapper slogger;
    
    /** A cache of image icons. */
    private static final Map<String, Object> ICON_CACHE;

    static {
        slogger = new Log4JWrapper();

        ICON_CACHE = new Hashtable<String, Object>(20, 0.75F);

        cacheIcons();
    }
    
    /** Cache all file icons listed in the FileIcon enum. */
    private static void cacheIcons() {
        slogger.logApiId();
        synchronized(ICON_CACHE) {
            ICON_CACHE.clear();
            
            for (final FileIcon fileIcon : FileIcon.values()) {
                slogger.logVariable("fileIcon", fileIcon);
                if (!ICON_CACHE.containsKey(fileIcon.iconName)) {
                    slogger.logVariable("fileIcon", fileIcon);
                    ICON_CACHE.put(fileIcon.iconName, ImageIOUtil.readIcon(fileIcon.iconFileName));
                }
            }
        }
    }
    
    /** An apache logger. */
    private final Log4JWrapper logger;
    
    /** A jdic file association service. */
    private final AssociationService associationService;

    /** Creates a new instance of MainPanelImageCache */
    public FileIconReader() {
        super();
        this.logger = new Log4JWrapper();
        this.associationService = new AssociationService();
    }

    /** Debug the cache. */
    void debug() {
        debug("File Icon Cache", ICON_CACHE);
    }
    
    /**
     * Get a file icon.
     */
    public ImageIcon getFileIcon(final String extension) {
        final String period = Separator.Period.toString();
        final String name;
        
        // The name is the extension in all caps with "." removed.
        if ((-1 != extension.indexOf(period)) &&
            (1 + extension.lastIndexOf(period) < extension.length())) {
            name = extension.substring(1 + extension.lastIndexOf(period)).toUpperCase();
        } else {
            name = extension.toUpperCase();
        }
        
        // Return the icon from the cache if available, otherwise
        // get the icon from the system and cache it.
        if (isCached(ICON_CACHE, name)) {
            return (ImageIcon) read(ICON_CACHE, name);
        } else {
            ImageIcon imageIcon = readSystemIcon(extension);
            if (null != imageIcon) {
                write(ICON_CACHE, name, imageIcon);
                return imageIcon;
            } else {
                return (read(FileIcon.FILE_DEFAULT));
            }
        }        
    }
    
    /**
     * Read a file icon from the cache. This is useful only for
     * the subset of file icons listed in the enum FileIcon.
     * 
     * @param icon
     *            The icon.
     * @return The icon.
     */
    public ImageIcon read(final FileIcon icon) {
        return (ImageIcon) read(ICON_CACHE, icon.iconName);
    }
    
    /**
     * Read the icon from the system if possible.
     */
    private ImageIcon readSystemIcon(final String extension) {
        ImageIcon imageIcon = null;
        switch (OSUtil.getOS()) {
        case WINDOWS_2000:
        case WINDOWS_XP:
            Association assoc = associationService
                    .getFileExtensionAssociation(extension);
            String iconSpec = assoc.getIconFileName();
            if (null != iconSpec) {
                final Image image = IconService.getIcon(iconSpec, 16);
                if (null != image) {
                    imageIcon = new ImageIcon(image);
                }
            }
            break;
        default:
            break;
        }
        
        return imageIcon;
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
    
    /**
     * Write to the cache.
     * 
     * @param cache
     *            The cache.
     * @param cacheKey
     *            The cache key.
     * @param cacheValue
     *            The cache value.          
     */
    private void write(final Map<String, Object> cache,
            final String cacheKey, final Object cacheValue) {
        slogger.logVariable("fileIcon", cacheKey);
        synchronized(cache) { cache.put(cacheKey, cacheValue); }
    }
    
    /**
     * See if the key is in the cache.
     * 
     * @param cache
     *            The cache.
     * @param cacheKey
     *            The cache key.
     * @return Boolean.
     */
    private Boolean isCached(final Map<String, Object> cache,
            final String cacheKey) {
        synchronized(cache) { return cache.containsKey(cacheKey); }
    }
    
    /** All pre-configured file icons. Some common icons aren't read
     *  properly by the jdic library so they are provided here. */
    public enum FileIcon {
        FILE_DEFAULT("FILE_DEFAULT", "IconFileDefault.png"),
        TXT("TXT", "IconFileTxt.png"),
        PDF("PDF", "IconFilePdf.png");
        
        /** The icon name. */
        private final String iconName;
        
        /** The icon file name. */
        private final String iconFileName;

        /**
         * Create a TabPanelIcon.
         * 
         * @param iconName
         *            The icon file name.
         */
        private FileIcon(final String iconName, final String iconFileName) {
            this.iconName = iconName;
            this.iconFileName = iconFileName;
        }            
    }
}
