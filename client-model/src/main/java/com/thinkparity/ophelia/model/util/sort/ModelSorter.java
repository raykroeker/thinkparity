/*
 * Feb 22, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.contact.IncomingInvitation;
import com.thinkparity.codebase.model.contact.OutgoingInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.message.SystemMessage;

/**
 * Utility convenience class for sorting lists.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelSorter {

    public static void sortReceipts(final List<ArtifactReceipt> list,
            final Comparator<ArtifactReceipt> comparator) {
        Collections.sort(list, comparator);
    }

    public static void sortContacts(final List<Contact> list,
            final Comparator<Contact> comparator) {
        Collections.sort(list, comparator);
    }

    public static void sortContainers(final List<Container> list,
            final Comparator<Artifact> comparator) {
        Collections.sort(list, comparator);
    }

    public static void sortContainerVersions(final List<ContainerVersion> list,
            final Comparator<ArtifactVersion> comparator) {
        Collections.sort(list, comparator);
    }

    public static void sortDocuments(final List<Document> list,
			final Comparator<Artifact> comparator) {
		Collections.sort(list, comparator);
	}

    public static void sortDocumentVersions(final List<DocumentVersion> list,
			final Comparator<ArtifactVersion> comparator) {
		Collections.sort(list, comparator);
	}

    /**
     * Sort a history.
     * 
     * @param history
     *            An artifact history.
     * @param comparator
     *            An artifact history comparator.
     */
    public static <T extends HistoryItem> void sortHistory(
            final List<T> history, final Comparator<? super T> comparator) {
        Collections.sort(history, comparator);
    }

    public static void sortIncomingContactInvitations(
            final List<IncomingInvitation> list,
            final Comparator<? super ContactInvitation> comparator) {
        Collections.sort(list, comparator);
    }

	public static void sortMessages(final List<SystemMessage> list,
            final Comparator<SystemMessage> comparator) {
		Collections.sort(list, comparator);
	}

    public static void sortOutgoingContactInvitations(
            final List<OutgoingInvitation> list,
            final Comparator<? super ContactInvitation> comparator) {
        Collections.sort(list, comparator);
    }

    public static void sortTeamMembers(final List<TeamMember> list,
            final Comparator<? super User> comparator) {
        Collections.sort(list, comparator);
    }

    public static void sortUsers(final List<User> list,
            final Comparator<User> comparator) {
        Collections.sort(list, comparator);
    }

	/**
	 * Create a ModelSorter [Singleton]
	 * 
	 */
	private ModelSorter() { super(); }
}
