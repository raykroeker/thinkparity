/*
 * Created On:  1-Jun-07 5:11:19 PM
 */
package org.xdoclet.plugin.xfireservices;

import org.generama.JellyTemplateEngine;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XFireServicesPlugin extends QDoxPlugin {

    /**
     * Create XFireServicesPlugin.
     *
     */
    public XFireServicesPlugin(final JellyTemplateEngine jellyTemplateEngine,
            final QDoxCapableMetadataProvider metadataProvider,
            final WriterMapper writerMapper) throws ClassNotFoundException {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        setFilereplace("services.xml");
        setMultioutput(false);
    }

    /**
     * @see org.generama.Plugin#shouldGenerate(java.lang.Object)
     * 
     */
    @Override
    public boolean shouldGenerate(final Object metadata) {
        final JavaClass javaClass = (JavaClass) metadata;
        final boolean isServiceEndpoint = javaClass.isA("com.thinkparity.desdemona.web.service.ServiceSEI");
        final boolean isAbstract = javaClass.isAbstract();
        if (isServiceEndpoint && !isAbstract) {
            return true;
        } else {
            return false;
        }
    }
}
