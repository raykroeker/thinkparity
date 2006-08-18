/*
 * Feb 9, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb;

import java.io.InputStream;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.DateUtil.DateImage;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.container.ContainerDraft;
import com.thinkparity.model.parity.model.io.md.MetaData;
import com.thinkparity.model.parity.model.io.md.MetaDataType;
import com.thinkparity.model.parity.model.message.system.SystemMessageType;
import com.thinkparity.model.parity.model.user.TeamMemberState;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

import com.thinkparity.migrator.Library;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Session {

    /**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

    /**
	 * The sql connection.
	 * 
	 */
	private final Connection connection;

	/**
	 * The session id.
	 * 
	 */
	private final JVMUniqueId id;

	/**
	 * A prepared statement.
	 * 
	 * @see #prepareStatement(String)
	 */
	private PreparedStatement preparedStatement;

	/**
	 * A result set.
	 * 
	 */
	private ResultSet resultSet;

	/**
	 * Create a Session.
	 * 
	 * @param connection
	 *            The sql connection.
	 */
	Session(final Connection connection) {
		super();
		this.connection = connection;
		this.logger = Logger.getLogger(getClass());
		this.id = JVMUniqueId.nextId();
	}

	public void close() {
		assertOpen("Cannot close a closed session.");
		try {
			close(preparedStatement, resultSet);
			connection.close();
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
		SessionManager.close(this);
	}

    public void commit() {
		assertOpen("commit()");
		try { connection.commit(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof Session)
			return id.equals(((Session) obj).id);
		return false;
	}

	public void execute(final String sql) {
		assertOpen("execute(String)");
		debugSql(sql);
		try {
			final Statement s = connection.createStatement();
			s.execute(sql);
			commit();
		}
		catch(final SQLException sqlx) {
			rollback();
			throw new HypersonicException(sqlx);
		}
	}

	public void execute(final String[] sql) {
		assertOpen("execute(String[]");
		debug(sql);
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for(final String s : sql) {
				statement.addBatch(s);
			}
			statement.executeBatch();
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
		finally { close(statement); }
	}

	/**
	 * Executes the SQL query in the session <code>PreparedStatement</code>
	 * object and sets the <code>ResultSet</code> object generated by the
	 * query.
	 * 
	 * @see #prepareStatement(String)
	 * @see #nextResult()
	 */
	public void executeQuery() {
		assertOpen("Cannot execute query if the session is not open.");
		assertPreparedStatement("Cannot execute query if the statement is not prepared.");
		try { resultSet = preparedStatement.executeQuery(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public int executeUpdate() {
		assertOpen("executeUpdate()");
		assertPreparedStatement("executeUpdate()");
		try { return preparedStatement.executeUpdate(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public AuditEventType getAuditEventTypeFromInteger(final String columnName) {
		assertOpen("getAuditEventTypeFromInteger(String)");
		assertOpenResult("getAuditEventTypeFromInteger(String)");
		debugSql(columnName);
		try { return AuditEventType.fromId(resultSet.getInt(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public Boolean getBoolean(final String columnName) {
        assertOpen("[GET BOOLEAN]");
        assertOpenResult("[GET BOOLEAN]");
        debugSql(columnName);
        try { return resultSet.getBoolean(columnName); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

    public byte[] getBytes(final String columnName) {
		assertOpen("getBytes(String)");
		assertOpenResult("getBytes(String)");
		try { return resultSet.getBytes(columnName); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public Calendar getCalendar(final String columnName) {
		assertOpen("getCalendar(String)");
		assertOpenResult("getCalendar(String)");
		try {
			final Calendar calendar = DateUtil.getInstance();
			final Timestamp timestamp = resultSet.getTimestamp(columnName, calendar);
			if(resultSet.wasNull()) { return null; }
			else {
				calendar.setTime(timestamp);
				return calendar;
			}
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public ArtifactFlag getFlagFromInteger(final String columnName) {
		assertOpen("getFlagFromInteger(String)");
		assertOpenResult("getFlagFromInteger(String)");
		debugSql(columnName);
		try { return ArtifactFlag.fromId(resultSet.getInt(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public ArtifactFlag getFlagFromString(final String columnName) {
		assertOpen("getFlagFromString(String)");
		assertOpenResult("getFlagFromString(String)");
		debugSql(columnName);
		try { return ArtifactFlag.valueOf(resultSet.getString(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	/**
	 * Obtain the session id.
	 * 
	 * @return The session id.
	 */
	public JVMUniqueId getId() { return id; }

	public Long getIdentity() {
		ResultSet resultSet = null;
		try {
			resultSet = list("CALL IDENTITY()");
			resultSet.next();
			return resultSet.getLong(1);
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
		finally { close(resultSet); }
	}

	/**
     * Obtain the input stream from the result.
     * 
     * @param columnName
     *            The column name.
     * @return An input stream.
     * @see ResultSet#getBinaryStream(String)
     */
    public InputStream getInputStream(final String columnName) {
        assertOpen("[GET INPUT STREAM]");
        assertOpenResult("[GET INPUT STREAM]");
        debugSql(columnName);
        try { return resultSet.getBinaryStream(columnName); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

    public Integer getInteger(final String columnName) {
        assertOpen("[LMODEL] [IO] [HSQL] [GET INTEGER]");
        assertOpenResult("[LMODEL] [IO] [HSQL] [GET INTEGER]");
        debugSql(columnName);
        try { return resultSet.getInt(columnName); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

	public Long getLong(final String columnName) {
		assertOpen("getLong(String)");
		assertOpenResult("getLong(String)");
		debugSql(columnName);
		try { return resultSet.getLong(columnName); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public MetaDataType getMetaDataTypeFromInteger(final String columnName) {
		assertOpen("getMetaDataTypeFromInteger(String)");
		assertOpenResult("getMetaDataTypeFromInteger(String)");
		debugSql(columnName);
		try { return MetaDataType.fromId(resultSet.getInt(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public JabberId getQualifiedUsername(final String columnName) {
		assertOpen("Cannot get values if the session is not open.");
		assertOpenResult("Cannot get values if the result is not open.");
		debugSql(columnName);
		try {
			final String qualifiedUsername = resultSet.getString(columnName);
			if(resultSet.wasNull()) { return null; }
			else { return JabberIdBuilder.parseQualifiedUsername(qualifiedUsername); }
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
		catch(final IllegalArgumentException iax) { throw new HypersonicException(iax); }
	}

	public ArtifactState getStateFromInteger(final String columnName) {
		assertOpen("getStateFromInteger(String)");
		assertOpenResult("getStateFromInteger(String)");
		debugSql(columnName);
		try { return ArtifactState.fromId(resultSet.getInt(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public ArtifactState getStateFromString(final String columnName) {
		assertOpen("getStateFromString(String)");
		assertOpenResult("getStateFromString(String)");
		debugSql(columnName);
		try { return ArtifactState.valueOf(resultSet.getString(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public ContainerDraft.ArtifactState getContainerStateFromString(
            final String columnName) {
        assertOpen("getStateFromString(String)");
        assertOpenResult("getStateFromString(String)");
        debugSql(columnName);
        try { return ContainerDraft.ArtifactState.valueOf(resultSet.getString(columnName)); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

	public String getString(final String columnName) {
		assertOpen("getString(String)");
		assertOpenResult("getString(String)");
		debugSql(columnName);
		try { return resultSet.getString(columnName); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public SystemMessageType getSystemMessageTypeFromInteger(
			final String columnName) {
		assertOpen("getSystemMessageTypeFromInteger(String)");
		assertOpenResult("getSystemMessageTypeFromInteger(String)");
		debugSql(columnName);
		try { return SystemMessageType.fromId(resultSet.getInt(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public TeamMemberState getTeamMemberStateFromInteger(
            final String columnName) {
        assertOpen("getArtifactTeamMemberStateFromInteger(String)");
        assertOpenResult("getArtifactTeamMemberStateFromInteger(String)");
        debugSql(columnName);
        try { return TeamMemberState.fromId(resultSet.getInt(columnName)); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

	public ArtifactType getTypeFromInteger(final String columnName) {
		assertOpen("getTypeFromInteger(String)");
		assertOpenResult("getTypeFromInteger(String)");
		debugSql(columnName);
		try { return ArtifactType.fromId(resultSet.getInt(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public ArtifactType getTypeFromString(final String columnName) {
		assertOpen("getTypeFromString(String)");
		assertOpenResult("getTypeFromString(String)");
		debugSql(columnName);
		try { return ArtifactType.valueOf(resultSet.getString(columnName)); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}
	public UUID getUniqueId(final String columnName) {
		assertOpen("getString(String)");
		assertOpenResult("getString(String)");
		return UUID.fromString(getString(columnName));
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() {
		return id.hashCode();
	}

	public boolean nextResult() {
		assertOpen("nextResult()");
		assertOpenResult("nextResult()");
		try { return resultSet.next(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public PreparedStatement prepareStatement(final String sql) {
		assertOpen("prepareStatement(String)");
		debugSql(sql);
		try {
			preparedStatement = connection.prepareStatement(sql);
			return preparedStatement;
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void rollback() {
		assertOpen("rollbackSession()");
		try { connection.commit(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setBytes(final Integer index, final byte[] bytes) {
		assertOpen("setBytes(Integer,Byte[])");
		assertPreparedStatement("setBytes(Integer,Byte[])");
		debugSql(bytes, index);
		try { preparedStatement.setBytes(index, bytes); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setCalendar(final Integer index, final Calendar calendar) {
		assertOpen("setCalendar(Integer,Calendar)");
		assertPreparedStatement("setCalendar(Integer,Calendar)");
		debugSql(calendar, index);
		try {
			preparedStatement.setTimestamp(index,
					new Timestamp(calendar.getTimeInMillis()), calendar);
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setEnumTypeAsString(final Integer index, final Enum<?> enumType) {
        assertOpen("setEnumAsString(Integer,Enum<?>)");
        assertPreparedStatement("setEnumAsString(Integer,Enum<?>)");
        debugSql(null == enumType ? null : enumType.toString(), index);
        try { preparedStatement.setString(index, enumType.toString()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

	public void setFlagAsInteger(final Integer index, final ArtifactFlag flag) {
		assertOpen("setFlagAsInteger(Integer,ArtifactFlag)");
		assertPreparedStatement("setFlagAsInteger(Integer,ArtifactFlag)");
		debugSql(null == flag ? null : flag.getId(), index);
		try { preparedStatement.setInt(index, flag.getId()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setFlagAsString(final Integer index, final ArtifactFlag flag) {
		assertOpen("setFlagAsString(Integer,ArtifactFlag)");
		assertPreparedStatement("setFlagAsString(Integer,ArtifactFlag)");
		debugSql(null == flag ? null : flag.toString(), index);
		try { preparedStatement.setString(index, flag.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setInt(final Integer index, final Integer integer) {
		assertOpen("setInt(Integer,Integer)");
		assertPreparedStatement("setInt(Integer,Integer)");
		debugSql(integer, index);
		try { preparedStatement.setInt(index, integer); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setLong(final Integer index, final Long longInteger) {
		assertOpen("setLong(Integer,Long)");
		assertPreparedStatement("setLong(Integer,Long)");
		debugSql(longInteger, index);
		try { preparedStatement.setLong(index, longInteger); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setMetaDataAsString(final Integer index, final MetaData metaData) {
		assertOpen("setMetaDataAsString(Integer,MetaData)");
		assertPreparedStatement("setMetaDataAsString(Integer,MetaData)");
		debugSql(null == metaData ? null : metaData.toString(), index);
		try { preparedStatement.setString(index, metaData.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setQualifiedUsername(final Integer index,
			final JabberId jabberId) {
		assertOpen("Database session is not open.");
		assertPreparedStatement("Prepared statement has not been set.");
		debugSql(jabberId, index);
		try { preparedStatement.setString(index, jabberId.getQualifiedUsername()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setStateAsInteger(final Integer index, final ArtifactState state) {
		assertOpen("setStateAsInteger(Integer,ArtifactState)");
		assertPreparedStatement("setStateAsInteger(Integer,ArtifactState)");
		debugSql(null == state ? null : state.toString(), index);
		try { preparedStatement.setInt(index, state.getId()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setStateAsInteger(final Integer index,
            final TeamMemberState state) {
        assertOpen("setStateAsInteger(Integer,ArtifactTeamMemberState)");
        assertPreparedStatement("setStateAsInteger(Integer,ArtifactTeamMemberState)");
        debugSql(null == state ? null : state.toString(), index);
        try { preparedStatement.setInt(index, state.getId()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

    public void setStateAsString(final Integer index, final ArtifactState state) {
        assertOpen("setStateAsString(Integer,ArtifactState)");
        assertPreparedStatement("setStateAsString(Integer,ArtifactState)");
        debugSql(null == state ? null : state.toString(), index);
        try { preparedStatement.setString(index, state.toString()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

    public void setStateAsString(final Integer index, final ContainerDraft.ArtifactState state) {
        assertOpen("SESSION CONNECTION NOT OPEN");
        assertPreparedStatement("SESSION STATEMENT NOT PREPARED");
        debugSql(null == state ? null : state.toString(), index);
        try { preparedStatement.setString(index, state.toString()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

	public void setStateAsString(final Integer index,
            final TeamMemberState state) {
        assertOpen("setStateAsString(Integer,ArtifactTeamMemberState)");
        assertPreparedStatement("setStateAsString(Integer,ArtifactTeamMemberState)");
        debugSql(null == state ? null : state.toString(), index);
        try { preparedStatement.setString(index, state.toString()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

    public void setString(final Integer index, final String string) {
		assertOpen("setString(Integer,String)");
		assertPreparedStatement("setString(Integer,String)");
		debugSql(string, index);
		try { preparedStatement.setString(index, string); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setTypeAsInteger(final Integer index, final ArtifactType type) {
		assertOpen("setTypeAsInteger(Integer,ArtifactType)");
		assertPreparedStatement("setTypeAsInteger(Integer,ArtifactType)");
		debugSql(null == type ? null : type.getId(), index);
		try { preparedStatement.setInt(index, type.getId()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setTypeAsInteger(final Integer index, final AuditEventType type) {
		assertOpen("setTypeAsInteger(Integer,AuditEventType)");
		assertPreparedStatement("setTypeAsInteger(Integer,AuditEventType)");
		debugSql(null == type ? null : type.getId(), index);
		try { preparedStatement.setInt(index, type.getId()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setTypeAsInteger(final Integer index, final Library.Type type) {
        assertOpen("setTypeAsInteger(Integer,Library.Type)");
        assertPreparedStatement("setTypeAsInteger(Integer,Library.Type)");
        debugSql(null == type ? null : type.getId(), index);
        try { preparedStatement.setInt(index, type.getId()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

    public void setTypeAsInteger(final Integer index, final MetaDataType type) {
		assertOpen("setTypeAsInteger(Integer,MetaDataType)");
		assertPreparedStatement("setTypeAsInteger(Integer,MetaDataType)");
		debugSql(null == type ? null : type.getId(), index);
		try { preparedStatement.setInt(index, type.getId()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setTypeAsInteger(final Integer index, final SystemMessageType type) {
        assertOpen("setTypeAsInteger(Integer,SystemMessageType)");
        assertPreparedStatement("setTypeAsInteger(Integer,SystemMessageType)");
        debugSql(null == type ? null : type.getId(), index);
        try { preparedStatement.setInt(index, type.getId()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

	public void setTypeAsString(final Integer index, final ArtifactType type) {
		assertOpen("setTypeAsString(Integer,ArtifactType)");
		assertPreparedStatement("setTypeString(Integer,ArtifactType)");
		debugSql(null == type ? null : type.toString(), index);
		try { preparedStatement.setString(index, type.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setTypeAsString(final Integer index, final AuditEventType type) {
		assertOpen("setTypeAsString(Integer,AuditEventType)");
		assertPreparedStatement("setTypeAsString(Integer,AuditEventType)");
		debugSql(null == type ? null : type.toString(), index);
		try { preparedStatement.setString(index, type.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setTypeAsString(final Integer index, final Enum<?> type) {
		assertOpen("setTypeAsString(Integer,Enum<?>)");
		assertPreparedStatement("setTypeString(Integer,Enum<?>)");
		debugSql(null == type ? null : type.toString(), index);
		try { preparedStatement.setString(index, type.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

    public void setTypeAsString(final Integer index, final Library.Type type) {
        assertOpen("setType(Integer,Library.Type)");
        assertPreparedStatement("setTypeString(Integer,Library.Type)");
        debugSql(null == type ? null : type.toString(), index);
        try { preparedStatement.setString(index, type.toString()); }
        catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
    }

	public void setTypeAsString(final Integer index, final MetaDataType type) {
		assertOpen("setType(Integer,MetaDataType)");
		assertPreparedStatement("setTypeString(Integer,MetaDataType)");
		debugSql(null == type ? null : type.toString(), index);
		try { preparedStatement.setString(index, type.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setTypeAsString(final Integer index, final SystemMessageType type) {
		assertOpen("setTypeAsString(Integer,SystemMessageType)");
		assertPreparedStatement("setTypeAsString(Integer,SystemMessageType)");
		debugSql(null == type ? null : type.toString(), index);
		try { preparedStatement.setString(index, type.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	public void setUniqueId(final Integer index, final UUID uniqueId) {
		assertOpen("setUniqueId(Integer,UUID)");
		assertPreparedStatement("setUniqueId(Integer,UUID)");
		debugSql(null == uniqueId ? null : uniqueId.toString(), index);
		try { preparedStatement.setString(index, uniqueId.toString()); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	/**
	 * Obtain the database metadata.
	 * 
	 * @return The database metadata.
	 * @throws HypersonicException
	 */
	DatabaseMetaData getMetaData() throws HypersonicException {
		assertOpen("getMetaData");
		try { return connection.getMetaData(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}


	private void assertOpen(final String caller) {
		if(null == connection) {
			throw new HypersonicException("Session is closed:  " + caller);
		}
	}

	private void assertOpenResult(final String caller) {
		if(null == resultSet)
			throw new HypersonicException("Result is closed:  " + caller);
	}

	private void assertPreparedStatement(final String caller) {
		if(null == preparedStatement)
			throw new HypersonicException("Prepared statement is null:  " + caller);
	}

	private void close(final ResultSet resultSet) {
		try { if(null != resultSet) resultSet.close(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	private void close(final Statement statement) {
		try { if(null != statement) statement.close(); }
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
	}

	private void close(final Statement statement, final ResultSet resultSet) {
		close(statement);
		close(resultSet);
	}

	private void debug(final String[] sql) {
		if(null != sql) {
			int index = 0;
			for(final String s : sql) { debugSql(s, index++); }
		}
		else { debugSql((String) null, null); }
	}

	private void debugSql(final byte[] bytes, final Integer sqlIndex) {
		debugSql(null == bytes ? null : bytes.length, sqlIndex);
	}

	private void debugSql(final Calendar calendar, final Integer sqlIndex) {
		debugSql(null == calendar ? null : DateUtil.format(calendar, DateImage.ISO), sqlIndex);
	}

	private void debugSql(final Integer integer, final Integer sqlIndex) {
		debugSql(null == integer ? null : integer.toString(), sqlIndex);
	}

	private void debugSql(final JabberId jabberId, final Integer sqlIndex) {
		debugSql(null == jabberId ? null : jabberId.toString(), sqlIndex);
	}

	private void debugSql(final Long longInteger, final Integer sqlIndex) {
		debugSql(null == longInteger ? null : longInteger.toString(), sqlIndex);
	}

	private void debugSql(final String sql) {
		debugSql(sql, null);
	}

	private void debugSql(final String sql, final Integer sqlIndex) {
        if(logger.isDebugEnabled()) {
            final String pattern;
            if(null == sqlIndex) {
                pattern = "SQL Statement:{1}";
            } else {
                pattern = "SQL Variable:{0}:{1}";
            }
            logger.debug(MessageFormat.format(pattern, sqlIndex, sql));
            
        }
	}

    private ResultSet list(final String sql) {
		assertOpen("list(String)");
		debugSql(sql);
		Statement statement = null;
		try {
			statement = connection.createStatement();
			return statement.executeQuery(sql);
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
		finally { close(statement); }
	}
}
