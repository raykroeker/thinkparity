/*
 * Created On:  21-Dec-07 3:47:09 PM
 */
package com.thinkparity.ophelia.model.workspace.configuration;

import com.thinkparity.codebase.constraint.DomainNameConstraint;
import com.thinkparity.codebase.constraint.IntegerConstraint;
import com.thinkparity.codebase.constraint.PortConstraint;
import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity Ophelia Model Proxy Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProxyConstraints {

    /** An instance of proxy constraints. */
    private static ProxyConstraints instance;

    /**
     * Obtain the proxy constraints.
     * 
     * @return A <code>ProxyConstraints</code>.
     */
    public static ProxyConstraints getInstance() {
        if (null == instance) {
            instance = new ProxyConstraints();
        }
        return instance;
    }

    /** The httpProxyHost constraint. */
    private final DomainNameConstraint httpProxyHost;

    /** The http proxy password constraint. */
    private final StringConstraint httpProxyPassword;

    /** The http proxy port constraint. */
    private final PortConstraint httpProxyPort;

    /** The http proxy username constraint. */
    private final StringConstraint httpProxyUsername;

    /** The socks proxy host constraint. */
    private final DomainNameConstraint socksProxyHost;

    /** The socks proxy port constraint. */
    private final PortConstraint socksProxyPort;

    /**
     * Create ProxyConstraints.
     *
     */
    private ProxyConstraints() {
        super();

        this.httpProxyHost = new DomainNameConstraint();
        this.httpProxyHost.setName("Http proxy host");
        this.httpProxyHost.setNullable(Boolean.FALSE);

        this.httpProxyUsername = new StringConstraint();
        this.httpProxyUsername.setMaxLength(Integer.MAX_VALUE);
        this.httpProxyUsername.setMinLength(1);
        this.httpProxyUsername.setName("Http proxy username");
        this.httpProxyUsername.setNullable(Boolean.TRUE);

        this.httpProxyPort = new PortConstraint();
        this.httpProxyPort.setName("Http proxy port");
        this.httpProxyPort.setNullable(Boolean.FALSE);

        this.httpProxyPassword = new StringConstraint();
        this.httpProxyPassword.setMaxLength(Integer.MAX_VALUE);
        this.httpProxyPassword.setMinLength(1);
        this.httpProxyPassword.setName("Password");
        this.httpProxyPassword.setNullable(Boolean.FALSE);

        this.socksProxyHost = new DomainNameConstraint();
        this.socksProxyHost.setName("Socks proxy host");
        this.socksProxyHost.setNullable(Boolean.FALSE);

        this.socksProxyPort = new PortConstraint();
        this.socksProxyPort.setName("Socks proxy port");
        this.socksProxyPort.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain the httpProxyHost.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getHttpProxyHost() {
        return httpProxyHost;
    }

    /**
     * Obtain the httpProxyPassword.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getHttpProxyPassword() {
        return httpProxyPassword;
    }

    /**
     * Obtain the httpProxyPort.
     *
     * @return A <code>IntegerConstraint</code>.
     */
    public IntegerConstraint getHttpProxyPort() {
        return httpProxyPort;
    }

    /**
     * Obtain the httpProxyUsername.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getHttpProxyUsername() {
        return httpProxyUsername;
    }

    /**
     * Obtain the socksProxyHost.
     *
     * @return A <code>DomainNameConstraint</code>.
     */
    public DomainNameConstraint getSocksProxyHost() {
        return socksProxyHost;
    }

    /**
     * Obtain the socksProxyPort.
     *
     * @return A <code>PortConstraint</code>.
     */
    public PortConstraint getSocksProxyPort() {
        return socksProxyPort;
    }
}
