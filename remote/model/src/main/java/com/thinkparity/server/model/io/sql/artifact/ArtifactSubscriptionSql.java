/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.io.sql.artifact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactId;
import com.thinkparity.server.model.artifact.ArtifactSubscription;
import com.thinkparity.server.model.io.sql.AbstractSql;
import com.thinkparity.server.model.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSubscriptionSql extends AbstractSql {

	private static final String SELECT =
		"select artifactId,username from parityArtifactSubscription where artifactId = ?";

	/**
	 * Create a ArtifactSubscriptionSql.
	 */
	public ArtifactSubscriptionSql() { super(); }

	public Collection<ArtifactSubscription> select(final Artifact artifact)
			throws SQLException {
		logger.info("select(Artifact)");
		logger.debug(artifact);
		final String artifactId = artifact.getId().getId().toString();
		logger.debug(artifactId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(SELECT);
			ps.setString(1, artifactId);
			rs = ps.executeQuery();
			final Collection<ArtifactSubscription> subscriptions =
				new Vector<ArtifactSubscription>(7);
			while(rs.next()) {
				subscriptions.add(extractArtifactSubscription(rs));
			}
			return subscriptions;
		}
		finally { close(cx, ps, rs); }
	}

	private ArtifactSubscription extractArtifactSubscription(final ResultSet rs)
			throws SQLException {
		final UUID uuid = UUID.fromString(rs.getString(1));
		final ArtifactId artifactId = new ArtifactId(uuid);
		final Artifact artifact = new Artifact(artifactId);
		final String username = rs.getString(2);
		final User user = new User(username);
		return new ArtifactSubscription(artifact, user);
	}
}
