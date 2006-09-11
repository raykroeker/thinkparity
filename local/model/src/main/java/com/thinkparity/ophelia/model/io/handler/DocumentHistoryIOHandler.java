/*
 * Mar 3, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import java.io.File;
import java.util.List;

import com.thinkparity.ophelia.model.audit.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface DocumentHistoryIOHandler {
	public File archive(final Long documentId, final List<HistoryItem> history);
}
