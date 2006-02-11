/*
 * Feb 6, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.io.IOHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface DocumentIOHandler extends IOHandler {

	public void create(final Document document, final DocumentContent content);

	public void createVersion(final DocumentVersion version, final DocumentVersionContent versionContent);

	public void delete(final Long documentId);

	public void deleteVersion(final Long documentId, final Long versionId);

	public Document get(final Long documentId);
	
	public Document get(final UUID documentUniqueId);
	
	public DocumentContent getContent(final Long documentId);
	
	public DocumentVersion getVersion(final Long documentId,
			final Long versionId);
	
	public DocumentVersionContent getVersionContent(final Long documentId,
			final Long versionId);
	
	public List<Document> list();
	
	public List<DocumentVersion> listVersions(final Long documentId);
	
	public void update(final Document document);
	
	public void updateContent(final DocumentContent content);
}
