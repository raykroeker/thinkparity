/*
 * Created On: Sep 5, 2006 3:34:44 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.InputStream;

import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Version Printer<br>
 * <b>Description:</b>A print interface for printing a container.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ContainerVersionPrinter {

    /**
     * Print a document version for a container.
     *
     * @param documentVersion
     *      A <code>DocumentVersion</code>.
     * @param content
     *      A document version's content <code>InputStream</code>.
     */
    public void print(final DocumentVersion documentVersion, final InputStream content);
}
