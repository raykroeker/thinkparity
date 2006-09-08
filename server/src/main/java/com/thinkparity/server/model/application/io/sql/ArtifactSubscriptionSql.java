/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jivesoftware.database.JiveID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.desdemona.model.artifact.ArtifactSubscription;

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
		.append("(artifactSubscriptionId,artifactId,createdBy,updatedOn,")
		.append("updatedBy,username) ")
		.append("values (?,?,?,CURRENT_TIMESTAMP,?,?)")
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

	/** Sql to read a single artifact subscription. */
    private static final String SQL_READ =
            new StringBuffer("select artifactSubscriptionId,artifactId,")
            .append("username,createdOn,updatedOn ")
            .append("from parityArtifactSubscription ")
            .append("where artifactId=? and username=?")
            .toString();

	/**
	 * Create a ArtifactSubscriptionSql.
	 */
	public ArtifactSubscriptionSql() { super(); }

    public void delete(final Long artifactId, final String username)
			throws SQLException {
        logApiId();
		logger.debug(artifactId);
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(DELETE);
			ps = cx.prepareStatement(DELETE);
            logStatementParameter(1, artifactId);
			ps.setLong(1, artifactId);
            logStatementParameter(2, username);
			ps.setString(2, username);
			Assert.assertTrue("delete(Integer,String)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public Boolean existSubscriptions(final Long artifactId)
			throws SQLException {
        logApiId();
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SQL_EXIST_SUBSCRIPTIONS);
			ps = cx.prepareStatement(SQL_EXIST_SUBSCRIPTIONS);
            logStatementParameter(1, artifactId);
			ps.setLong(1, artifactId);
			rs = ps.executeQuery();
			rs.next();
			if(0 < rs.getInt("SUBSCRIPTION_COUNT")) { return Boolean.TRUE; }
			else { return Boolean.FALSE; }
		}
		finally { close(cx, ps, rs); }
	}

	public Integer insert(final Long artifactId, final String username,
            final JabberId createdBy) throws SQLException {
        logApiId();
		logger.debug(artifactId);
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(INSERT);
			ps = cx.prepareStatement(INSERT);
			final Integer artifactSubscriptionId = nextId(this);
			set(ps, 1, artifactSubscriptionId);
			set(ps, 2, artifactId);
			set(ps, 3, createdBy);
			set(ps, 4, createdBy);
			set(ps, 5, username);
			if(1 != ps.executeUpdate())
				throw new SQLException("Could not create subscription.");

			return artifactSubscriptionId;
		}
		finally { close(cx, ps); }
	}

	public ArtifactSubscription read(final Long artifactId,
            final JabberId jabberId) throws SQLException {
        logApiId();
        debugVariable("artifactId", artifactId);
        debugVariable("jabberId", jabberId);
        Connection cx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cx = getCx();
            logStatement(SQL_READ);
            ps = cx.prepareStatement(SQL_READ);
            logStatementParameter(1, artifactId);
            logStatementParameter(2, jabberId.getUsername());
            ps.setLong(1, artifactId);
            ps.setString(2, jabberId.getUsername());
            rs = ps.executeQuery();
            if(rs.next()) {
                return extract(rs);
            } else {
                return null;
            }
        } finally {
            close(cx, ps, rs);
        }
    }

	public List<ArtifactSubscription> select(final Long artifactId)
			throws SQLException {
        logApiId();
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SELECT);
			ps = cx.prepareStatement(SELECT);
            logStatementParameter(1, artifactId);
			ps.setLong(1, artifactId);
			rs = ps.executeQuery();
			final List<ArtifactSubscription> subscriptions =
				new ArrayList<ArtifactSubscription>(7);
			while(rs.next()) {
				subscriptions.add(extract(rs));
			}
			return subscriptions;
		}
		finally { close(cx, ps, rs); }
	}

	public Integer selectCount(final Integer artifactId, final String username)
			throws SQLException {
        logApiId();
		logger.debug(artifactId);
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SELECT_COUNT);
			ps = cx.prepareStatement(SELECT_COUNT);
            logStatementParameter(1, artifactId);
			ps.setInt(1, artifactId);
            logStatementParameter(2, username);
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
		as.setJabberId(JabberIdBuilder.parseUsername(username));
		return as;
	}
}
