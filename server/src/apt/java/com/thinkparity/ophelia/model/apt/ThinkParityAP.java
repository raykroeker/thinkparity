/*
 * Created On:  11-Jan-07 4:58:51 PM
 */
package com.thinkparity.ophelia.model.apt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.annotation.ThinkParityInterface;
import com.thinkparity.codebase.model.annotation.ThinkParityInterfaceMethod;
import com.thinkparity.codebase.model.annotation.ThinkParityInterfaceMethod.Visibility;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ThinkParityAP implements AnnotationProcessor {

    /** An <code>AnnotationProcessortEnvironment</code>. */
    private final AnnotationProcessorEnvironment env;

    private final Map<ClassDeclaration, List<MethodDeclaration>> externalInterfaces;

    /**
     * Create ThinkParityInterfaceAP.
     *
     */
    ThinkParityAP(final AnnotationProcessorEnvironment env) {
        super();
        this.env = env;
        this.externalInterfaces = new HashMap<ClassDeclaration, List<MethodDeclaration>>();
    }

    /**
     * @see com.sun.mirror.apt.AnnotationProcessor#process()
     *
     */
    public void process() {
        processExternalInterfaces();
    }

    private void processExternalInterfaces() {
        // populate external interfaces
        externalInterfaces.clear();
        for (final TypeDeclaration typeDeclaration : env.getSpecifiedTypeDeclarations()) {
            typeDeclaration.accept(new ExternalInterfaceVisitor());
        }
    }

    /**
     * <b>Title:</b><br>
     * <b>Description:</b><br>
     */
    private final class ExternalInterfaceMethodVisitor extends
            SimpleDeclarationVisitor {
        @Override
        public void visitMethodDeclaration(final MethodDeclaration d) {
            if (null != d.getAnnotation(ThinkParityInterfaceMethod.class)) {
                final ThinkParityInterfaceMethod a =
                        d.getAnnotation(ThinkParityInterfaceMethod.class);
                if (Visibility.EXTERNAL == a.value()) {
                    externalInterfaces.get(d.getDeclaringType()).add(d);
                }
            }
        }
    }

    /**
     * <b>Title:</b><br>
     * <b>Description:</b><br>
     */
    private final class ExternalInterfaceVisitor extends
            SimpleDeclarationVisitor {
        @Override
        public void visitClassDeclaration(final ClassDeclaration d) {
            if (null != d.getAnnotation(ThinkParityInterface.class)) {
                externalInterfaces.put(d, new ArrayList<MethodDeclaration>());
                for (final MethodDeclaration method : d.getMethods()) {
                    method.accept(new ExternalInterfaceMethodVisitor());
                }
            }
        }
    }
}
