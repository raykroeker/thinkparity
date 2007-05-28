/*
 * Created On:  23-May-07 4:21:56 PM
 */
package com.thinkparity.ophelia.model.help;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.thinkparity.codebase.PropertiesUtil;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.config.ConfigFactory;

import com.thinkparity.codebase.model.session.Environment;

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

    static {
        CFG_KEY_TOPIC_ID = "id";
        CFG_KEY_TOPIC_IDS = "topic-ids";
        CFG_KEY_TOPIC_NAME = "name";
        CFG_KEY_TOPIC_MOVIE = "movie";
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
    public HelpContent readContent(final Long id) {
        try {
            final InputStream inputStream = ResourceUtil.getInputStream(resolveContentPath(id));
            final Reader reader = new BufferedReader(new InputStreamReader(
                    inputStream, StringUtil.Charset.ISO_8859_1.getCharset()));
            final StringBuilder buffer = new StringBuilder(2048);
            try {
                int character;
                while (-1 != (character = reader.read())) {
                    buffer.append((char) character);
                }
            } finally {
                inputStream.close();
            }
            final HelpContent content = new HelpContent();
            content.setContent(buffer.toString());
            content.setId(id);
            return content;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#readTopic(java.lang.Long)
     *
     */
    public HelpTopic readTopic(final Long id) {
        try {
            final Properties topicConfig = ConfigFactory.newInstance(resolveTopicPath(id));
            if (topicConfig.containsKey(CFG_KEY_TOPIC_ID)) {
                PropertiesUtil.verify(topicConfig, CFG_KEY_TOPIC_ID);
                PropertiesUtil.verify(topicConfig, CFG_KEY_TOPIC_NAME);

                final HelpTopic topic = new HelpTopic();
                topic.setId(Long.parseLong(topicConfig.getProperty(CFG_KEY_TOPIC_ID)));
                if (topicConfig.containsKey(CFG_KEY_TOPIC_MOVIE))
                    topic.setMovie(new URL(topicConfig.getProperty(CFG_KEY_TOPIC_MOVIE)));
                topic.setName(topicConfig.getProperty(CFG_KEY_TOPIC_NAME));
                return topic;
            } else {
                return null;
            }
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
     * @see com.thinkparity.ophelia.model.help.InternalHelpModel#index()
     *
     */
    public void index() {
        try {
            final List<HelpTopic> topics = readTopics();
            for (final HelpTopic topic : topics) {
                getIndexModel().indexHelpTopic(topic.getId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#searchTopics(java.lang.String)
     *
     */
    public List<Long> searchTopics(final String expression) {
        try {
            return getIndexModel().searchHelpTopics(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
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

    /**
     * Read the help topic ids from the index.
     * 
     * @return A <code>List<Long></code> containing help topic ids.
     */
    private List<Long> readTopicIds() {
        final StringTokenizer tokenizer = new StringTokenizer(
                helpConfig.getProperty(CFG_KEY_TOPIC_IDS));
        final List<Long> ids = new ArrayList<Long>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            ids.add(Long.valueOf(tokenizer.nextToken()));
        }
        return ids;
    }

    /**
     * Resolve the help topic path for an id.
     * 
     * @param id
     *            A help topic id <code>Long</code>.
     * @return A help topic resource path <code>String</code>.
     */
    private String resolveContentPath(final Long id) {
        return new StringBuilder(18).append("help/HelpContent_")
            .append(id).toString();
    }

    /**
     * Resolve the help topic path for an id.
     * 
     * @param id
     *            A help topic id <code>Long</code>.
     * @return A help topic resource path <code>String</code>.
     */
    private String resolveTopicPath(final Long id) {
        return new StringBuilder(29).append("help/HelpTopic_")
            .append(id).append(".properties")
            .toString();
    }
}
