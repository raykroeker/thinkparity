/*
 * Nov 30, 2005
 */
package com.thinkparity.server.model.io.sql.artifact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import org.jivesoftware.database.JiveID;

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
		"insert into parityArtifact (artifactId,artifactUUID) values (?,?)";

	private static final String SELECT = new StringBuffer()
		.append("select artifactId,artifactUUID,createdOn,updatedOn ")
		.append("from parityArtifact ")
		.append("where artifactUUID = ?").toString();

	/**
	 * Create a ArtifactSql.
	 */
	public ArtifactSql() { super(); }

	public Integer insert(final UUID artifactUUID) throws SQLException {
		logger.info("insert(UUID)");
		logger.debug(artifactUUID);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(INSERT);
			final Integer artifactId = nextId(this);
			ps.setInt(1, artifactId);
			ps.setString(2, artifactUUID.toString());
			Assert.assertTrue("insert(UUID)", 1 == ps.executeUpdate());
			return artifactId;
		}
		finally { close(cx, ps); }
	}

	public Artifact select(final UUID artifactUUID) throws SQLException {
		logger.info("select(UUID)");
		logger.debug(artifactUUID);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(SELECT);
			ps.setString(1, artifactUUID.toString());
			rs = ps.executeQuery();
			if(rs.next()) { return extract(rs); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	private Artifact extract(final ResultSet rs) throws SQLException {
		final Integer artifactId = rs.getInt(1);
		final UUID artifactUUID = UUID.fromString(rs.getString(2));
		final Calendar createdOn = DateUtil.getInstance(rs.getTimestamp(3));
		final Calendar updatedOn = DateUtil.getInstance(rs.getTimestamp(4));
		return new Artifact(artifactId, artifactUUID, createdOn, updatedOn);
	}
}
