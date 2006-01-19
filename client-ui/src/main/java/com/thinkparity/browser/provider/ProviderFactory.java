/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.provider;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.project.Project;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProviderFactory {

	/**
	 * Document model api.
	 * 
	 */
	private static final DocumentModel documentModel;

	/**
	 * Singleton instance.
	 * 
	 */
	private static final ProviderFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		documentModel = ModelFactory.getInstance().getDocumentModel(ProviderFactory.class);
		singleton = new ProviderFactory();
		singletonLock = new Object();
	}

	/**
	 * Obtain the document content provider.
	 * 
	 * @return The document content provider.
	 */
	public static FlatContentProvider getDocumentProvider() {
		synchronized(singletonLock) {
			return singleton.doGetDocumentProvider();
		}
	}

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	public static FlatContentProvider getHistoryProvider() {
		synchronized(singletonLock) { return singleton.doGetHistoryProvider(); }
	}

	/**
	 * The document provider.
	 * 
	 */
	private final FlatContentProvider documentProvider;

	/**
	 * The document history provider.
	 * 
	 */
	private final FlatContentProvider historyProvider;

	/**
	 * Create a ProviderFactory.
	 */
	private ProviderFactory() {
		super();
		this.documentProvider = new FlatContentProvider() {
			public Object[] getElements(Object input) {
				final Project p = (Project) input;
				Collection<Document> documentList;
				try { documentList = documentModel.list(p.getId()); }
				catch(ParityException px) {
					// NOTE Error Handler Code
					documentList = new Vector<Document>(0);
				}
				return documentList.toArray(new Document[] {});
			}
		};
		this.historyProvider = new FlatContentProvider() {
			public Object[] getElements(Object input) {
				final Document d = (Document) input;
				Collection<DocumentVersion> versionList;
				try { versionList = documentModel.listVersions(d.getId()); }
				catch(ParityException px) {
					// NOTE Error Handler Code
					versionList = new Vector<DocumentVersion>(0);
				}
				return versionList.toArray(new DocumentVersion[] {});
			}
		};
	}

	/**
	 * Obtain the document content provider.
	 * 
	 * @return The document content provider.
	 */
	private FlatContentProvider doGetDocumentProvider() {
		return documentProvider;
	}

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	private FlatContentProvider doGetHistoryProvider() {
		return historyProvider;
	}
}
