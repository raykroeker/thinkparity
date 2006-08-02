/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.audit.HistoryItem;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Utility convenience class for sorting lists.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelSorter {

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

	public static void sortMessages(final List<SystemMessage> list,
            final Comparator<SystemMessage> comparator) {
		Collections.sort(list, comparator);
	}

	/**
	 * Create a ModelSorter [Singleton]
	 * 
	 */
	private ModelSorter() { super(); }
}
