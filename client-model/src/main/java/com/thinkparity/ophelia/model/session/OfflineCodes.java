/*
 * Created On:  14-Jun-07 9:08:39 AM
 */
package com.thinkparity.ophelia.model.session;

import java.util.Stack;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class OfflineCodes {

    private final Stack<OfflineCode> codes;

    /**
     * Create OfflineCodes.
     *
     */
    OfflineCodes() {
        super();
        this.codes = new Stack<OfflineCode>();
    }

    void clear() {
        codes.clear();
    }

    Boolean contains(final OfflineCode code) {
        return Boolean.valueOf(codes.contains(code));
    }

    Boolean isEmpty() {
        return Boolean.valueOf(codes.isEmpty());
    }

    OfflineCode peek() {
        return codes.peek();
    }

    OfflineCode pop() {
        return codes.pop();
    }

    void push(final OfflineCode code) {
        Assert.assertNotTrue(codes.contains(code), "Cannot use {0} again.", code);
        codes.push(code);
    }

    void remove(final OfflineCode code) {
        Assert.assertTrue(codes.contains(code), "Cannot remove {0}.", code);
        codes.remove(code);
    }

    int size() {
        return codes.size();
    }
}
