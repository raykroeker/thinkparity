/*
 * Created On:  23-May-07 4:21:56 PM
 */
package com.thinkparity.ophelia.model.help;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.thinkparity.codebase.PropertiesUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity OpheliaModel Help Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpModelImpl extends Model implements HelpModel,
        InternalHelpModel {

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_ID;

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_IDS;

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_MOVIE;

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_NAME;

    /** An instance of <code>XStreamUtil</code>. */
    private static final XStreamUtil XSTREAM_UTIL;

    static {
        CFG_KEY_TOPIC_ID = "id";
        CFG_KEY_TOPIC_IDS = "topic-ids";
        CFG_KEY_TOPIC_NAME = "name";
        CFG_KEY_TOPIC_MOVIE = "movie";
        XSTREAM_UTIL = XStreamUtil.getInstance();
    }

    /** A help configuration file. */
    private Properties helpConfig;

    /**
     * Create HelpModelImpl.
     *
     */
    public HelpModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#readContent(java.lang.Long)
     *
     */
    public HelpContent readContent(Long id) {
        // NOCOMMIT HelpModelImpl.readContent NYI raymond@thinkparity.com 28-May-07
        throw Assert.createNotYetImplemented("");
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#readTopic(java.lang.Long)
     *
     */
    public HelpTopic readTopic(final Long id) {
        try {
            final Properties topicConfig = ConfigFactory.newInstance(resolveTopicPath(id));
            PropertiesUtil.verify(helpConfig, CFG_KEY_TOPIC_ID);
            PropertiesUtil.verify(helpConfig, CFG_KEY_TOPIC_MOVIE);
            PropertiesUtil.verify(helpConfig, CFG_KEY_TOPIC_NAME);

            final HelpTopic topic = new HelpTopic();
            topic.setId(Long.parseLong(topicConfig.getProperty(CFG_KEY_TOPIC_ID)));
            topic.setMovie(new URL(topicConfig.getProperty(CFG_KEY_TOPIC_MOVIE)));
            topic.setName(topicConfig.getProperty(CFG_KEY_TOPIC_NAME));
            return topic;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#readTopics()
     *
     */
    public List<HelpTopic> readTopics() {
        try {
            final List<Long> ids = readTopicIds();
            final List<HelpTopic> topics = new ArrayList<HelpTopic>(ids.size());
            for (final Long id : ids) {
                topics.add(readTopic(id));
            }
            return topics;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.help.InternalHelpModel#rebuildIndex()
     *
     */
    public void rebuildIndex() {
        // NOCOMMIT HelpModelImpl.rebuildIndex NYI raymond@thinkparity.com 28-May-07
        throw Assert.createNotYetImplemented("");
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#searchTopics(java.lang.String)
     *
     */
    public List<Long> searchTopics(final String expression) {
        // NOCOMMIT HelpModelImpl.searchTopics NYI raymond@thinkparity.com 28-May-07
        throw Assert.createNotYetImplemented("");
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        helpConfig = ConfigFactory.newInstance("help/index.properties");
        PropertiesUtil.verify(helpConfig, CFG_KEY_TOPIC_IDS);
    }

    private List<Long> readTopicIds() {
        final StringTokenizer tokenizer = new StringTokenizer(
                helpConfig.getProperty(CFG_KEY_TOPIC_IDS));
        final List<Long> ids = new ArrayList<Long>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            ids.add(Long.valueOf(tokenizer.nextToken()));
        }
        return ids;
    }

    private String resolveTopicPath(final Long id) {
        return MessageFormat.format("help/HelpTopic_1000.properties", id);
    }

}
