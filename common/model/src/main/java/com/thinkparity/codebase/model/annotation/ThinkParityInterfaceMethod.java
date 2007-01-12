/*
 * Created On:  9-Jan-07 1:51:45 PM
 */
package com.thinkparity.codebase.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ThinkParityInterfaceMethod {

    /**
     * Define the visibility of the interface method. The default visibility
     * being internal.
     * 
     * @return The interface method <code>Visibility</code>.
     */
    public Visibility value() default Visibility.INTERNAL;

    /**
     * <b>Title:</b>thinkParity Interface Method<br>
     * <b>Description:</b><br>
     */
    public enum Visibility { EXTERNAL, INTERNAL }
}
