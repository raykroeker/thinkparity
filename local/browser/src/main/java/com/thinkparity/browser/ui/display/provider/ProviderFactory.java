/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.ui.display.provider;

import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.parity.model.artifact.ComparatorBuilder;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProviderFactory {

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

	public static FlatContentProvider getUserProvider() {
		synchronized(singletonLock) { return singleton.doGetUserProvider(); }
	}

	/**
	 * Document model api.
	 * 
	 */
	protected final DocumentModel documentModel;

	/**
	 * Session model api.
	 * 
	 */
	protected final SessionModel sessionModel;

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
	 * User provider.
	 * 
	 */
	private final FlatContentProvider userProvider;

	/**
	 * Create a ProviderFactory.
	 * 
	 */
	private ProviderFactory() {
		super();
		documentModel = ModelFactory.getInstance().getDocumentModel(getClass());
		sessionModel = ModelFactory.getInstance().getSessionModel(getClass());
		this.documentProvider = new FlatContentProvider() {
			public Object[] getElements(Object input) {
				Assert.assertOfType("", UUID.class, input);
				final UUID projectId = (UUID) input;
				Collection<Document> documentList;
				try { documentList = documentModel.list(projectId); }
				catch(ParityException px) {
					// NOTE Error Handler Code
					documentList = new Vector<Document>(0);
				}
				return documentList.toArray(new Document[] {});
			}
		};
		this.historyProvider = new FlatContentProvider() {
			final ComparatorBuilder comparatorBuilder = new ComparatorBuilder();
			final Comparator<ParityObjectVersion> versionIdDescending =
				comparatorBuilder.createVersionById(Boolean.FALSE);
			public Object[] getElements(Object input) {
				final UUID documentId = (UUID) input;
				Collection<DocumentVersion> versionList;
				try {
					versionList = documentModel.listVersions(documentId,
							versionIdDescending);
				}
				catch(ParityException px) {
					// NOTE Error Handler Code
					versionList = new Vector<DocumentVersion>(0);
				}
				return versionList.toArray(new DocumentVersion[] {});
			}
		};
		this.userProvider = new FlatContentProvider() {
			public Object[] getElements(Object input) {
				final UUID documentId = (UUID) input;
				Collection<User> userList;
				try {
					userList = sessionModel.getRosterEntries();
//					TODO Implement model method here
//					userList.add(sessionModel.getTeamEntries(documentId));
				}
				catch(ParityException px) {
					// NOTE Error Handler Code
					userList = new Vector<User>(0);
				}
				return userList.toArray(new User[] {});
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

	/**
	 * Obtain the user provider.
	 * 
	 * @return The user provider.
	 */
	private FlatContentProvider doGetUserProvider() { return userProvider; }
}
