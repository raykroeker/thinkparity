/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.model.document.WorkingVersion;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.sort.AbstractArtifactComparator;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.HasBeenSeenComparator;
import com.thinkparity.model.parity.model.sort.UpdatedOnComparator;
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
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	public static ContentProvider getHistoryProvider() {
		synchronized(singletonLock) { return singleton.doGetHistoryProvider(); }
	}

	/**
	 * Obtain the document content provider.
	 * 
	 * @return The document content provider.
	 */
	public static ContentProvider getMainDocumentProvider() {
		synchronized(singletonLock) { return singleton.doGetDocumentProvider(); }
	}

	public static ContentProvider getMainMessageProvider() {
		synchronized(singletonLock) { return singleton.doGetSystemMessagesProvider(); }
	}

	public static ContentProvider getMainProvider() {
		synchronized(singletonLock) { return singleton.doGetMainProvider(); }
	}

	public static ContentProvider getSendArtifactProvider() {
		synchronized(singletonLock) { return singleton.doGetSendArtifactProvider(); }
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
	 * System message interface.
	 * 
	 */
	protected final SystemMessageModel systemMessageModel;

	/**
	 * The document history provider.
	 * 
	 */
	private final ContentProvider historyProvider;

	/**
	 * The document provider.
	 * 
	 */
	private final ContentProvider mainDocumentProvider;

	private final ContentProvider mainKeyProvider;

	/**
	 * The system message provider.
	 * 
	 */
	private final ContentProvider mainMessageProvider;

	/**
	 * The main provider. Is a composite provider consisting of a document
	 * list provider as well as a message list provider.
	 * 
	 */
	private final ContentProvider mainProvider;

	/**
	 * Send artifact provider.
	 * 
	 */
	private final ContentProvider sendArtifactProvider;

	private final ContentProvider sendArtifactRosterProvider;

	private final ContentProvider sendArtifactTeamProvider;

	private final ContentProvider sendArtifactVersionProvider;

	/**
	 * Create a ProviderFactory.
	 * 
	 */
	private ProviderFactory() {
		super();
		final ModelFactory modelFactory = ModelFactory.getInstance();
		this.documentModel = modelFactory.getDocumentModel(getClass());
		this.sessionModel = modelFactory.getSessionModel(getClass());
		this.systemMessageModel = modelFactory.getSystemMessageModel(getClass());
		this.historyProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				final Long documentId = (Long) input;
				Collection<HistoryItem> history;
				try { history = documentModel.readHistory(documentId); }
				catch(final ParityException px) {
					logger.error("Could not obtain the document history.", px);
					history = Collections.emptyList();
				}
				return history.toArray(new HistoryItem[] {});
			}
		};
		this.logger = ModelLoggerFactory.getLogger(getClass());
		this.mainDocumentProvider = new FlatContentProvider() {
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
		this.mainKeyProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				List<Long> keys;
				try { keys = sessionModel.getArtifactKeys(); }
				catch(final ParityException px) {
					logger.error("Cannot obtain main keys.", px);
					keys = Collections.<Long>emptyList();
				}
				return keys.toArray(new Long[] {});
			}
		};
		this.mainMessageProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				try {
					final Collection<SystemMessage> messages =
						systemMessageModel.read();
					return messages.toArray(new SystemMessage[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.mainProvider = new CompositeFlatContentProvider() {
			private final ContentProvider[] contentProviders = new ContentProvider[] {mainDocumentProvider, mainMessageProvider, mainKeyProvider};
			public Object[] getElements(final Integer index, final Object input) {
				Assert.assertNotNull("Index cannot be null.", index);
				Assert.assertTrue(
						"Index must lie within [0," + (contentProviders.length - 1) + "]",
						index >= 0 && index < contentProviders.length);
				return ((FlatContentProvider) getProvider(index)).getElements(input);
			}
			private FlatContentProvider getProvider(final Integer index) {
				return (FlatContentProvider) contentProviders[index];
			}
		};
		this.sendArtifactRosterProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				Assert.assertNotNull(
						"The send artifact roster provider requires an artifact id:  java.lang.Long",
						input);
				Assert.assertOfType(
						"The send artifact roster provider requires an artifact id:  java.lang.Long",
						Long.class,
						input);
				Collection<User> roster;
				try {
					final User[] team =
						(User[]) ((FlatContentProvider) sendArtifactTeamProvider).getElements(input);
					roster = sessionModel.getRosterEntries();
					// remove all team members from the roster list
					for(final User user : team)
						roster.remove(user);
				}
				catch(final ParityException px) {
					logger.error("Cannot obtain the user's roster.", px);
					roster = new Vector<User>(0);
				}
				return roster.toArray(new User[] {});
			}
		};
		this.sendArtifactTeamProvider = new FlatContentProvider() {
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
					team = sessionModel.getSubscriptions(artifactId);
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
		this.sendArtifactVersionProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				Assert.assertNotNull(
						"The send artifact version provider requires an artifact id:  java.lang.Long.",
						input);
				Assert.assertOfType(
						"The send artifact version provider requires an artifact id:  java.lang.Long.",
						Long.class, input);
				final Long artifactId = (Long) input;
				final List<DocumentVersion> versions = new LinkedList<DocumentVersion>();
				try {
					versions.addAll(documentModel.listVersions(artifactId));
					if(sessionModel.isLoggedInUserKeyHolder(artifactId)) {
						versions.add(0, WorkingVersion.getWorkingVersion());
					}
				}
				catch(final ParityException px) {
					logger.error("Could not obtain send artifact version list:  " + input, px);
				}
				return versions.toArray(new DocumentVersion[] {});
			}
		};
		this.sendArtifactProvider = new CompositeFlatContentProvider() {
			private final ContentProvider[] contentProviders = new ContentProvider[] {sendArtifactRosterProvider, sendArtifactTeamProvider, sendArtifactVersionProvider};
			public Object[] getElements(final Integer index, final Object input) {
				Assert.assertNotNull(
						"Index for composite content provider cannot be null.",
						index);
				Assert.assertTrue(
						"Index for the send artifact content provider mus lie within [0," + 2 + "]",
						index >= 0 && index <= 2);
				return getProvider(index).getElements(input);
			}
			private FlatContentProvider getProvider(final Integer index) {
				return (FlatContentProvider) contentProviders[index];
			}
		};

	}

	/**
	 * Obtain the document content provider.
	 * 
	 * @return The document content provider.
	 */
	private ContentProvider doGetDocumentProvider() { return mainDocumentProvider; }

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

	/**
	 * Obtain the system message provider.
	 * 
	 * @return The system message provider.
	 */
	private ContentProvider doGetSystemMessagesProvider() {
		return mainMessageProvider;
	}
}
