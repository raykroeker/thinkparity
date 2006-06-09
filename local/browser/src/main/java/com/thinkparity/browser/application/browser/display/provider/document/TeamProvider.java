/*
 * Created On: Jun 8, 2006 2:26:49 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.document;

import java.util.Set;

import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class TeamProvider extends FlatContentProvider {

    private final ArtifactModel aModel;

    /** Create TeamProvider. */
    public TeamProvider(final ArtifactModel aModel) {
        super();
        this.aModel = aModel;
    }

    /** @see com.thinkparity.browser.application.browser.display.provider.FlatContentProvider#getElements(java.lang.Object) */
    public Object[] getElements(final Object input) {
        final Set<User> team = aModel.readTeam((Long) input);
        return team.toArray(new User[] {});
    }
}
