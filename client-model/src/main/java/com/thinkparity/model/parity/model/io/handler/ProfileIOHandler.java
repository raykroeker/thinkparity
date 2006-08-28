/*
 * Created On: Jul 17, 2006 12:37:07 PM
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;

import com.thinkparity.model.parity.model.io.IOHandler;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public interface ProfileIOHandler extends IOHandler {
    public void create(final Profile profile, final List<ProfileEMail> emails);
    public void createEmail(final ProfileEMail email);
    public void deleteEmail(final Long profileId, final Long emailId);
    public Profile read(final JabberId jabberId);
    public ProfileEMail readEmail(final Long profileId, final Long emailId);
    public List<ProfileEMail> readEmails(final Long profileId);
    public void update(final Profile profile);
}
