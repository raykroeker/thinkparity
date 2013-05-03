/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactSql extends AbstractSql {

    /** Sql to create a contact. */
	private static final String SQL_CREATE =
		new StringBuilder("insert into TPSD_CONTACT ")
		.append("(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,")
        .append("UPDATED_ON) ")
		.append("values (?,?,?,?,?,?)")
		.toString();

    /** Sql to delete a contact. */
	private static final String SQL_DELETE =
		new StringBuilder("delete from TPSD_CONTACT ")
		.append("where (USER_ID=? and CONTACT_ID=?) ")
        .append("or (USER_ID=? and CONTACT_ID=?)")
		.toString();

    /** Sql to read contact ids. */
	private static final String SQL_READ_IDS =
		new StringBuilder("select UC.USERNAME \"CONTACT_USERNAME\" ")
		.append("from TPSD_CONTACT C ")
        .append("inner join TPSD_USER U on U.USER_ID=C.USER_ID ")
        .append("inner join TPSD_USER UC on UC.USER_ID=C.CONTACT_ID ")
		.append("where U.USER_ID=?")
		.toString();

	/**
     * Create ContactSql.
     * 
     */
	public ContactSql() {
        super();
	}

	/**
     * Create ContactSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
	public ContactSql(final DataSource dataSource) {
	    super(dataSource);
	}

    public void create(final User user, final User contact,
            final User createdBy, final Calendar createdOn) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_CREATE);
            session.setLong(1, user.getLocalId());
            session.setLong(2, contact.getLocalId());
            session.setLong(3, createdBy.getLocalId());
            session.setCalendar(4, createdOn);
            session.setLong(5, createdBy.getLocalId());
            session.setCalendar(6, createdOn);
			if(1 != session.executeUpdate())
				throw new HypersonicException(
                        "Could not create contact {0} for user {1}.",
                        contact, user);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
        }
	}

    public void delete(final Long userId, final Long contactId) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_DELETE);
            session.setLong(1, userId);
            session.setLong(2, contactId);
            session.setLong(3, contactId);
            session.setLong(4, userId);
			switch (session.executeUpdate()) {
			    case 0:      // the contact beat you to it
			    case 2:      // the first good delete
			        break;
		        default:
		            throw new HypersonicException(
		                    "Could not delete contact {0} for user {1}.", contactId,
		                    userId);
			}

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
		}
	}

	/**
     * Read a list of contact ids for a user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
	public List<JabberId> readIds(final Long userId) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_READ_IDS);
			session.setLong(1, userId);
			session.executeQuery();
			final List<JabberId> contacts = new LinkedList<JabberId>();
			while (session.nextResult()) {
                contacts.add(extractJabberId(session));
			}
			return contacts;
		} finally {
            session.close();
		}
	}

    private JabberId extractJabberId(final HypersonicSession session) {
		return JabberIdBuilder.parseUsername(session.getString("CONTACT_USERNAME"));
	}
}
