/*
 * Feb 28, 2006
 */
package com.thinkparity.server.model.io.sql.contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.server.model.io.sql.AbstractSql;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ContactSql extends AbstractSql {

	private static final String SQL_CREATE =
		new StringBuffer("insert into parityContact ")
		.append("(username,contactUsername,createdBy,updatedBy,updatedOn) ")
		.append("values (?,?,?,?,CURRENT_TIMESTAMP)")
		.toString();

	private static final String SQL_DELETE =
		new StringBuffer("delete from parityContact ")
		.append("where username=? and contactUsername=?")
		.toString();

	private static final String SQL_READ =
		new StringBuffer("select username,contactUsername ")
		.append("from parityContact ")
		.append("where username=?")
		.toString();

	/**
	 * Create a InvitationSql.
	 * 
	 */
	public ContactSql() { super(); }

	public void create(final JabberId username, final JabberId contact,
			final JabberId createdBy) throws SQLException {
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			debugSql(SQL_CREATE);
			ps = cx.prepareStatement(SQL_CREATE);
			set(ps, 1, username.getUsername());
			set(ps, 2, contact.getQualifiedUsername());
			set(ps, 3, createdBy);
			set(ps, 4, createdBy);
			if(1 != ps.executeUpdate())
				throw new SQLException("Could not create contact.");
		}
		finally { close(cx, ps); }
	}

	public void delete(final JabberId username, final JabberId contact)
		throws SQLException {
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			debugSql(SQL_DELETE);
			ps = cx.prepareStatement(SQL_DELETE);
			ps.setString(1, username.getUsername());
			ps.setString(2, contact.getQualifiedUsername());
			if(1 != ps.executeUpdate())
				throw new SQLException("Could not delete contact.");
		}
		finally { close(cx, ps); }
	}

	public List<JabberId> read(final JabberId username) throws SQLException {
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			debugSql(SQL_READ);
			ps = cx.prepareStatement(SQL_READ);
			ps.setString(1, username.getUsername());
			rs = ps.executeQuery();
			final List<JabberId> contacts = new LinkedList<JabberId>();
			while(rs.next()) { contacts.add(extractJabberId(rs)); }
			return contacts;
		}
		finally { close(cx, ps, rs); }
	}

	private JabberId extractJabberId(final ResultSet rs) throws SQLException {
		return JabberIdBuilder.parseQualifiedUsername(rs.getString("contactUsername"));
	}
}
