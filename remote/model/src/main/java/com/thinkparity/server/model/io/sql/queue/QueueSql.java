/*
 * Dec 1, 2005
 */
package com.thinkparity.server.model.io.sql.queue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;

import org.jivesoftware.database.JiveID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.model.io.sql.AbstractSql;
import com.thinkparity.server.model.queue.QueueItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
@JiveID(1002)
public class QueueSql extends AbstractSql {

	private static final String DELETE =
		"delete from parityQueue where queueId = ?";

	private static final String INSERT = new StringBuffer()
		.append("insert into parityQueue (queueId,username,queueMessageSize,")
		.append("queueMessage,updatedOn) values (?,?,?,?,CURRENT_TIMESTAMP)").toString();

	private static final String SELECT = new StringBuffer()
		.append("select queueId,username,queueMessageSize,queueMessage,")
		.append("createdOn,updatedOn ")
		.append("from parityQueue ")
		.append("where queueId = ?").toString();

	private static final String SELECT_USERNAME = new StringBuffer()
		.append("select queueId,username,queueMessageSize,queueMessage,")
		.append("createdOn,updatedOn ")
		.append("from parityQueue ")
		.append("where username = ? order by createdOn asc").toString();

	/**
	 * Create a QueueSql.
	 */
	public QueueSql() { super(); }

	public void delete(final Integer queueId) throws SQLException {
		logger.info("delete(Integer)");
		logger.debug(queueId);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(DELETE);
			Assert.assertTrue("delete(Integer)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public Integer insert(final String username, final String message)
			throws SQLException {
		logger.info("insert(String,String)");
		logger.debug(username);
		logger.debug(message);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(INSERT);
			final Integer queueId = nextId(this);
			ps.setInt(1, queueId);
			ps.setString(2, username);
			ps.setInt(3, message.length());
			ps.setString(4, message);
			Assert.assertTrue("insert(String,String)", 1 == ps.executeUpdate());
			return queueId;
		}
		finally { close(cx, ps); }
	}

	public QueueItem select(final Integer queueId) throws SQLException {
		logger.info("select(Integer)");
		logger.debug(queueId);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(SELECT);
			ps.setInt(1, queueId);
			rs = ps.executeQuery();
			if(rs.next()) { return extract(rs); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	public Collection<QueueItem> select(final String username) throws SQLException {
		logger.info("select(Integer)");
		logger.debug(username);
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(SELECT_USERNAME);
			ps.setString(1, username);
			rs = ps.executeQuery();
			final Collection<QueueItem> items = new Vector<QueueItem>(7);
			while(rs.next()) { items.add(extract(rs)); }
			return items;
		}
		finally { close(cx, ps, rs); }
	}

	private QueueItem extract(final ResultSet rs) throws SQLException {
		final Integer queueId = rs.getInt(1);
		final String username = rs.getString(2);
		final Integer queueMessageSize = rs.getInt(3);
		final String queueMessage = rs.getString(4);
		final Calendar createdOn = DateUtil.getInstance(rs.getTimestamp(5));		
		final Calendar updatedOn =  DateUtil.getInstance(rs.getTimestamp(6));
		return new QueueItem(createdOn, queueId, queueMessage, queueMessageSize,
				updatedOn, username);
	}
}
