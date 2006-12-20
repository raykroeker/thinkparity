/*
 * Created On:  11-Nov-06 5:32:51 PM
 */
package com.thinkparity.codebase.model.util.xstream;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.model.contact.ContactVCard;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.user.UserVCard;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XStreamUserVCardTest extends XStreamTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "XStream XMPP Event Test";

    /** The test datum <code>Fixture</code>. */
    private List<Fixture> data;


    /**
     * Create ArtifactDraftCreatedEventTest.
     *
     */
    public XStreamUserVCardTest() {
        super(NAME);
    }

    /**
     * Test to and from xml.
     *
     */
    public void testXML() {
        for (final Fixture datum : data) {
            final StringWriter writer = new StringWriter();
            XStreamUtil.getInstance().toXML(datum.vcard, writer);
            final StringReader reader = new StringReader(writer.toString());
            XStreamUtil.getInstance().fromXML(reader, datum.vcard2);
            XStreamTestCase.assertEquals(datum.vcard, datum.vcard2);
        }
    }

    /**
     * @see com.thinkparity.codebase.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        data = new ArrayList<Fixture>();
        data.add(setUp(new ProfileVCard(), new ProfileVCard()));
        data.add(setUp(new ContactVCard(), new ContactVCard()));
    }

    /**
     * @see com.thinkparity.codebase.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        data.clear();
        data = null;
        super.tearDown();
    }

    private Fixture setUp(final ProfileVCard vcard, final ProfileVCard vcard2) {
        return new Fixture(vcard, vcard2);
    }

    private Fixture setUp(final ContactVCard vcard, final ContactVCard vcard2) {
        return new Fixture(vcard, vcard2);
    }

    private static final class Fixture extends XStreamTestCase.Fixture {
        private final UserVCard vcard;
        private UserVCard vcard2;
        private Fixture(final UserVCard vcard, final UserVCard vcard2) {
            this.vcard = vcard;
            this.vcard.setCity("city");
            this.vcard.setCountry("country");
            this.vcard.setMobilePhone("mobile phone");
            this.vcard.setName("name");
            this.vcard.setOrganization("organization");
            this.vcard.setPhone("phone");
            this.vcard.setTitle("title");
            this.vcard2 = vcard2;
        }
    }
}

