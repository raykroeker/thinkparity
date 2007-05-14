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

    List<PDFXMLDocumentSummary> documentSummaries;

    String documentSum;

    String firstPublished;

    String lastPublished;

    String name;

    List<PDFXMLTeamMember> teamMembers;

    String teamMemberSum;

    String userSum;

    List<PDFXMLContainerVersion> versions;

    List<PDFXMLContainerVersionSummary> versionSummaries;

    String versionSum;

    /**
     * Create PDFXMLContainer.
     * 
     */
    PDFXMLContainer() {
        super();
    }
}
