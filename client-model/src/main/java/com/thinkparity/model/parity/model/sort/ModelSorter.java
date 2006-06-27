/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * Utility convenience class for sorting lists.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelSorter {

    public static void sortContainers(final List<Container> list,
            final Comparator<Artifact> comparator) {
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

	public static void sortHistoryItems(final List<HistoryItem> list,
			final Comparator<HistoryItem> comparator) {
		Collections.sort(list, comparator);
	}

	public static void sortSystemMessages(final List<SystemMessage> list,
			final Comparator<SystemMessage> comparator) {
		Collections.sort(list, comparator);
	}

	/**
	 * Create a ModelSorter [Singleton]
	 * 
	 */
	private ModelSorter() { super(); }
}
