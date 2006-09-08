/*
 * Created On: Sep 3, 2006 12:10:00 PM
 */
package org.xdoclet.plugin.jivehandler;

import org.generama.JellyTemplateEngine;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class JiveHandlerPlugin extends QDoxPlugin {

    /**
     * Create JiveHandlerPlugin.
     * 
     * @param jellyTemplateEngine
     *            A generama <code>JellyTemplateEngine</code>.
     * @param metadataProvider
     *            A generama <code>MetadataProvider</code>.
     * @param writerMapper
     *            A generama <code>WriterMapper</code>.
     */
    public JiveHandlerPlugin(final JellyTemplateEngine jellyTemplateEngine,
            final QDoxCapableMetadataProvider metadataProvider,
            final WriterMapper writerMapper) throws ClassNotFoundException {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        setFilereplace("wildfire-handlers.xml");
        setMultioutput(false);
    }

    /**
     * @see org.generama.Plugin#shouldGenerate(java.lang.Object)
     */
    @Override
    public boolean shouldGenerate(final Object metadata) {
        final JavaClass javaClass = (JavaClass) metadata;
        final boolean isAHandler = javaClass.isA("org.jivesoftware.messenger.handler.IQHandler");
        final boolean isAnAbstractController = javaClass.isA("com.thinkparity.desdemona.wildfire.handler.AbstractHandler");
        final boolean isAbstract = javaClass.isAbstract();
        final boolean isInterface = javaClass.isInterface();
        if (!isAbstract && !isInterface) {
            if (isAHandler || isAnAbstractController) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
