/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.provider.contact.ManageContactsProvider;
import com.thinkparity.browser.application.browser.display.provider.document.HistoryProvider;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.model.document.WorkingVersion;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
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
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.contact.Contact;

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

	static {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		preferences = workspace.getPreferences();

		singleton = new ProviderFactory();
	}

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	public static ContentProvider getHistoryProvider() {
		return singleton.doGetHistoryProvider();
	}

	/**
	 * Obtain the document content provider.
	 * 
	 * @return The document content provider.
	 */
	public static ContentProvider getMainDocumentProvider() {
		return singleton.doGetDocumentProvider();
	}

	public static ContentProvider getMainMessageProvider() {
		return singleton.doGetSystemMessagesProvider();
	}

	public static ContentProvider getMainProvider() {
		return singleton.doGetMainProvider();
	}

	/**
	 * Obtain the manage contacts provider.
	 * 
	 * @return The manage contacts provider.
	 */
	public static ContentProvider getManageContactsProvider() {
		return singleton.doGetManageContactsProvider();
	}

	public static ContentProvider getSendArtifactProvider() {
		return singleton.doGetSendArtifactProvider();
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
	 * Manage contacts provider.
	 * 
	 */
	private final ContentProvider manageContactsProvider;

	private final ContentProvider sendArtifactArtifactContactProvider;

	private final ContentProvider sendArtifactContactProvider;

	/**
	 * Send artifact provider.
	 * 
	 */
	private final ContentProvider sendArtifactProvider;

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
		this.historyProvider = new HistoryProvider(documentModel);
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
			private final ContentProvider[] contentProviders = new ContentProvider[] {mainDocumentProvider, mainMessageProvider};
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
		this.manageContactsProvider = new ManageContactsProvider(sessionModel);
		this.sendArtifactContactProvider = new FlatContentProvider() {
			public Object[] getElements(final Object input) {
				List<Contact> roster;
				try {
					roster = sessionModel.readContacts();

					// remove all team members from the roster list
					final Contact[] team = (Contact[]) input;
					if(null != team) {
						for(final Contact contact : team)
							roster.remove(contact);
					}
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
				return roster.toArray(new Contact[] {});
			}
		};
		this.sendArtifactArtifactContactProvider = new FlatContentProvider() {
			final JabberId jabberId =
				JabberIdBuilder.parseUsername(preferences.getUsername());
			public Object[] getElements(final Object input) {
				Assert.assertNotNull(
						"The team provider requires an artifact id:  java.lang.Long.",
						input);
				Assert.assertOfType(
						"The team provider requires an artifact id:  java.lang.Long.",
						Long.class, input);
				final Long artifactId = (Long) input;
				List<Contact> artifactContacts;
				try {
					artifactContacts = sessionModel.readArtifactContacts(artifactId);
					Contact contact;
					for(final Iterator<Contact> i = artifactContacts.iterator(); i.hasNext();) {
						contact= i.next();
						if(contact.getId().equals(jabberId)) { i.remove(); }
					}
					return artifactContacts.toArray(new Contact[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
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
					return versions.toArray(new DocumentVersion[] {});
				}
				catch(final ParityException px) { throw new RuntimeException(px); }
			}
		};
		this.sendArtifactProvider = new CompositeFlatContentProvider() {
			private final ContentProvider[] contentProviders = new ContentProvider[] {sendArtifactContactProvider, sendArtifactArtifactContactProvider, sendArtifactVersionProvider};
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
	 * Obtain the manage contacts provider.
	 * 
	 * @return The manage contacts provider.
	 */
	private ContentProvider doGetManageContactsProvider() {
		return manageContactsProvider;
	}

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
