/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            new StringBuffer("insert into parityContactInvitation ")
            .append("(invitationFrom,invitationTo,createdBy,updatedBy,updatedOn) ")
            .append("values (?,?,?,?,CURRENT_TIMESTAMP)")
            .toString();

    /** Sql to create an email invitation. */
    private static final String SQL_CREATE_EMAIL =
            new StringBuffer("insert into PARITYCONTACTEMAILINVITATION ")
            .append("(INVITATIONFROM,INVITATIONTO,CREATEDBY,UPDATEDBY,UPDATEDON) ")
            .append("values (?,?,?,?,CURRENT_TIMESTAMP)")
            .toString();

    /** Sql to delete an invitation. */
    private static final String SQL_DELETE =
            new StringBuffer("delete from parityContactInvitation ")
            .append("where invitationFrom=? and invitationTo=?")
            .toString();

    /** Sql to delete an email invitation. */
    private static final String SQL_DELETE_EMAIL =
            new StringBuffer("delete from PARITYCONTACTEMAILINVITATION ")
            .append("where INVITATIONFROM=? and INVITATIONTO=?")
            .toString();

    /** Sql to read an invitation. */
    private static final String SQL_READ =
            new StringBuffer("select invitationFrom,invitationTo ")
            .append("from parityContactInvitation ")
            .append("where invitationFrom=? and invitationTo=?")
            .toString();

    /** Create InvitationSql. */
    public InvitationSql() { super(); }

    public void create(final JabberId userId, final JabberId extendTo)
            throws SQLException {
        logApiId();
        logVariable("variable", userId);
        logVariable("variable", extendTo);
        Connection cx = null;
        PreparedStatement ps = null;
        try {
            cx = getCx();
            logStatement(SQL_CREATE);
            ps = cx.prepareStatement(SQL_CREATE);
            set(ps, 1, userId.getUsername());
            set(ps, 2, extendTo.getUsername());
            set(ps, 3, userId.getUsername());
            set(ps, 4, userId.getUsername());
            if (1 != ps.executeUpdate())
                throw new SQLException();
            cx.commit();
        } finally {
            close(cx, ps);
        }
    }

    public void createEmail(final JabberId userId, final EMail extendTo)
            throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        try {
            cx = getCx();
            logStatement(SQL_CREATE_EMAIL);
            ps = cx.prepareStatement(SQL_CREATE_EMAIL);
            ps.setString(1, userId.getUsername());
            ps.setString(2, extendTo.toString());
            ps.setString(3, userId.getUsername());
            ps.setString(4, userId.getUsername());
            if (1 != ps.executeUpdate())
                throw new SQLException();
            cx.commit();
        } finally {
            close(cx, ps);
        }
    }

	public void delete(final JabberId from, final JabberId to)
			throws SQLException {
        final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_DELETE);
			session.setString(1, from.getUsername());
			session.setString(2, to.getUsername());
			if (1 != session.executeUpdate())
				throw new HypersonicException("Could not delete invitation.");
            session.commit();
        } catch (final Throwable t) {
            translateError(session, t);
		} finally {
            session.close();
		}
	}

    public void deleteEmail(final JabberId from, final EMail invitedAs)
            throws SQLException {
        logApiId();
        Connection cx = null;
        PreparedStatement ps = null;
        try {
            cx = getCx();
            ps = cx.prepareStatement(SQL_DELETE_EMAIL);
            ps.setString(1, from.getUsername());
            ps.setString(2, invitedAs.toString());
            if (1 != ps.executeUpdate())
                throw new SQLException();
            cx.commit();
        } finally {
            close(cx, ps);
        }
    }

	public Invitation read(final JabberId from, final JabberId to) throws SQLException {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_READ);
			session.setString(1, from.getUsername());
			session.setString(2, to.getUsername());
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
        // from and to are simple usernames
        i.setFrom(JabberIdBuilder.parseUsername(session.getString("invitationFrom")));
        i.setTo(JabberIdBuilder.parseUsername(session.getString("invitationTo")));
        return i;
    }
}
