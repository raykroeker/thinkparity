/*
 * Created On: Sep 5, 2006 3:34:44 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.InputStream;

import com.thinkparity.codebase.model.document.Document;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Draft Printer<br>
 * <b>Description:</b>A print interface for printing a container.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ContainerDraftPrinter {

    /**
     * Print a document for a container.
     *
     * @param document
     *      A <code>Document</code>.
     * @param content
     *      A document's content <code>InputStream</code>.
     */
    public void print(final Document document, final InputStream content);
}
