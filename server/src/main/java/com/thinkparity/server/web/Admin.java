/*
 * Created On:  6-Jun-07 9:28:35 AM
 */
package com.thinkparity.desdemona.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.admin.AdminModelFactory;
import com.thinkparity.desdemona.model.session.SessionModel;

import com.thinkparity.desdemona.util.DesdemonaProperties;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Admin<br>
 * <b>Description:</b>A servlet that services localhost admin gets.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Admin extends HttpServlet {

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** A map valid path info strings to their handlers. */
    private static final List<String> VALID_PATHINFO_LIST;

    static {
        LOGGER = new Log4JWrapper(Admin.class);

        VALID_PATHINFO_LIST = new ArrayList<String>();
        VALID_PATHINFO_LIST.add("report");
    }

    /**
     * Determine if the path info is valid. If the path info is null, or if it
     * is not contained within a valid list.
     * 
     * @param pathInfo
     *            The path info <code>String</code>.
     * @return True if the path info is valid.
     */
    private static boolean isValid(final String pathInfo) {
        if (null == pathInfo)
            return false;
        return VALID_PATHINFO_LIST.contains(pathInfo.substring(1));
    }

    /** The properties. */
    private final Properties properties;

    /**
     * Create WebService.
     *
     */
    public Admin() {
        super();
        this.properties = DesdemonaProperties.getInstance();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *
     */
    @Override
    protected void doGet(final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException,
            IOException {
        WebServiceMetrics.begin(req);
        try {
            if (enabled()) {
                LOGGER.logInfo("Admin is enabled.");
                final String pathInfo = req.getPathInfo();
                if (!isValid(pathInfo)) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                final String username = req.getParameter("username");
                final String password = req.getParameter("password");
                final String adminPassword = req.getParameter("adminpassword");
                if (properties.getProperty("thinkparity.admin.adminpassword").equals(adminPassword)) {
                    LOGGER.logInfo("Generating admin report.");
                    final ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    final ModelFactory modelFactory = ModelFactory.getInstance(loader);
                    final Credentials credentials = new Credentials();
                    credentials.setPassword(password);
                    credentials.setUsername(username);
                    final SessionModel sessionModel = modelFactory.getSessionModel();
                    final AuthToken authToken = sessionModel.login(credentials);
                    final User user = sessionModel.readUser(authToken.getSessionId());
                    final AdminModelFactory adminModelFactory = AdminModelFactory.getInstance(user, loader);
                    adminModelFactory.newReportModel().generate();
                } else {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                LOGGER.logInfo("Admin is not enabled.");
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (final InvalidCredentialsException icx) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (final Throwable t) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            WebServiceMetrics.end(req);
        }
    }

    /**
     * Determine if admin is enabled.
     * 
     * @return True if it is enabled.
     */
    private boolean enabled() {
        return Boolean.valueOf(properties.getProperty(
                "thinkparity.admin.enableadmin", "false"));
    }
}
