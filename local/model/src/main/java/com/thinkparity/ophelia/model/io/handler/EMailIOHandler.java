/*
 * Created On:  18-Apr-07 11:53:16 AM
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.ophelia.model.io.IOHandler;

/**
 * <b>Title:</b>thinkParity OpheliaModel EMail IO Handler<br>
 * <b>Description:</b>A temporary interface to the e-mail address table for the
 * migrator model so that the addresses can be mass updated.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @deprecated
 */
@Deprecated
public interface EMailIOHandler extends IOHandler {
    public List<EMail> read();
    public Long readId(final EMail email);
    public void update(final Long emailId, final EMail email);
}
