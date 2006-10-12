/*
 * Created On: 11-Oct-06 12:22:42 PM
 */
package com.thinkparity.ophelia.model.container.export;

import java.util.List;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PDFXMLContainerVersion {

    List<PDFXMLDocument> documents;

    String documentSum;

    String publishedBy;

    String publishedOn;

    List<PDFXMLUser> users;

    String userSum;

    String versionId;

    /**
     * Create PDFXMLContainer.
     * 
     */
    PDFXMLContainerVersion() {
        super();
    }
}
