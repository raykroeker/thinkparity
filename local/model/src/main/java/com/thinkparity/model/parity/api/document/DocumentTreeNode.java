/*
 * Mar 27, 2005
 */
package com.thinkparity.model.parity.api.document;

import com.thinkparity.model.parity.model.tree.DataTreeNode;

public class DocumentTreeNode extends DataTreeNode {

	public DocumentTreeNode(final Document document) {
		super(document.getId().toString(), NodeType.Document, document
				.getCustomName(), document.getMetaDataFile());
	}
}
