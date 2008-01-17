/*
 * Created On:  12-Dec-07 12:58:48 PM
 */
package com.thinkparity.ophelia.support.ui.action.network;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.AbstractAction;
import com.thinkparity.ophelia.support.ui.avatar.network.NetworkAvatar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReloadTab extends AbstractAction {

    /**
     * Extract the uri from the input.
     * 
     * @param input
     *            An <code>Input</code>.
     * @return A <code>URI</code>.
     */
    private static URI extractURI(final Input input) {
        return (URI) input.get(InputKey.URI);
    }

    /**
     * Select a proxy list for a uri.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @return A <code>List<Proxy></code>.
     */
    private static List<Proxy> selectProxyList(final URI uri) {
        return ProxySelector.getDefault().select(uri);
    }

    /**
     * Create Reload.
     *
     */
    public ReloadTab() {
        super("/network/reloadtab");
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#invoke(com.thinkparity.ophelia.support.ui.Input)
     *
     */
    @Override
    public void invoke(final Input input) {
        final URI uri = extractURI(input);
        ((NetworkAvatar) getContext().lookupAvatar("/network/network")).setURI(uri);
        ((NetworkAvatar) getContext().lookupAvatar("/network/network")).setProxyList(selectProxyList(uri));

        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                try {
                    getContext().lookupAvatar("/network/network").reload();
                } catch (final Throwable t) {
                    t.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        });
    }

    /** <b>Title:</b>Reload Tab Input Key<br> */
    public enum InputKey { PROXY_LIST, URI }
}
