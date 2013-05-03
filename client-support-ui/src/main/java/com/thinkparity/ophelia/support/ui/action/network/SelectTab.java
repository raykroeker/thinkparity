/*
 * Created On:  12-Dec-07 11:27:22 AM
 */
package com.thinkparity.ophelia.support.ui.action.network;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.AbstractAction;
import com.thinkparity.ophelia.support.ui.avatar.main.MainAvatar;
import com.thinkparity.ophelia.support.ui.avatar.network.NetworkAvatar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SelectTab extends AbstractAction {

    /** A default uri. */
    private static URI defaultURI;

    /**
     * Determine if this is the first invocation.
     * 
     * @return A <code>boolean</code>.
     */
    private static boolean isFirst() {
        if (null == defaultURI) {
            defaultURI = URI.create("http://www.google.com");
            return true;
        } else {
            return false;
        }
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
     * Create SelectTab.
     *
     */
    public SelectTab() {
        super("/network/selecttab");
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#invoke(com.thinkparity.ophelia.support.ui.Input)
     *
     */
    @Override
    public void invoke(final Input input) {
        if (isFirst()) {
            ((NetworkAvatar) getContext().lookupAvatar("/network/network")).setURI(defaultURI);
            ((NetworkAvatar) getContext().lookupAvatar("/network/network")).setProxyList(selectProxyList(defaultURI));
        }

        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                try {
                    ((MainAvatar) getContext().lookupAvatar("/main/main")).selectNetwork();
                    getContext().lookupAvatar("/network/network").reload();
                } catch (final Throwable t) {
                    t.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        });
    }

    /** <b>Title:</b>Select Tab Input Key<br> */
    public enum InputKey { URI }
}
