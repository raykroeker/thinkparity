/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.RandomData;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.AbstractArtifactComparator;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.artifact.ComparatorBuilder;
import com.thinkparity.model.parity.model.artifact.HasBeenSeenComparator;
import com.thinkparity.model.parity.model.artifact.UpdatedOnComparator;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProviderFactory {

	/**
	 * The parity preferences.
	 * 
	 */
	private static final Preferences preferences;

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
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		preferences = workspace.getPreferences();

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

	public static ContentProvider getSendArtifactProvider() {
		synchronized(singletonLock) { return singleton.doGetSendArtifactProvider(); }
	}

	public static ContentProvider getSystemMessageProvider() {
		synchronized(singletonLock) { return singleton.doGetSystemMessageProvider(); }
	}

	public static ContentProvider getSystemMessagesProvider() {
		synchronized(singletonLock) { return singleton.doGetSystemMessagesProvider(); }
	}

	/**
	 * Document model api.
	 * 
	 */
	protected final DocumentModel documentModel;

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

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

	private final ContentProvider rosterProvider;

	/**
	 * Send artifact provider.
	 * 
	 */
	private final ContentProvider sendArtifactProvider;

	private final ContentProvider systemMessageProvider;

	/**
	 * The system message provider.
	 * 
	 */
	private final ContentProvider systemMessagesProvider;

	private final ContentProvider teamProvider;

	/**
	 * Create a ProviderFactory.
	 * 
	 */
	private ProviderFactory() {
		super();
		documentModel = ModelFactory.getInstance().getDocumentModel(getClass());
		sessionModel = ModelFactory.getInstance().getSessionModel(getClass());
		this.documentProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				try {
					// sort by:
					// 	+> hasBeenSeen ? true b4 false
					//	+> last update ? earlier b4 later
					//  +> name ? alpha order
					final AbstractArtifactComparator sort =
						new HasBeenSeenComparator(Boolean.FALSE);
					sort.add(new UpdatedOnComparator(Boolean.FALSE));
					sort.add(new ComparatorBuilder().createByName(Boolean.TRUE));
					return documentModel.list(sort).toArray(new Document[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.historyProvider = new FlatContentProvider() {
			final ComparatorBuilder comparatorBuilder = new ComparatorBuilder();
			final Comparator<ArtifactVersion> versionIdDescending =
				comparatorBuilder.createVersionById(Boolean.FALSE);
			public Object[] getElements(Object input) {
				final Long documentId = (Long) input;
				Collection<DocumentVersion> versionList;
				try {
					versionList =
						documentModel.listVersions(
								documentId, versionIdDescending);
				}
				catch(final ParityException px) {
					logger.error("Could not obtain the document version list.", px);
					versionList = new Vector<DocumentVersion>(0);
				}
				return versionList.toArray(new DocumentVersion[] {});
			}
		};
		this.logger = ModelLoggerFactory.getLogger(getClass());
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
		this.rosterProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				Collection<User> roster;
				try { roster = sessionModel.getRosterEntries(); }
				catch(final ParityException px) {
					logger.error("Cannot obtain the user's roster.", px);
					roster = new Vector<User>(0);
				}
				return roster.toArray(new User[] {});
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
		this.teamProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				Assert.assertNotNull(
						"The team provider requires an artifact id:  java.lang.Long.",
						input);
				Assert.assertOfType(
						"The team provider requires an artifact id:  java.lang.Long.",
						Long.class, input);
				final Long artifactId = (Long) input;
				Collection<User> team;
				try {
					final User[] roster =
						(User[]) ((FlatContentProvider) rosterProvider).getElements(null);
					team = sessionModel.getSubscriptions(artifactId);
					// remove all roster members from the team
					for(final User rosterUser : roster) {
						team.remove(rosterUser);
					}
					final Iterator<User> iTeam = team.iterator();
					User user;
					final String loggedInUsername = new StringBuffer(preferences.getUsername())
						.append("@")
						.append(preferences.getServerHost())
						.toString();
					while(iTeam.hasNext()) {
						user = iTeam.next();
						if(user.getUsername().equals(loggedInUsername)) {
							iTeam.remove();
						}
					}
				}
				catch(final ParityException px) {
					logger.error("Could not obtain the team for the artifact:  "
							+ artifactId, px);
					team = new Vector<User>(0);
				}
				return team.toArray(new User[] {});
			}
		};
		this.sendArtifactProvider = new CompositeFlatContentProvider() {
			private final ContentProvider[] contentProviders = new
				ContentProvider[] {teamProvider, rosterProvider};
			public Object[] getElements(final Integer index, final Object input) {
				Assert.assertNotNull(
						"Index for composite content provider cannot be null.",
						index);
				Assert.assertTrue(
						"Index for the send artifact content provider mus lie within [0," + 1 + "]",
						index >= 0 && index <= 1);
				return ((FlatContentProvider) getProvider(index)).getElements(input);
			}
			private ContentProvider getProvider(final Integer index) {
				return contentProviders[index];
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

	/**
	 * Obtain the user provider.
	 * 
	 * @return The user provider.
	 */
	private ContentProvider doGetSendArtifactProvider() {
		return sendArtifactProvider;
	}

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
}
