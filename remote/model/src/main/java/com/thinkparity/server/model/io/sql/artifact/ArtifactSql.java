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

import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.io.sql.AbstractSql;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
@JiveID(1000)
public class ArtifactSql extends AbstractSql {

	private static final String INSERT =
		new StringBuffer("insert into parityArtifact ")
		.append("(artifactId,artifactUUID,artifactKeyHolder,updatedOn) ")
		.append("values (?,?,?,CURRENT_TIMESTAMP)")
		.toString();

	private static final String SELECT = new StringBuffer()
	.append("select artifactId,artifactUUID,artifactKeyHolder,createdOn,")
	.append("updatedOn ")
	.append("from parityArtifact ")
	.append("where artifactUUID = ?").toString();

	private static final String SQL_LIST_FOR_KEY_HOLDER =
		new StringBuffer("select artifactId,artifactUUID,artifactKeyHolder,")
		.append("createdOn,updatedOn ")
		.append("from parityArtifact ")
		.append("where artifactKeyHolder= ?").toString();

	private static final String SELECT_KEYHOLDER = new StringBuffer()
		.append("select artifactKeyHolder ")
		.append("from parityArtifact ")
		.append("where artifactId = ?").toString();

	private static final String UPDATE_KEYHOLDER = new StringBuffer()
		.append("update parityArtifact set artifactKeyHolder = ?,")
		.append("updatedOn = current_timestamp where artifactId = ?")
		.toString();

	/**
	 * Create a ArtifactSql.
	 */
	public ArtifactSql() { super(); }

	public Integer insert(final UUID artifactUUID,
			final String artifactKeyHolder) throws SQLException {
		logger.info("insert(UUID)");
		logger.debug(artifactUUID);
		logger.debug(artifactKeyHolder);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			debugSql(INSERT);
			ps = cx.prepareStatement(INSERT);
			final Integer artifactId = nextId(this);
			debugSql(1, artifactId);
			ps.setInt(1, artifactId);
			debugSql(2, artifactUUID.toString());
			ps.setString(2, artifactUUID.toString());
			debugSql(3, artifactKeyHolder);
			ps.setString(3, artifactKeyHolder);
			Assert.assertTrue("insert(UUID)", 1 == ps.executeUpdate());
			return artifactId;
		}
		finally { close(cx, ps); }
	}

	public List<Artifact> listForKeyHolder(final JID keyHolderJID)
			throws SQLException {
		logger.info("listForKeyHolder(JID)");
		logger.debug(keyHolderJID);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			debugSql(SQL_LIST_FOR_KEY_HOLDER);
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
		logger.info("select(UUID)");
		logger.debug(artifactUUID);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			debugSql(SELECT);
			ps = cx.prepareStatement(SELECT);
			debugSql(1, artifactUUID.toString());
			ps.setString(1, artifactUUID.toString());
			rs = ps.executeQuery();
			if(rs.next()) { return extract(rs); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	public String selectKeyHolder(final Integer artifactId) throws SQLException {
		logger.info("selectKeyHolder(Integer)");
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			debugSql(SELECT_KEYHOLDER);
			ps = cx.prepareStatement(SELECT_KEYHOLDER);
			debugSql(1, artifactId);
			ps.setInt(1, artifactId);
			rs = ps.executeQuery();
			if(rs.next()) { return rs.getString(1); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	public void updateKeyHolder(final Integer artifactId,
			final String artifactKeyHolder) throws SQLException {
		logger.info("updateKeyHolder(Integer,String)");
		logger.debug(artifactId);
		logger.debug(artifactKeyHolder);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			debugSql(UPDATE_KEYHOLDER);
			ps = cx.prepareStatement(UPDATE_KEYHOLDER);
			debugSql(1, artifactKeyHolder);
			ps.setString(1, artifactKeyHolder);
			debugSql(2, artifactId);
			ps.setInt(2, artifactId);
			Assert.assertTrue(
					"updateKeyHolder(Integer,String)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	private Artifact extract(final ResultSet rs) throws SQLException {
		final Integer artifactId = rs.getInt("artifactId");
		final UUID artifactUUID = UUID.fromString(rs.getString("artifactUUID"));
		final String artifactKeyHolder = rs.getString("artifactKeyHolder");
		final Calendar createdOn = DateUtil.getInstance(rs.getTimestamp("createdOn"));
		final Calendar updatedOn = DateUtil.getInstance(rs.getTimestamp("updatedOn"));
		return new Artifact(artifactId, artifactUUID, artifactKeyHolder,
				createdOn, updatedOn);
	}
}
