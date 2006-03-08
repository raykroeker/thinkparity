/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractIndexor;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.index.ArtifactIndex;
import com.thinkparity.model.xmpp.JabberId;

/**
 * Adds an ability to index documents.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentIndexor extends AbstractIndexor {

	/**
	 * Create a DocumentIndexor.
	 * 
	 * @param context
	 *            The parity context.
	 */
	DocumentIndexor(final Context context) { super(context); }

	void create(final JabberId createdBy, final Calendar createdOn,
			final Long id, final JabberId keyHolder, final String name,
			final List<JabberId> contacts) throws ParityException {
		final ArtifactIndex index = new ArtifactIndex();
		index.setCreatedBy(createdBy);
		index.setCreatedOn(createdOn);
		index.setId(id);
		index.setKeyHolder(keyHolder);
		index.setName(name);
		index.addAllContacts(contacts);

		getInternalIndexModel().index(index);
	}
}
