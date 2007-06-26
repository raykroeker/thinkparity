/*
 * Created On:  26-Jun-07 8:55:25 AM
 */
package com.thinkparity.desdemona.web.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.session.SessionModel;
import com.thinkparity.desdemona.model.stream.StreamService;

/**
 * <b>Title:</b>thinkParity Web Stream Servlet<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WebStream extends HttpServlet {

    /** The size of the buffer to use when reading/writing. */
    private static final int BUFFER_SIZE;

    static {
        BUFFER_SIZE = 1024 * 1024 * 2;
    }

    /**
     * Obtain the session id from the request.
     * 
     * @param req
     *            A <code>HttpServletRequest</code>.
     * @return A <code>String</code>.
     */
    private static String getSessionId(final HttpServletRequest req) {
        return req.getHeader(HeaderNames.SESSION_ID);
    }

    /** The stream loader. */
    private ClassLoader loader;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** The stream service. */
    private final StreamService service;

    /** The session model. */
    private final SessionModel sessionModel;

    /**
     * Create WebStream.
     *
     */
    public WebStream() {
        super();
        this.logger = new Log4JWrapper();
        this.service = StreamService.getInstance();
        this.sessionModel = ModelFactory.getInstance(getLoader()).getSessionModel();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *
     */
    @Override
    protected void doGet(final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException,
            IOException {
        WebStreamMetrics.begin(req);
        try {
            final String sessionId = getSessionId(req);
            if (null == sessionId) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            final Session session = getSession(sessionId);
            if (null == session) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            final String pathInfo = req.getPathInfo();
            if (!isValidForGet(pathInfo)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            readStream(session, pathInfo, resp.getOutputStream());
        } catch (final Exception x) {
            logger.logError(x, "Error handling stream get.");
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } finally {
            WebStreamMetrics.end(req);
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *
     */
    @Override
    protected void doPut(final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException,
            IOException {
        WebStreamMetrics.begin(req);
        try {
            final String sessionId = getSessionId(req);
            if (null == sessionId) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            final Session session = getSession(sessionId);
            if (null == session) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            final String pathInfo = req.getPathInfo();
            if (!isValidForPut(pathInfo)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            writeStream(session, pathInfo, req.getInputStream());
        } catch (final Exception x) {
            logger.logError(x, "Error handling stream put.");
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } finally {
            WebStreamMetrics.end(req);
        }
    }

    /**
     * Allocate a buffer.
     * 
     * @return A <code>ByteBuffer</code>.
     */
    private ByteBuffer allocateBuffer() {
        return ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    /**
     * Obtain the stream loader.
     * 
     * @return A <code>ClassLoader</code>.
     */
    private ClassLoader getLoader() {
        if (null == loader) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return loader;
    }

    /**
     * Obtain the user's session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A <code>Session</code>.
     */
    private Session getSession(final String sessionId) {
        return sessionModel.readSession(sessionId);
    }

    /**
     * Determine if the path info is valid.
     * 
     * @param pathInfo
     *            A path info <code>String</code>.
     * @return True if the path info is not null.
     */
    private boolean isValid(final String pathInfo) {
        return null != pathInfo;
    }

    /**
     * Determine if the path info is valid for a get operation.
     * 
     * @param pathInfo
     *            A path info <code>String</code>.
     * @return True if the path info exists in the stream server.
     */
    private boolean isValidForGet(final String pathInfo) {
        return isValid(pathInfo) && service.doesExist(pathInfo).booleanValue();
    }

    /**
     * Determine if the path info is valid for a put operation.
     * 
     * @param pathInfo
     *            A path info <code>String</code>.
     * @return True if the path info does not exist in the stream server.
     */
    private boolean isValidForPut(final String pathInfo) {
        return isValid(pathInfo) && !service.doesExist(pathInfo).booleanValue();
    }

    /**
     * Write the stream.
     * 
     * @param session
     *            A <code>Sesssion</code>.
     * @param pathInfo
     *            A path info <code>String</code>.
     * @param stream
     *            An <code>InputStream</code>.
     */
    private void readStream(final Session session, final String pathInfo,
            final OutputStream stream) throws IOException {
        final ReadableByteChannel channel = service.openForDownstream(pathInfo);
        try {
            ByteBuffer buffer = allocateBuffer();
            try {
                StreamUtil.copy(channel, stream, buffer);
            } finally {
                releaseBuffer(buffer);
            }
        } finally {
            channel.close();
        }
    }

    /**
     * Release the buffer.
     * 
     * @param buffer
     *            A <code>ByteBuffer</code>.
     */
    private void releaseBuffer(ByteBuffer buffer) {
        buffer.clear();
        buffer = null;
    }

    /**
     * Write the stream.
     * 
     * @param session
     *            A <code>Sesssion</code>.
     * @param pathInfo
     *            A path info <code>String</code>.
     * @param stream
     *            An <code>InputStream</code>.
     */
    private void writeStream(final Session session, final String pathInfo,
            final InputStream stream) throws IOException {
        final WritableByteChannel channel = service.openForUpstream(pathInfo);
        try {
            ByteBuffer buffer = allocateBuffer();
            try {
                StreamUtil.copy(stream, channel, buffer);
            } finally {
                releaseBuffer(buffer);
            }
            service.finalize(pathInfo);
        } finally {
            channel.close();
        }
    }

    /** <b>Title:</b>Web Stream Header Names<br> */
    private static class HeaderNames {
        private static final String SESSION_ID = "Session-Id";
    }
}
