/*
 * Created On: 7-Nov-06 10:42:10 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.main;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.document.DocumentUtil;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class FileIconReader {

    /** An instance of <code>DocumentUtil</code>. */
    private static final DocumentUtil DOCUMENT_UTIL;

    /** A cache of image icons. */
    private static final Map<String, Object> ICON_CACHE;

    /** An apache logger. */
    private static final Log4JWrapper slogger;

    static {
        DOCUMENT_UTIL = DocumentUtil.getInstance();
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

    /**
     * Create FileIconReader.
     *
     */
    public FileIconReader() {
        super();
    }

    /**
     * Obtain an icon for a document.
     * 
     * @param document.
     *            A <code>Document</code>.
     * @return An <code>Icon</code>.
     */
    public Icon getIcon(final Document document) {
        return getFileIcon(DOCUMENT_UTIL.getNameExtension(document));
    }

    /**
     * Obtain an icon for a document version.
     * 
     * @param version.
     *            A <code>DocumentVersion</code>.
     * @return An <code>Icon</code>.
     */
    public Icon getIcon(final DocumentVersion version) {
        return getFileIcon(DOCUMENT_UTIL.getNameExtension(version));
    }

    /**
     * Obtain an icon for a file name extension.
     * 
     * @param extension
     *            A file name extension <code>String</code>.
     * @return An <code>Icon</code>.
     */
    private Icon getFileIcon(final String extension) {
        // The name has the "." removed.
        final String name = StringUtil.searchAndReplace(extension,
                Separator.Period, Separator.EmptyString).toUpperCase();
        
        // Return the icon from the cache if available, otherwise
        // get the icon from the system and cache it.
        if ((null==name) || (name.length()==0)) {
            return null;
        } else if (isCached(ICON_CACHE, name)) {
            return (Icon) read(ICON_CACHE, name);
        } else {
            final Icon icon = readSystemIcon(extension);
            if (null != icon) {
                write(ICON_CACHE, name, icon);
                return icon;
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
     * Read a file icon from the cache.
     * 
     * @param icon
     *            The icon.
     * @return The <code>Icon</code>.
     */
    private Icon read(final FileIcon icon) {
        return (Icon) read(ICON_CACHE, icon.iconName);
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
    private Icon readSystemIcon(final String extension) {
        Icon icon = null;
        try {
             final File file = BrowserPlatform.getInstance().createTempFile("." + extension);
             FileSystemView view = FileSystemView.getFileSystemView();
             icon = view.getSystemIcon(file);
        } catch (final IOException iox) {
            throw new BrowserException("Cannot read system file icon.", iox);
        }

        return icon;
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

    /** All pre-configured file icons. */
    public enum FileIcon {
        FILE_DEFAULT("FILE_DEFAULT", "IconFileDefault.png");
        
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
