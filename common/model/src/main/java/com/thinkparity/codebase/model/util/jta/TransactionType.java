/*
 * Created On:  10-Jan-07 10:40:27 AM
 */
package com.thinkparity.codebase.model.util.jta;

/**
 * <b>Title:</b>thinkParity Transaction Type<br>
 * <b>Description:</b>Defines enumerated transactional behaviour for
 * thinkParity model methods.<br> *
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum TransactionType { NEVER, REQUIRED, REQUIRES_NEW, SUPPORTED }
