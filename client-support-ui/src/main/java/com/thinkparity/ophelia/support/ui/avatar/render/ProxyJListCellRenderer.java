/*
 * Created On:  12-Dec-07 12:40:19 PM
 */
package com.thinkparity.ophelia.support.ui.avatar.render;

import java.awt.Component;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.MessageFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProxyJListCellRenderer extends DefaultListCellRenderer {

    /**
     * Obtain text for the proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>String</code>.
     */
    private static String toText(final Proxy proxy) {
        final StringBuilder pattern = new StringBuilder("{0}");
        final Object[] arguments;
        final InetSocketAddress proxyAddress;
        switch (proxy.type()) {
        case DIRECT:
            arguments = new Object[] { "None." };
            break;
        case HTTP:
            proxyAddress = (InetSocketAddress) proxy.address();
            arguments = new Object[] { "Http", proxyAddress.getHostName(),
                    String.valueOf(proxyAddress.getPort()) };

            pattern.append(":  {1}:{2}");
            break;
        case SOCKS:
            proxyAddress = (InetSocketAddress) proxy.address();
            arguments = new Object[] { "Socks", proxyAddress.getHostName(),
                    String.valueOf(proxyAddress.getPort()) };

            pattern.append(":  {1}:{2}");
            break;
        default:
            throw Assert.createUnreachable("Unsupported proxy type:  {0}", proxy.type());
        }
        return MessageFormat.format(pattern.toString(), arguments);
    }

    /**
     * Create ProxyJListCellRenderer.
     *
     */
    public ProxyJListCellRenderer() {
        super();
    }

    /**
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     *
     */
    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, toText((Proxy) value),
                index, isSelected, cellHasFocus);
    }
}
