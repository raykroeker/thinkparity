/*
 * Created On:  9-Jan-07 1:51:45 PM
 */
package com.thinkparity.codebase.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Transaction<br>
 * <b>Description:</b>Defines transactional behaviour for thinkParity model
 * implementation method with a ThinkParityInterfaceMethod annotation. If used
 * at a class level it will apply to all methods unless overridden.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ThinkParityTransaction {

    /**
     * Determine the transaction type.
     * 
     * @return A <code>TransactionType</code>.
     */
    public TransactionType value();
}
