/*
 * Nov 2, 2005
 */
package com.thinkparity.codebase.model.document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.4
 */
public class DocumentContent {

	/**
	 * Document content checksum.
	 */
	private String checksum;

	/**
	 * Document content bytes.
	 */
	private byte[] content;

	/**
	 * The document id.
	 * 
	 */
	private Long documentId;

	/**
	 * Create a DocumentContent.
	 * 
	 */
	public DocumentContent() { super(); }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof DocumentContent) {
			final DocumentContent dc = (DocumentContent) obj;
			return documentId.equals(dc.documentId)
					&& checksum.equals(dc.checksum);
		}
		return false;
	}

	/**
	 * Obtain the content checksum.
	 * 
	 * @return The content checksum.
	 */
	public String getChecksum() { return checksum; }

	/**
	 * Obtain the content.
	 * 
	 * @return The content.
	 */
	public byte[] getContent() { return content; }

	/**
	 * Obtain the document id.
	 * 
	 * @return The document id.
	 */
	public Long getDocumentId() { return documentId; }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return documentId.hashCode(); }

	/**
	 * Set the content checksum.
	 * 
	 * @param checksum
	 *            The content checksum.
	 */
	public void setChecksum(String checksum) { this.checksum = checksum; }

	/**
	 * Set the content.
	 * 
	 * @param content
	 *            The content.
	 */
	public void setContent(byte[] content) {
		this.content = new byte[content.length];
		System.arraycopy(content, 0, this.content, 0, content.length);
	}

	/**
	 * Set the document reference for this content.
	 * 
	 * @param document id
	 *            The document id.
	 */
	public void setDocumentId(final Long documentId) { this.documentId = documentId; }
}
