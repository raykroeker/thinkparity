/*
 * Feb 28, 2006
 */
package com.thinkparity.server.model.io.sql.contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.server.model.contact.Invitation;
import com.thinkparity.server.model.io.sql.AbstractSql;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InvitationSql extends AbstractSql {

	private static final String SQL_CREATE =
		new StringBuffer("insert into parityContactInvitation ")
		.append("(invitationFrom,invitationTo,createdBy,updatedBy,updatedOn) ")
		.append("values (?,?,?,?,CURRENT_TIMESTAMP)")
		.toString();

	private static final String SQL_DELETE =
		new StringBuffer("delete from parityContactInvitation ")
		.append("where invitationFrom=? and invitationTo=?")
		.toString();

	private static final String SQL_READ =
		new StringBuffer("select invitationFrom,invitationTo ")
		.append("from parityContactInvitation ")
		.append("where invitationFrom=? and invitationTo=?")
		.toString();

	/**
	 * Create a InvitationSql.
	 */
	public InvitationSql() { super(); }

	public void create(final JabberId from, final JabberId to,
			final JabberId createdBy) throws SQLException {
        logApiId();
		logger.debug(from);
		logger.debug(to);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(SQL_CREATE);
			ps = cx.prepareStatement(SQL_CREATE);
			set(ps, 1, from.getQualifiedUsername());
			set(ps, 2, to.getQualifiedUsername());
			set(ps, 3, createdBy);
			set(ps, 4, createdBy);
			if(1 != ps.executeUpdate())
				throw new SQLException("Could not create invitation.");
		}
		finally { close(cx, ps); }
	}

	public void delete(final JabberId from, final JabberId to)
			throws SQLException {
        logApiId();
		logger.debug(from);
		logger.debug(to);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(SQL_DELETE);
			ps = cx.prepareStatement(SQL_DELETE);
			ps.setString(1, from.getQualifiedUsername());
			ps.setString(2, to.getQualifiedUsername());
			if(1 != ps.executeUpdate())
				throw new SQLException("Could not delete invitation.");
		}
		finally { close(cx, ps); }
	}

	public Invitation read(final JabberId from, final JabberId to) throws SQLException {
        logApiId();
		logger.debug(from);
		logger.debug(to);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SQL_READ);
			ps = cx.prepareStatement(SQL_READ);
			ps.setString(1, from.getQualifiedUsername());
			ps.setString(2, to.getQualifiedUsername());
			rs = ps.executeQuery();

			if(rs.next()) { return extractInvitation(rs); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	private Invitation extractInvitation(final ResultSet rs)
			throws SQLException {
		final Invitation i = new Invitation();
		i.setFrom(JabberIdBuilder.parseQualifiedUsername(rs.getString("invitationFrom")));
		i.setTo(JabberIdBuilder.parseQualifiedUsername(rs.getString("invitationTo")));
		return i;
	}
}
