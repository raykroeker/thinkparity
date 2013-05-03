/*
 * Created On:  9-Jan-07 1:51:45 PM
 */
package com.thinkparity.desdemona.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity Authenticate<br>
 * <b>Description:</b>Defines authentication behaviour for the server model
 * api. If a method has been annotated with authenticate the innvocation handler
 * will call the appropriate authentication on the model prior to the actual
 * invocation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ThinkParityAuthenticate {

    /**
     * Determine the authentication type.
     * 
     * @return A <code>AuthenticationType</code>.
     */
    public AuthenticationType value();
}
