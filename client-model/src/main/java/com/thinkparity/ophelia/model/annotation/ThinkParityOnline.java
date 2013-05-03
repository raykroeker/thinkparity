/*
 * Created On:  9-Jan-07 1:51:45 PM
 */
package com.thinkparity.ophelia.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Title:</b>thinkParity Online<br>
 * <b>Description:</b>Defines an online requiredment for an OpheliaModel
 * implementation method.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ThinkParityOnline {}
