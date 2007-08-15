/*
 * Created On:  23-May-07 4:21:13 PM
 */
package com.thinkparity.ophelia.model.help;

import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityConcurrency;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.concurrent.Lock;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity OpheliaModel Help Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.NEVER)
public interface HelpModel {

    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public HelpContent readContent(final Long id);

    public HelpTopic readTopic(final Long id);

    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public List<HelpTopic> readTopics();

    public List<Long> searchTopics(final String expression);
}
