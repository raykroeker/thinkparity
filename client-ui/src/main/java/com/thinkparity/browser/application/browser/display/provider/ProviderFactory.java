/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.RandomData;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.parity.model.artifact.AbstractArtifactComparator;
import com.thinkparity.model.parity.model.artifact.ComparatorBuilder;
import com.thinkparity.model.parity.model.artifact.HasBeenSeenComparator;
import com.thinkparity.model.parity.model.artifact.UpdatedOnComparator;
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
	public static ContentProvider getDocumentProvider() {
		synchronized(singletonLock) { return singleton.doGetDocumentProvider(); }
	}

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	public static ContentProvider getHistoryProvider() {
		synchronized(singletonLock) { return singleton.doGetHistoryProvider(); }
	}

	public static ContentProvider getMainProvider() {
		synchronized(singletonLock) { return singleton.doGetMainProvider(); }
	}

	public static ContentProvider getSystemMessageProvider() {
		synchronized(singletonLock) { return singleton.doGetSystemMessageProvider(); }
	}

	public static ContentProvider getSystemMessagesProvider() {
		synchronized(singletonLock) { return singleton.doGetSystemMessagesProvider(); }
	}

	public static ContentProvider getUserProvider() {
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
	private final ContentProvider documentProvider;

	/**
	 * The document history provider.
	 * 
	 */
	private final ContentProvider historyProvider;

	/**
	 * The main provider. Is a composite provider consisting of a document
	 * list provider as well as a message list provider.
	 * 
	 */
	private final ContentProvider mainProvider;

	private final ContentProvider systemMessageProvider;

	/**
	 * The system message provider.
	 * 
	 */
	private final ContentProvider systemMessagesProvider;

	/**
	 * User provider.
	 * 
	 */
	private final ContentProvider userProvider;

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
				try {
					// sort by:
					// 	+> hasBeenSeen ? true b4 false
					//	+> last update ? earlier b4 later
					//  +> name ? alpha order
					final AbstractArtifactComparator sort =
						new HasBeenSeenComparator(Boolean.FALSE);
					sort.add(new UpdatedOnComparator(Boolean.FALSE));
					sort.add(new ComparatorBuilder().createByName(Boolean.TRUE));
					documentList = documentModel.list(projectId, sort);
				}
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
		this.mainProvider = new CompositeFlatContentProvider() {
			public Object[] getElements(final Integer index, final Object input) {
				Assert.assertNotNull("Index cannot be null.", index);
				Assert.assertTrue(
						"Index must lie within [0," + 1 + "]",
						index >= 0 && index <= 1);
				return ((FlatContentProvider) getProvider(index)).getElements(input);
			}
			private ContentProvider getProvider(final Integer index) {
				if(0 == index) { return systemMessagesProvider; }
				else if(1 == index) { return documentProvider; }
				else { throw Assert.createUnreachable(""); }
			}
		};
		this.systemMessageProvider = new SingleContentProvider() {
			// RANDOMDATA System Message
			final RandomData randomData = new RandomData();
			public Object getElement(Object input) {
				return randomData.getSystemMessage();
			}
		};
		this.systemMessagesProvider = new FlatContentProvider() {
			// RANDOMDATA System Messages
			final RandomData randomData = new RandomData();
			public Object[] getElements(Object input) {
				return randomData.getSystemMessages();
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
	private ContentProvider doGetDocumentProvider() { return documentProvider; }

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	private ContentProvider doGetHistoryProvider() { return historyProvider; }

	/**
	 * Obtain the main provider.
	 * 
	 * @return The main provider.
	 */
	private ContentProvider doGetMainProvider() { return mainProvider; }

	private ContentProvider doGetSystemMessageProvider() {
		return systemMessageProvider;
	}

	/**
	 * Obtain the system message provider.
	 * 
	 * @return The system message provider.
	 */
	private ContentProvider doGetSystemMessagesProvider() {
		return systemMessagesProvider;
	}

	/**
	 * Obtain the user provider.
	 * 
	 * @return The user provider.
	 */
	private ContentProvider doGetUserProvider() { return userProvider; }
}
