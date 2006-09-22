/*
 * Dec 1, 2005
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

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.queue.QueueItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
@JiveID(1002)
public class QueueSql extends AbstractSql {

    private static final String DELETE =
		"delete from parityQueue where queueId = ?";

    private static final String INSERT =
		new StringBuffer("insert into parityQueue ")
		.append("(queueId,username,queueMessageSize,queueMessage,createdBy,")
		.append("updatedBy,updatedOn) ")
		.append("values (?,?,?,?,?,?,CURRENT_TIMESTAMP)")
		.toString();

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

	/** Sql to create an event. */
    private static final String SQL_CREATE_EVENT =
            new StringBuffer("insert into PARITY_EVENT_QUEUE ")
            .append("(USERNAME,EVENT_XML,EVENT_DATE) ")
            .append("values (?,?,?)")
            .toString();

	/**
	 * Create a QueueSql.
	 */
	public QueueSql() { super(); }

	public void createEvent(final JabberId userId, final String eventXml,
            final Calendar eventDate) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_EVENT);
            session.setString(1, userId.getUsername());
            session.setString(2, eventXml);
            session.setCalendar(3, eventDate);
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT CREATE QUEUE EVENT");

            session.commit();
        } catch (final HypersonicException hx) {
            session.rollback();
            throw hx;
        } finally {
            session.close();
        }
    }

	public void delete(final Integer queueId) throws SQLException {
        logApiId();
		logger.debug(queueId);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(DELETE);
			ps.setInt(1, queueId);
			Assert.assertTrue("delete(Integer)", 1 == ps.executeUpdate());
		}
		finally { close(cx, ps); }
	}

	public Integer insert(final String username, final String message,
			final JabberId createdBy) throws SQLException {
        logApiId();
		logger.debug(username);
		logger.debug(message);
		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = prepare(cx, INSERT);

			final Integer queueId = nextId(this);
			set(ps, 1, queueId);
			set(ps, 2, username);
			set(ps, 3, message.length());
			set(ps, 4, message);
			set(ps, 5, createdBy);
			set(ps, 6, createdBy);
			if(1 != ps.executeUpdate())
				throw new SQLException("Could not create queue entry.");

			return queueId;
		}
		finally { close(cx, ps); }
	}

	public QueueItem select(final Integer queueId) throws SQLException {
        logApiId();
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

	public List<QueueItem> select(final String username) {
        logApiId();
		logVariable("username", username);
        final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SELECT_USERNAME);
			session.setString(1, username);
			session.executeQuery();
			final List<QueueItem> items = new ArrayList<QueueItem>(7);
			while (session.nextResult()) {
                items.add(extract(session));
			}
			return items;
		} finally {
            session.close();
		}
	}

    private QueueItem extract(final HypersonicSession session) {
        final Integer queueId = session.getInteger("QUEUEID");
        final String username = session.getString("USERNAME");
        final Integer queueMessageSize = session.getInteger("QUEUEMESSAGESIZE");
        final String queueMessage = session.getString("QUEUEMESSAGE");
        final Calendar createdOn = session.getCalendar("CREATEDON");
        final Calendar updatedOn =  session.getCalendar("UPDATEDON");
        return new QueueItem(createdOn, queueId, queueMessage, queueMessageSize,
                updatedOn, username);
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
