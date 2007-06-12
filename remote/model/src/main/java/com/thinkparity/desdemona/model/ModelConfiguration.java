/*
 * Created On:  21-Mar-07 11:45:48 AM
 */
package com.thinkparity.desdemona.model;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.util.DesdemonaProperties;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ModelConfiguration {

    private static final Log4JWrapper LOGGER;

    private static final String PREFIX;

    static {
        PREFIX = "thinkparity";
        LOGGER = new Log4JWrapper(ModelConfiguration.class);
    }

    public static ModelConfiguration getInstance(
            final Class<? extends AbstractModelImpl> model) {
        final String packageName = ModelConfiguration.class.getPackage().getName();
        final String modelPackageName = model.getPackage().getName();
        final String context = modelPackageName.replace(packageName,
                Separator.EmptyString.toString());
        return new ModelConfiguration(context);
    }

    private final String context;

    private final DesdemonaProperties properties;

    /**
     * Create ModelConfiguration.
     *
     */
    public ModelConfiguration(final String context) {
        super();
        this.context = context;
        this.properties = DesdemonaProperties.getInstance();
    }

    public String getConfiguration(final String key) {
        return getConfiguration(key, null);
    }

    private String contextualize(final String key) {
        return new StringBuilder(PREFIX).append(context).append(".").append(key).toString();
    }

    private String getConfiguration(final String key, final String defaultValue) {
        final String stringValue = properties.getProperty(
                LOGGER.logVariable("key", contextualize(key)));
        if (null == stringValue) {
            return LOGGER.logVariable("value", defaultValue);
        } else {
            return LOGGER.logVariable("value", stringValue);
        }
    }
}
