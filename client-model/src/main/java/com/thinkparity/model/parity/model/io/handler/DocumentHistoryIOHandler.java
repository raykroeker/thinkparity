/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.io.File;
import java.util.List;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface DocumentHistoryIOHandler {
	public File archive(final Long documentId, final List<HistoryItem> history);
}
