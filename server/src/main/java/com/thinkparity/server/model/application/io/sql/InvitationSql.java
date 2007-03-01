/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.desdemona.model.contact.Invitation;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InvitationSql extends AbstractSql {

    /** Sql to create an invitation. */
    private static final String SQL_CREATE =
        new StringBuffer("insert into USER_INVITATION ")
        .append("(INVITATION_FROM,INVITATION_TO,CREATED_BY,CREATED_ON,")
        .append("UPDATED_BY,UPDATED_ON) ")
        .append("values (?,?,?,?,?,?)")
        .toString();

    /** Sql to create an email invitation. */
    private static final String SQL_CREATE_EMAIL =
        new StringBuffer("insert into USER_EMAIL_INVITATION ")
        .append("(INVITATION_FROM,INVITATION_TO,CREATED_BY,CREATED_ON,")
        .append("UPDATED_BY,UPDATED_ON) ")
        .append("values (?,?,?,?,?,?)")
        .toString();

    /** Sql to delete an invitation. */
    private static final String SQL_DELETE =
        new StringBuffer("delete from USER_INVITATION ")
        .append("where INVITATION_FROM=? and INVITATION_TO=?")
        .toString();

    /** Sql to delete an email invitation. */
    private static final String SQL_DELETE_EMAIL =
        new StringBuffer("delete from USER_EMAIL_INVITATION ")
        .append("where INVITATION_FROM=? and INVITATION_TO=?")
        .toString();

    /** Sql to read an invitation. */
    private static final String SQL_READ =
        new StringBuffer("select PUIF.USERNAME \"INVITATION_FROM_USERNAME\",")
        .append(" PUIT.USERNAME \"INVITATION_TO_USERNAME\" ")
        .append("from USER_INVITATION UI ")
        .append("inner join PARITY_USER PUIF on UI.INVITATION_FROM=PUIF.USER_ID ")
        .append("inner join PARITY_USER PUIT on UI.INVITATION_TO=PUIT.USER_ID ")
        .append("where UI.INVITATION_FROM=? and UI.INVITATION_TO=?")
        .toString();

    private final UserSql userSql;

    /**
     * Create InvitationSql.
     *
     */
    public InvitationSql() {
        super();
        this.userSql = new UserSql();
    }

    public void create(final JabberId from, final JabberId to,
            final Calendar createdOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            final Long fromLocalUserId = readLocalUserId(from);
            session.setLong(1, fromLocalUserId);
            session.setLong(2, readLocalUserId(to));
            session.setLong(3, fromLocalUserId);
            session.setCalendar(4, createdOn);
            session.setLong(5, fromLocalUserId);
            session.setCalendar(6, createdOn);
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create invitation from {0} to {1}.", from, to);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void createEmail(final JabberId from, final EMail to,
            final Calendar createdOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_EMAIL);
            final Long fromLocalUserId = readLocalUserId(from);
            session.setLong(1, fromLocalUserId);
            session.setString(2, to.toString());
            session.setLong(3, fromLocalUserId);
            session.setCalendar(4, createdOn);
            session.setLong(5, fromLocalUserId);
            session.setCalendar(6, createdOn);
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create e-mail invitation from {0} to {1}.", from, to);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void delete(final JabberId from, final JabberId to) {
        final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_DELETE);
			session.setLong(1, readLocalUserId(from));
			session.setLong(2, readLocalUserId(to));
			if (1 != session.executeUpdate())
				throw new HypersonicException(
                        "Could not delete invitation from {0} to {1}.", from , to);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
		}
	}

	public void deleteEmail(final JabberId from, final EMail to) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EMAIL);
            session.setLong(1, readLocalUserId(from));
            session.setString(2, to.toString());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete e-mail invitation from {0} to {1}.",
                        from, to);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public Invitation read(final JabberId from, final JabberId to) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_READ);
			session.setLong(1, readLocalUserId(from));
			session.setLong(2, readLocalUserId(to));
			session.executeQuery();
			if (session.nextResult()) {
                return extractInvitation(session);
			} else {
                return null;
			}
		}finally {
            session.close();
        }
	}

	private Invitation extractInvitation(final HypersonicSession session) {
        final Invitation i = new Invitation();
        i.setFrom(JabberIdBuilder.parseUsername(session.getString("INVITATION_FROM_USERNAME")));
        i.setTo(JabberIdBuilder.parseUsername(session.getString("INVITATION_TO_USERNAME")));
        return i;
    }

    private Long readLocalUserId(final JabberId userId) {
        return userSql.readLocalUserId(userId);
    }
}
