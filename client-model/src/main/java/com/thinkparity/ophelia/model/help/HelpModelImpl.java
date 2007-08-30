/*
 * Created On:  23-May-07 4:21:56 PM
 */
package com.thinkparity.ophelia.model.help;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.thinkparity.codebase.PropertiesUtil;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.l10n.LocaleManager;

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
public final class HelpModelImpl extends Model<EventListener> implements
        HelpModel, InternalHelpModel {

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_ID;

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_IDS;

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_MOVIES;

    /** Help configuration key for the topic ids. */
    private static final String CFG_KEY_TOPIC_NAME;

    /** Help configuration key for the movie id. */
    private static final String CFG_KEY_MOVIE_ID;

    /** Help configuration key for the movie title. */
    private static final String CFG_KEY_MOVIE_TITLE;

    /** Help configuration key for the movie url. */
    private static final String CFG_KEY_MOVIE_URL;

    static {
        CFG_KEY_TOPIC_ID = "id";
        CFG_KEY_TOPIC_IDS = "topic-ids";
        CFG_KEY_TOPIC_NAME = "name";
        CFG_KEY_TOPIC_MOVIES = "movies";
        CFG_KEY_MOVIE_ID = "id";
        CFG_KEY_MOVIE_TITLE = "title";
        CFG_KEY_MOVIE_URL = "url";
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
     * @see com.thinkparity.ophelia.model.help.InternalHelpModel#deleteIndex()
     *
     */
    public void deleteIndex() {
        try {
            final String isIndexed = configurationIO.read(ConfigurationKeys.IS_INDEXED);
            if (null == isIndexed) {
                logger.logWarning("Help index does not exist.");
            } else {
                configurationIO.delete(ConfigurationKeys.IS_INDEXED);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#readContent(java.lang.Long)
     *
     */
    public HelpContent readContent(final Long id) {
        try {
            final InputStream inputStream = ResourceUtil.getLocalizedInputStream(
                    resolveContentBaseName(id), LocaleManager.getLocale());
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
            content.setContent(0 == buffer.toString().trim().length()
                    ? null : buffer.toString());
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
            final ResourceBundle topicBundle = getLocalizationBundle(
                    resolveTopicBundleBaseName(id));
            if (topicBundle.containsKey(CFG_KEY_TOPIC_ID)) {
                Assert.assertTrue("Topic does not contain id.",
                        topicBundle.containsKey(CFG_KEY_TOPIC_ID));
                Assert.assertTrue("Topic does not contain name.",
                        topicBundle.containsKey(CFG_KEY_TOPIC_NAME));

                final HelpTopic topic = new HelpTopic();
                topic.setId(Long.parseLong(topicBundle.getString(CFG_KEY_TOPIC_ID)));
                if (topicBundle.containsKey(CFG_KEY_TOPIC_MOVIES)) {
                    final StringTokenizer tokenizer = new StringTokenizer(
                            topicBundle.getString(CFG_KEY_TOPIC_MOVIES), ",");
                    final List<HelpTopicMovie> movies = new ArrayList<HelpTopicMovie>(tokenizer.countTokens());
                    Long movieId;
                    ResourceBundle movieBundle;
                    HelpTopicMovie movie;
                    while (tokenizer.hasMoreTokens()) {
                        movieId = Long.parseLong(tokenizer.nextToken());
                        movieBundle = getLocalizationBundle(resolveMovieBundleBaseName(movieId));
                        movie = new HelpTopicMovie();
                        movie.setId(Long.parseLong(movieBundle.getString(CFG_KEY_MOVIE_ID)));
                        movie.setTitle(movieBundle.getString(CFG_KEY_MOVIE_TITLE));
                        movie.setURL(movieBundle.getString(CFG_KEY_MOVIE_URL));
                        movies.add(movie);
                    }
                    topic.setMovies(movies);
                }
                topic.setName(topicBundle.getString(CFG_KEY_TOPIC_NAME));
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
     * @see com.thinkparity.ophelia.model.help.HelpModel#searchTopics(java.lang.String)
     *
     */
    public List<Long> searchTopics(final String expression) {
        try {
            if (!isIndexed()) {
                index();
            }

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
     * Build the help index.
     *
     */
    private void index() {
        try {
            final boolean wasIndexed = isIndexed();

            final List<HelpTopic> topics = readTopics();
            for (final HelpTopic topic : topics) {
                getIndexModel().indexHelpTopic(topic.getId());
            }

            if (!wasIndexed) {
                configurationIO.create(ConfigurationKeys.IS_INDEXED,
                        Boolean.TRUE.toString());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Determine whether or not the help is indexed.
     * 
     * @return True if the help is indexed.
     */
    private boolean isIndexed() {
        // build the index if requried
        final String isIndexed = configurationIO.read(ConfigurationKeys.IS_INDEXED);
        if (null == isIndexed) {
            return false;
        } else {
            return Boolean.parseBoolean(isIndexed);
        }
    }

    /**
     * Read the help topic ids from the index.
     * 
     * @return A <code>List<Long></code> containing help topic ids.
     */
    private List<Long> readTopicIds() {
        final StringTokenizer tokenizer = new StringTokenizer(
                helpConfig.getProperty(CFG_KEY_TOPIC_IDS), ",");
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
    private String resolveContentBaseName(final Long id) {
        return new StringBuilder(18).append("help/HelpContent_")
            .append(id).toString();
    }

    /**
     * Resolve the help movie path for an id.
     * 
     * @param id
     *            A help topic id <code>Long</code>.
     * @return A help topic resource path <code>String</code>.
     */
    private String resolveMovieBundleBaseName(final Long id) {
        return new StringBuilder(18).append("help/HelpMovie_")
            .append(id).toString();
    }

    /**
     * Resolve the help topic path for an id.
     * 
     * @param id
     *            A help topic id <code>Long</code>.
     * @return A help topic resource path <code>String</code>.
     */
    private String resolveTopicBundleBaseName(final Long id) {
        return new StringBuilder(18).append("help/HelpTopic_")
            .append(id).toString();
    }

    private static class ConfigurationKeys {
        public static final String IS_INDEXED = "thinkparity.help.is-indexed";
    }
}
