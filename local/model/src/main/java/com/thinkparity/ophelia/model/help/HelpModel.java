/*
 * Created On:  23-May-07 4:21:13 PM
 */
package com.thinkparity.ophelia.model.help;

import java.util.List;

/**
 * <b>Title:</b>thinkParity OpheliaModel Help Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface HelpModel {

    public HelpTopic readHelpTopic(final Long id);

    public List<HelpTopic> readHelpTopics();

    public List<Long> search(final String expression);
}
