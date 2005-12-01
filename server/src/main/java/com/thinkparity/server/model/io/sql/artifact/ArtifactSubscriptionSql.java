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

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.model.artifact.ArtifactSubscription;
import com.thinkparity.server.model.io.sql.AbstractSql;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSubscriptionSql extends AbstractSql {

	private static final String DELETE =
		"delete from parityArtifactSubscription where artifactId = ? and username = ?";

	private static final String INSERT =
		"insert into parityArtifactSubscription (artifactId,username) values (?,?)";

	private static final String SELECT = new StringBuffer()
		.append("select artifactSubscriptionId,artifactId,username,createdOn,")
		.append("updatedOn from parityArtifactSubscription ")
		.append("where artifactId = ?").toString();

	private static final String SELECT_COUNT = new StringBuffer()
		.append("select count(artifactSubscriptionId) ")
		.append("from parityArtifactSubscription ")
		.append("where artifactId = ? and username = ?").toString();

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
			ps = cx.prepareStatement(DELETE);
			ps.setInt(1, artifactId);
			ps.setString(2, username);
			Assert.assertTrue("delete(Integer,String)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public void insert(final Integer artifactId, final String username)
			throws SQLException {
		logger.info("insert(Integer,String)");
		logger.debug(artifactId);
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(INSERT);
			ps.setInt(1, artifactId);
			ps.setString(2, username);
			Assert.assertTrue("insert(Integer,String)", 1 == ps.executeUpdate());
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
			ps = cx.prepareStatement(SELECT);
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
			ps = cx.prepareStatement(SELECT_COUNT);
			ps.setInt(1, artifactId);
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
		return new ArtifactSubscription(artifactId, artifactSubscriptionId,
				createdOn, updatedOn, username);
	}
}
