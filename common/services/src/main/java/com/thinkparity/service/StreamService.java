/*
 * Created On:  30-May-07 10:07:03 AM
 */
package com.thinkparity.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity Stream Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Stream")
public interface StreamService {

    @WebMethod
    StreamSession newDownstreamSession(AuthToken authToken,
            DocumentVersion version);

    @WebMethod
    StreamSession newDownstreamSession(AuthToken authToken, Product product,
            Release release);

    @WebMethod
    StreamSession newUpstreamSession(AuthToken authToken,
            StreamInfo streamInfo, DocumentVersion version);

    @WebMethod
    StreamSession newUpstreamSession(AuthToken authToken,
            StreamInfo streamInfo, Product product, Release release);
}
