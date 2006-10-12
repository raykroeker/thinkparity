/*
 * Created On: 11-Oct-06 12:22:42 PM
 */
package com.thinkparity.ophelia.model.container.export;

import java.util.List;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PDFXMLContainer {

    String createdBy;

    String createdOn;

    String documentSum;

    String name;

    String userSum;

    List<PDFXMLContainerVersion> versions;

    String versionSum;

    /**
     * Create PDFXMLContainer.
     * 
     */
    PDFXMLContainer() {
        super();
    }
}
