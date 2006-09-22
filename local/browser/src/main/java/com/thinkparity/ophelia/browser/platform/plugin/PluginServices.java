/*
 * Created On: Sep 21, 2006 3:42:20 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JHelper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PluginServices implements Cloneable {

    private final Logger logger;

    private PluginExtensionMetaInfo extensionMetaInfo;

    private PluginMetaInfo metaInfo;

    private PluginModelFactory modelFactory;

    PluginServices() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        final PluginServices clone = (PluginServices) super.clone();
        clone.extensionMetaInfo = extensionMetaInfo;
        clone.metaInfo = metaInfo;
        clone.modelFactory = modelFactory;
        return clone;
    }

    public void logWarning(final String warnPattern,
            final Object... warnArguments) {
        logger.warn(Log4JHelper.renderAndFormat(logger, warnPattern,
                warnArguments));
    }

    public void logWarning(final Throwable t, final String warnPattern,
            final Object... warnArguments) {
        logger.warn(Log4JHelper.renderAndFormat(logger, warnPattern,
                warnArguments), t);
    }

    public void logTrace(final String tracePattern,
            final Object... traceArguments) {
        if (logger.isTraceEnabled()) {
            logger.warn(Log4JHelper.renderAndFormat(logger, tracePattern,
                    traceArguments));
        }
    }

    public <V> V logVariable(final String name, final V value) {
        if (logger.isDebugEnabled()) {
            logger.warn(Log4JHelper.renderAndFormat(logger, "{0}:{1}", name,
                    value));
        }
        return value;
    }

    /**
     * Translate an error into a plugin error.
     * 
     * @param cause
     *            The cause of the error.
     * @return A plugin error.
     */
    public RuntimeException translateError(final Throwable cause) {
        if (PluginException.class.isAssignableFrom(cause.getClass())) {
            return (PluginException) cause;
        } else {
            final Object errorId = getErrorId(cause);
            logger.error(errorId, cause);
            return PluginException.translate(errorId.toString(), cause);
        }
    }

    /**
     * Obtain an error id.
     * 
     * @param cause
     *            The cause of the error.
     * @return The error id.
     */
    private Object getErrorId(final Throwable cause) {
        return MessageFormat.format("{0} - {1} - {2} - {3}.{4}({5}:{6})",
                metaInfo.getPluginName(),
                extensionMetaInfo.getExtensionName(),
                cause.getLocalizedMessage(),
                StackUtil.getFrameClassName(2),
                StackUtil.getFrameMethodName(2),
                StackUtil.getFrameFileName(2),
                StackUtil.getFrameLineNumber(2));
    }

    public PluginExtensionMetaInfo getExtensionMetaInfo() {
        return extensionMetaInfo;
    }

    public PluginMetaInfo getMetaInfo() {
        return metaInfo;
    }

    public PluginModelFactory getModelFactory() {
        return modelFactory;
    }

    void setExtensionMetaInfo(final PluginExtensionMetaInfo extensionMetaInfo) {
        this.extensionMetaInfo = extensionMetaInfo;
    }

    void setMetaInfo(final PluginMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    void setModelFactory(final PluginModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }
}
