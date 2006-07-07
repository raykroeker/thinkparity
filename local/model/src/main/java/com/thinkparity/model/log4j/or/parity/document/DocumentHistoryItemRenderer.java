/*
 * Created On: Jul 6, 2006 9:38:59 PM
 */
package com.thinkparity.model.log4j.or.parity.document;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.model.parity.model.document.DocumentHistoryItem;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class DocumentHistoryItemRenderer implements ObjectRenderer {

    /** Create DocumentHistoryItemRenderer. */
    public DocumentHistoryItemRenderer() { super(); }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     */
    public String doRender(final Object o) {
        if(null == o ) {
            return new StringBuffer(getClass().getName())
                    .append("//null")
                    .toString();
        }
        else {
            final DocumentHistoryItem item = (DocumentHistoryItem) o;
            final StringBuffer buffer = new StringBuffer(getClass().getName())
                .append("//id=").append(item.getId())
                .append(",documentId=").append(item.getDocumentId());
            if(item.isSetVersionId())
                buffer.append(",versionId=").append(item.getVersionId());
            return buffer.append(",date=").append(item.getDate().getTimeInMillis())
                    .append(",event=").append(item.getEvent())
                    .toString();
        }
    }

}
