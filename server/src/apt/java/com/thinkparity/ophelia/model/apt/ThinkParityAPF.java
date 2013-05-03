/*
 * Created On:  11-Jan-07 4:59:52 PM
 */
package com.thinkparity.ophelia.model.apt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.model.annotation.ThinkParityInterface;
import com.thinkparity.codebase.model.annotation.ThinkParityInterfaceMethod;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ThinkParityAPF implements
        AnnotationProcessorFactory {

    private static final List<String> SUPPORTED_ANNOTATIONS;

    private static final List<String> SUPPORTED_OPTIONS;

    static {
        SUPPORTED_ANNOTATIONS = Collections.unmodifiableList(Arrays.asList(
                ThinkParityInterface.class.getName(),
                ThinkParityTransaction.class.getName(),
                ThinkParityInterfaceMethod.class.getName()));
        SUPPORTED_OPTIONS = Collections.unmodifiableList(Arrays.asList(
                "-Avelocity.resourcepath"));
    }

    /**
     * Create ThinkParityInterfaceFactory.
     *
     */
    public ThinkParityAPF() {
        super();
    }

    /**
     * @see com.sun.mirror.apt.AnnotationProcessorFactory#getProcessorFor(java.util.Set, com.sun.mirror.apt.AnnotationProcessorEnvironment)
     *
     */
    public AnnotationProcessor getProcessorFor(
            final Set<AnnotationTypeDeclaration> atds,
            final AnnotationProcessorEnvironment env) {
        if (atds.isEmpty())
            return AnnotationProcessors.NO_OP;
        return new ThinkParityAP(env);
    }

    /**
     * @see com.sun.mirror.apt.AnnotationProcessorFactory#supportedAnnotationTypes()
     *
     */
    public Collection<String> supportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    /**
     * @see com.sun.mirror.apt.AnnotationProcessorFactory#supportedOptions()
     *
     */
    public Collection<String> supportedOptions() {
        return SUPPORTED_OPTIONS;
    }
}
