/*
 * Created On:  14-Aug-07 4:57:54 PM
 */
package com.thinkparity.codebase.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thinkparity.codebase.model.util.concurrent.Lock;

/**
 * <b>Title:</b>thinkParity Model Concurrency<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ThinkParityConcurrency {

    /**
     * Define the locking for a method invocation.
     * 
     * @return A <code>Lock</code>.
     */
    Lock value();
}
