/*
 * Created On: 7-Nov-06 10:42:10 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.main;

import java.awt.Image;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.browser.util.ArtifactUtil;
import com.thinkparity.ophelia.browser.util.ArtifactVersionUtil;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

import org.jdesktop.jdic.filetypes.Association;
import org.jdesktop.jdic.filetypes.AssociationService;
import org.jdesktop.jdic.icons.IconService;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class FileIconReader {
    
    /** A cache of image icons. */
    private static final Map<String, Object> ICON_CACHE;
    
    /** An apache logger. */
    private static final Log4JWrapper slogger;

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

    /** A jdic <code>AssociationService</code>. */
    private final AssociationService associationService;

    /**
     * Create FileIconReader.
     *
     */
    public FileIconReader() {
        super();
        this.associationService = new AssociationService();
    }

    /**
     * Obtain an icon for a document.
     * 
     * @param document.
     *            A <code>Document</code>.
     * @return An <code>ImageIcon</code>.
     */
    public ImageIcon getIcon(final Document document) {
        return getFileIcon(ArtifactUtil.getNameExtension(document));
    }

    /**
     * Obtain an icon for a document version.
     * 
     * @param version.
     *            A <code>DocumentVersion</code>.
     * @return An <code>ImageIcon</code>.
     */
    public ImageIcon getIcon(final DocumentVersion version) {
        return getFileIcon(ArtifactVersionUtil.getNameExtension(version));
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
     * Obtain an icon for a file name extension.
     * 
     * @param extension
     *            A file name extension <code>String</code>.
     * @return An <code>ImageIcon</code>.
     */
    private ImageIcon getFileIcon(final String extension) {
        // The name has the "." removed.
        final String name = StringUtil.searchAndReplace(extension,
                Separator.Period, Separator.EmptyString).toUpperCase();
        
        // Return the icon from the cache if available, otherwise
        // get the icon from the system and cache it.
        if ((null==name) || (name.length()==0)) {
            return null;
        } else if (isCached(ICON_CACHE, name)) {
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

    /**
     * Read from the cache.
     * 
     * @param cache
     *            The cache.
     * @param cacheKey
     *            The cache key.
     * @return The cached object.
     */
    private Object read(final Map<String, Object> cache, final String cacheKey) {
        synchronized (cache) {
            return cache.get(cacheKey);
        }
    }
    
    /**
     * Read the icon from the system if possible.
     */
    private ImageIcon readSystemIcon(final String extension) {
        if (null != associationService) {
            final Association assoc =
                associationService.getFileExtensionAssociation(extension);
            if (null != assoc) {
                final String iconSpec = assoc.getIconFileName();
                if (null != iconSpec) {
                    final Image image = IconService.getIcon(iconSpec, 16);
                    if (null != image) {
                        return new ImageIcon(image);
                    }
                }
            }
        }
        return null;
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
    
    /** All pre-configured file icons. Some common icons aren't read
     *  properly by the jdic library so they are provided here. */
    public enum FileIcon {
        FILE_DEFAULT("FILE_DEFAULT", "IconFileDefault.png"),
        PDF("PDF", "IconFilePdf.png"),
        TXT("TXT", "IconFileTxt.png");
        
        /** The icon file name. */
        private final String iconFileName;
        
        /** The icon name. */
        private final String iconName;

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
