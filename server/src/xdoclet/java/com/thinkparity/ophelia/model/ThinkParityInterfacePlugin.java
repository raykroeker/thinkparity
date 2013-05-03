/*
 * Created On:  10-Jan-07 2:17:44 PM
 */
package com.thinkparity.ophelia.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thinkparity.ophelia.model.ThinkParityUtils.Visibility;
import com.thinkparity.ophelia.model.qtags.ThinkParityInterfaceImpl;
import com.thinkparity.ophelia.model.qtags.ThinkParityInterfaceMethodImpl;
import com.thinkparity.ophelia.model.qtags.ThinkParityTransactionImpl;

import org.generama.QDoxCapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.JavaGeneratingPlugin;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * <b>Title:</b>XDoclet thinkParity Model Interface Plugin<br>
 * <b>Description:</b>XDoclet plugin for generating thinkParity model interface
 * source.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ThinkParityInterfacePlugin extends JavaGeneratingPlugin {

    /**
     * Create ThinkParityInterfacePlugin.
     * 
     * @param templateEngine
     *            A generama <code>VelocityTemplateEngine</code>.
     * @param metadataProvider
     *            A generama <code>QDoxCapableMetadataProvider</code>.
     * @param writerMapper
     *            A generama <code>WriterMapper</code>.
     */
    public ThinkParityInterfacePlugin(
            final VelocityTemplateEngine templateEngine,
            final QDoxCapableMetadataProvider metadataProvider,
            final WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        metadataProvider.getDocletTagFactory().registerTag(
                ThinkParityInterfaceImpl.NAME, ThinkParityInterfaceImpl.class);
        metadataProvider.getDocletTagFactory().registerTag(
                ThinkParityInterfaceMethodImpl.NAME, ThinkParityInterfaceMethodImpl.class);
        metadataProvider.getDocletTagFactory().registerTag(
                ThinkParityTransactionImpl.NAME, ThinkParityTransactionImpl.class);
        setMultioutput(true);
    }

    /**
     * @see org.generama.defaults.QDoxPlugin#shouldGenerate(java.lang.Object)
     *
     */
    @Override
    public boolean shouldGenerate(final Object metadata) {
        return ThinkParityUtils.isTagged((JavaClass) metadata,
                ThinkParityInterfaceImpl.NAME);
    }

    /**
     * @see org.generama.defaults.JavaGeneratingPlugin#getDestinationClassname(java.lang.Object)
     *
     */
    @Override
    public String getDestinationClassname(final Object metadata) {
        return ((JavaClass) metadata).getName().replace("Impl", "");
    }

    /**
     * @see org.generama.Plugin#getDestinationFilename(java.lang.Object)
     *
     */
    @Override
    public String getDestinationFilename(final Object metadata) {
        return getDestinationClassname(metadata) + ".java";
    }

    /**
     * @see org.generama.Plugin#populateContextMap(java.util.Map)
     *
     */
    @Override
    protected void populateContextMap(final Map map) {
        super.populateContextMap(map);
        final ThinkParityInterfacePluginContext tpi = new ThinkParityInterfacePluginContext();
        map.put("tpi", tpi);
    }

    public List<JavaMethod> getInterfaceMethods(final JavaClass javaClass) {
        final JavaMethod[] javaMethods = javaClass.getMethods();
        final List<JavaMethod> interfaceMethods = new ArrayList<JavaMethod>(javaMethods.length);
        for (final JavaMethod javaMethod : javaMethods) {
            if (ThinkParityUtils.isTagged(javaMethod, Visibility.EXTERNAL)) {
                interfaceMethods.add(javaMethod);
            }
        }
        return interfaceMethods;
    }
}
