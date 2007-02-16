/*
 * Nov 30, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.artifact.RemoteArtifact;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

import org.xmpp.packet.JID;


/**
 * <b>Title:</b>thinkParity Artifact SQL<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArtifactSql extends AbstractSql {

	private static final String INSERT =
		new StringBuffer("insert into parityArtifact ")
		.append("(artifactUUID,artifactKeyHolder,artifactStateId,createdBy,createdOn,updatedBy,updatedOn) ")
		.append("values (?,?,?,?,?,?,?)")
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

	/** Sql to read the artifact key holder. */
    private static final String SQL_READ_KEY_HOLDER =
            new StringBuffer("select ARTIFACTKEYHOLDER ")
            .append("from PARITYARTIFACT ")
            .append("where ARTIFACTUUID=?")
            .toString();

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

	public void delete(final Long artifactId) throws SQLException {
        logApiId();
		logVariable("variable", artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
            logStatement(SQL_DELETE);
			ps = cx.prepareStatement(SQL_DELETE);
            logStatementParameter(1, artifactId);
			ps.setLong(1, artifactId);
			Assert.assertTrue("Unable to delete.", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public Long insert(final UUID uniqueId, final JabberId keyHolder,
            final ArtifactState state, final JabberId createdBy,
            final Calendar createdOn) {
		logApiId();
		logVariable("uniqueId", uniqueId);
        logVariable("keyHolder", keyHolder);
        logVariable("state", state);
        logVariable("createdBy", createdBy);
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(INSERT);
			session.setUniqueId(1, uniqueId);
			session.setString(2, keyHolder.getUsername());
            session.setInt(3, state.getId());
            session.setString(4, createdBy.getUsername());
            session.setCalendar(5, createdOn);
            session.setString(6, createdBy.getUsername());
            session.setCalendar(7, createdOn);
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT CREATE ARTIFACT");
			return session.getIdentity();
        } catch (final HypersonicException hx) {
            session.rollback();
            throw hx;
		} finally {
            session.close();
		}
	}

	public List<Artifact> listForKeyHolder(final JID keyHolderJID)
			throws SQLException {
	    final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_LIST_FOR_KEY_HOLDER);
			session.setString(1, keyHolderJID.getNode());
			session.executeQuery();
			final List<Artifact> artifacts = new ArrayList<Artifact>();
			while (session.nextResult()) {
                artifacts.add(extract(session));
			}
			return artifacts;
		} finally {
            session.close();
		}
	}

    public JabberId readKeyHolder(final UUID uniqueId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_KEY_HOLDER);
            session.setString(1, uniqueId.toString());
            session.executeQuery();
            if (session.nextResult()) {
                return JabberIdBuilder.parseUsername(
                        session.getString("ARTIFACTKEYHOLDER"));
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    public Artifact select(final UUID artifactUUID) throws SQLException {
        final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SELECT);
			session.setString(1, artifactUUID.toString());
			session.executeQuery();
			if (session.nextResult()) {
                return extract(session);
			} else {
                return null;
			}
		} finally {
            session.close();
		}
	}

	public String selectKeyHolder(final Long artifactId) throws SQLException {
        logApiId();
		logVariable("variable", artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
            logStatement(SELECT_KEYHOLDER);
			ps = cx.prepareStatement(SELECT_KEYHOLDER);
            logStatementParameter(1, artifactId);
			ps.setLong(1, artifactId);
			rs = ps.executeQuery();
			if(rs.next()) { return rs.getString(1); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

    public void updateKeyHolder(final Long artifactId,
            final JabberId userId, final JabberId updatedBy)
            throws SQLException {
        updateKeyHolder(artifactId, userId.getUsername(), updatedBy);
    }

    public void updateKeyHolder(final Long artifactId,
            final String artifactKeyHolder, final JabberId updatedBy)
            throws SQLException {
        logApiId();
		logVariable("variable", artifactId);
		logVariable("variable", artifactKeyHolder);
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
			ps.setLong(3, artifactId);
			Assert.assertTrue(
					"updateKeyHolder(Integer,String)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public void updateKeyHolder(final Long artifactId, final User user,
            final JabberId updatedBy) throws SQLException {
        updateKeyHolder(artifactId, user.getId(), updatedBy);
    }

	public void updateState(final Integer artifactId, final ArtifactState currentState,
			final ArtifactState newState, final JabberId updatedBy) throws SQLException {
        logApiId();
		logVariable("variable", artifactId);
		logVariable("variable", currentState);
		logVariable("variable", newState);
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

	Artifact extract(final HypersonicSession session) {
        final Artifact artifact = new RemoteArtifact();
        artifact.setId(session.getLong("artifactId"));
        artifact.setUniqueId(UUID.fromString(session.getString("artifactUUID")));
        artifact.setState(ArtifactState.fromId(session.getInteger("artifactStateId")));
        artifact.setCreatedOn(session.getCalendar("createdOn"));
        artifact.setUpdatedOn(session.getCalendar("updatedOn"));
        return artifact;
	}
}
