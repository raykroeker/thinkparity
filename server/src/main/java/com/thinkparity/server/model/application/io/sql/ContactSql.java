/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactSql extends AbstractSql {

	private static final String SQL_CREATE =
		new StringBuffer("insert into USER_CONTACT ")
		.append("(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,")
        .append("UPDATED_ON) ")
		.append("values (?,?,?,?,?,?)")
		.toString();

	private static final String SQL_DELETE =
		new StringBuffer("delete from USER_CONTACT ")
		.append("where USER_ID=? and CONTACT_ID=?")
		.toString();

	private static final String SQL_READ =
		new StringBuffer("select PUC.USERNAME \"CONTACT_USERNAME\" ")
		.append("from USER_CONTACT UC ")
        .append("inner join PARITY_USER PUC on PUC.USER_ID=UC.CONTACT_ID ")
		.append("where UC.USER_ID=?")
		.toString();

	private final UserSql userSql;

	/**
	 * Create a InvitationSql.
	 * 
	 */
	public ContactSql() {
        super();
        this.userSql = new UserSql();
	}

    public void create(final JabberId userId, final JabberId contactId,
			final JabberId createdBy, final Calendar createdOn) {
		final HypersonicSession session = openSession();
		try {
            final Long createdByLocalUserId = readLocalUserId(createdBy);
			session.prepareStatement(SQL_CREATE);
            session.setLong(1, readLocalUserId(userId));
            session.setLong(2, readLocalUserId(contactId));
            session.setLong(3, createdByLocalUserId);
            session.setCalendar(4, createdOn);
            session.setLong(5, createdByLocalUserId);
            session.setCalendar(6, createdOn);
			if(1 != session.executeUpdate())
				throw new HypersonicException(
                        "Could not create contact {0} for user {1}.", contactId, userId);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
        }
	}

    public void delete(final JabberId userId, final JabberId contactId) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_DELETE);
			session.setLong(1, readLocalUserId(userId));
			session.setLong(2, readLocalUserId(contactId));
			if(1 != session.executeUpdate())
				throw new HypersonicException(
                        "Could not delete contact {0} for user {1}.", contactId, userId);

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
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;JabberId&gt;</code>.
     * @throws SQLException
     */
	public List<JabberId> readIds(final JabberId userId) throws SQLException {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_READ);
			session.setLong(1, readLocalUserId(userId));
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

	private Long readLocalUserId(final JabberId userId) {
        return userSql.readLocalUserId(userId);
    }
}
