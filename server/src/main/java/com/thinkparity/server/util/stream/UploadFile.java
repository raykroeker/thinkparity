/*
 * Created On:  18-Oct-07 2:11:07 PM
 */
package com.thinkparity.desdemona.util.stream;

import com.thinkparity.codebase.model.stream.StreamConfiguration;
import com.thinkparity.codebase.model.stream.StreamRetryHandler;
import com.thinkparity.codebase.model.stream.StreamSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 * <b>Title:</b>thinkParity Desdemona Util Stream Upload File Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UploadFile extends
        com.thinkparity.codebase.model.stream.upload.UploadFile {

    static {
        final StreamConfiguration configuration = new StreamConfiguration();

        final HttpClient httpClient = new HttpClient();
        httpClient.setHttpConnectionManager(new MultiThreadedHttpConnectionManager());
        httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(3);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(7 * 1000);
        configuration.setHttpClient(httpClient);
    }

    /**
     * Create UploadFile.
     * 
     * @param retryHandler
     *            A <code>StreamRetryHandler</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public UploadFile(final StreamRetryHandler retryHandler,
            final StreamSession session) {
        super(retryHandler, session);
    }
}
