/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.io.sql.artifact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;

import org.jivesoftware.database.JiveID;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.JabberIdBuilder;
import com.thinkparity.server.model.artifact.ArtifactSubscription;
import com.thinkparity.server.model.io.sql.AbstractSql;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
@JiveID(1001)
public class ArtifactSubscriptionSql extends AbstractSql {

	private static final String DELETE =
		"delete from parityArtifactSubscription where artifactId=? and username=?";

	private static final String INSERT =
		new StringBuffer("insert into parityArtifactSubscription ")
		.append("(artifactSubscriptionId,artifactId,updatedOn,username) ")
		.append("values (?,?,CURRENT_TIMESTAMP,?)")
		.toString();

	private static final String SELECT = new StringBuffer()
		.append("select artifactSubscriptionId,artifactId,username,createdOn,")
		.append("updatedOn from parityArtifactSubscription ")
		.append("where artifactId=?").toString();

	private static final String SELECT_COUNT = new StringBuffer()
		.append("select count(artifactSubscriptionId) ")
		.append("from parityArtifactSubscription ")
		.append("where artifactId=? and username=?").toString();

	private static final String SQL_EXIST_SUBSCRIPTIONS =
		new StringBuffer("select count(artifactSubscriptionId) SUBSCRIPTION_COUNT ")
		.append("from parityArtifactSubscription ")
		.append("where artifactId=?").toString();

	/**
	 * Create a ArtifactSubscriptionSql.
	 */
	public ArtifactSubscriptionSql() { super(); }

	public void delete(final Integer artifactId, final String username)
			throws SQLException {
		logger.info("delete(Integer,String)");
		logger.debug(artifactId);
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			debugSql(DELETE);
			ps = cx.prepareStatement(DELETE);
			debugSql(1, artifactId);
			ps.setInt(1, artifactId);
			debugSql(2, username);
			ps.setString(2, username);
			Assert.assertTrue("delete(Integer,String)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public Integer insert(final Integer artifactId, final String username)
			throws SQLException {
		logger.info("insert(Integer,String)");
		logger.debug(artifactId);
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			debugSql(INSERT);
			ps = cx.prepareStatement(INSERT);
			final Integer artifactSubscriptionId = nextId(this);
			debugSql(1, artifactSubscriptionId);
			ps.setInt(1, artifactSubscriptionId);
			debugSql(2, artifactId);
			ps.setInt(2, artifactId);
			debugSql(3, username);
			ps.setString(3, username);
			Assert.assertTrue("insert(Integer,String)", 1 == ps.executeUpdate());
			return artifactSubscriptionId;
		}
		finally { close(cx, ps); }
	}

	public Collection<ArtifactSubscription> select(final Integer artifactId)
			throws SQLException {
		logger.info("select(Integer)");
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			debugSql(SELECT);
			ps = cx.prepareStatement(SELECT);
			debugSql(1, artifactId);
			ps.setInt(1, artifactId);
			rs = ps.executeQuery();
			final Collection<ArtifactSubscription> subscriptions =
				new Vector<ArtifactSubscription>(7);
			while(rs.next()) {
				subscriptions.add(extract(rs));
			}
			return subscriptions;
		}
		finally { close(cx, ps, rs); }
	}

	public Integer selectCount(final Integer artifactId, final String username)
			throws SQLException {
		logger.info("selectCount(Integer,String)");
		logger.debug(artifactId);
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			debugSql(SELECT_COUNT);
			ps = cx.prepareStatement(SELECT_COUNT);
			debugSql(1, artifactId);
			ps.setInt(1, artifactId);
			debugSql(2, username);
			ps.setString(2, username);
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		}
		finally { close(cx, ps, rs); }
	}

	private ArtifactSubscription extract(final ResultSet rs)
			throws SQLException {
		final Integer artifactSubscriptionId = rs.getInt(1);
		final Integer artifactId = rs.getInt(2);
		final String username = rs.getString(3);
		final Calendar createdOn = DateUtil.getInstance(rs.getTimestamp(4));
		final Calendar updatedOn = DateUtil.getInstance(rs.getTimestamp(5));
		final ArtifactSubscription as = new ArtifactSubscription(artifactId, artifactSubscriptionId,
				createdOn, updatedOn, username);
		final JID jid = JIDBuilder.build(username);
		as.setJabberId(JabberIdBuilder.parseJID(jid));
		return as;
	}

	public Boolean existSubscriptions(final Integer artifactId)
			throws SQLException {
		logger.info("existSubscriptions(Integer)");
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			debugSql(SQL_EXIST_SUBSCRIPTIONS);
			ps = cx.prepareStatement(SQL_EXIST_SUBSCRIPTIONS);
			debugSql(1, artifactId);
			ps.setInt(1, artifactId);
			rs = ps.executeQuery();
			rs.next();
			if(0 < rs.getInt("SUBSCRIPTION_COUNT")) { return Boolean.TRUE; }
			else { return Boolean.FALSE; }
		}
		finally { close(cx, ps, rs); }
	}
}
