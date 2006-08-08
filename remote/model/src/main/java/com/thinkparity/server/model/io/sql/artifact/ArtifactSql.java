/*
 * Nov 30, 2005
 */
package com.thinkparity.server.model.io.sql.artifact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.database.JiveID;

import org.xmpp.packet.JID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.Artifact.State;
import com.thinkparity.server.model.io.sql.AbstractSql;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
@JiveID(1000)
public class ArtifactSql extends AbstractSql {

	private static final String INSERT =
		new StringBuffer("insert into parityArtifact ")
		.append("(artifactId,artifactUUID,artifactKeyHolder,artifactStateId,createdBy,updatedOn,updatedBy) ")
		.append("values (?,?,?,?,?,CURRENT_TIMESTAMP,?)")
		.toString();

	private static final String SELECT = new StringBuffer()
	.append("select artifactId,artifactUUID,artifactKeyHolder,artifactStateId,createdOn,")
	.append("updatedOn ")
	.append("from parityArtifact ")
	.append("where artifactUUID = ?").toString();

	private static final String SELECT_KEYHOLDER = new StringBuffer()
		.append("select artifactKeyHolder ")
		.append("from parityArtifact ")
		.append("where artifactId = ?").toString();

	private static final String SQL_DELETE =
		new StringBuffer("delete from parityArtifact ")
		.append("where artifactId=?")
		.toString();

	private static final String SQL_LIST_FOR_KEY_HOLDER =
		new StringBuffer("select artifactId,artifactUUID,artifactKeyHolder,")
		.append("artifactStateId,createdOn,updatedOn ")
		.append("from parityArtifact ")
		.append("where artifactKeyHolder= ?").toString();

	private static final String SQL_UPDATE_STATE =
		new StringBuffer("update parityArtifact ")
		.append("set artifactStateId=?,updatedOn=current_timestamp,")
		.append("updatedBy=? ")
		.append("where artifactId=? and artifactStateId=?")
		.toString();

	private static final String UPDATE_KEYHOLDER =
		new StringBuffer("update parityArtifact ")
		.append("set artifactKeyHolder=?,updatedOn=current_timestamp,")
		.append("updatedBy=? ")
		.append("where artifactId=?")
		.toString();

	/**
	 * Create a ArtifactSql.
	 */
	public ArtifactSql() { super(); }

	public void delete(final Integer artifactId) throws SQLException {
        logApiId();
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(SQL_DELETE);
			ps = cx.prepareStatement(SQL_DELETE);
            logStatementParameter(1, artifactId);
			ps.setInt(1, artifactId);
			Assert.assertTrue("Unable to delete.", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public Integer insert(final UUID artifactUUID,
			final String artifactKeyHolder, final Artifact.State state,
			final JabberId createdBy) throws SQLException {
		logApiId();
		logger.debug(artifactUUID);
		logger.debug(artifactKeyHolder);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(INSERT);
			ps = cx.prepareStatement(INSERT);
			final Integer artifactId = nextId(this);
            logStatementParameter(1, artifactId);
			ps.setInt(1, artifactId);
            logStatementParameter(2, artifactUUID.toString());
			ps.setString(2, artifactUUID.toString());
            logStatementParameter(3, artifactKeyHolder);
			ps.setString(3, artifactKeyHolder);
            logStatementParameter(4, state);
			ps.setInt(4, state.getId());
			set(ps, 5, createdBy);
			set(ps, 6, createdBy);
			Assert.assertTrue("insert(UUID)", 1 == ps.executeUpdate());
			return artifactId;
		}
		finally { close(cx, ps); }
	}

	public List<Artifact> listForKeyHolder(final JID keyHolderJID)
			throws SQLException {
        logApiId();
		logger.debug(keyHolderJID);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SQL_LIST_FOR_KEY_HOLDER);
			ps = cx.prepareStatement(SQL_LIST_FOR_KEY_HOLDER);
			ps.setString(1, keyHolderJID.getNode());
			rs = ps.executeQuery();

			final List<Artifact> artifacts = new LinkedList<Artifact>();
			while(rs.next()) { artifacts.add(extract(rs)); }
			return artifacts;
		}
		finally { close(cx, ps, rs); }
	}

	public Artifact select(final UUID artifactUUID) throws SQLException {
        logApiId();
		logger.debug(artifactUUID);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SELECT);
			ps = cx.prepareStatement(SELECT);
            logStatementParameter(1, artifactUUID.toString());
			ps.setString(1, artifactUUID.toString());
			rs = ps.executeQuery();
			if(rs.next()) { return extract(rs); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	public String selectKeyHolder(final Integer artifactId) throws SQLException {
        logApiId();
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SELECT_KEYHOLDER);
			ps = cx.prepareStatement(SELECT_KEYHOLDER);
            logStatementParameter(1, artifactId);
			ps.setInt(1, artifactId);
			rs = ps.executeQuery();
			if(rs.next()) { return rs.getString(1); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	public void updateKeyHolder(final Integer artifactId,
			final String artifactKeyHolder, final JabberId updatedBy)
			throws SQLException {
        logApiId();
		logger.debug(artifactId);
		logger.debug(artifactKeyHolder);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(UPDATE_KEYHOLDER);
			ps = cx.prepareStatement(UPDATE_KEYHOLDER);
            logStatementParameter(1, artifactKeyHolder);
			ps.setString(1, artifactKeyHolder);
			set(ps, 2, updatedBy);
            logStatementParameter(3, artifactId);
			ps.setInt(3, artifactId);
			Assert.assertTrue(
					"updateKeyHolder(Integer,String)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public void updateState(final Integer artifactId, final State currentState,
			final State newState, final JabberId updatedBy) throws SQLException {
        logApiId();
		logger.debug(artifactId);
		logger.debug(currentState);
		logger.debug(newState);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = prepare(cx, SQL_UPDATE_STATE);
			set(ps, 1, newState);
			set(ps, 2, updatedBy);
			set(ps, 3, artifactId);
			set(ps, 4, currentState);
			if(1 != ps.executeUpdate())
				throw new SQLException("Unable to update state.");
		}
		finally { close(cx, ps); }
	}

	private Artifact extract(final ResultSet rs) throws SQLException {
		final Integer artifactId = rs.getInt("artifactId");
		final UUID artifactUUID = UUID.fromString(rs.getString("artifactUUID"));
		final String artifactKeyHolder = rs.getString("artifactKeyHolder");
		final State artifactState = State.fromId(rs.getInt("artifactStateId"));
		final Calendar createdOn = DateUtil.getInstance(rs.getTimestamp("createdOn"));
		final Calendar updatedOn = DateUtil.getInstance(rs.getTimestamp("updatedOn"));
		return new Artifact(artifactId, artifactUUID, artifactKeyHolder,
				artifactState, createdOn, updatedOn);
	}
}
