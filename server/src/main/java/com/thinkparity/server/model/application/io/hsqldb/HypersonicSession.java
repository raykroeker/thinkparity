/*
 * Created On: Sep 4, 2006 4:01:31 PM
 */
package com.thinkparity.desdemona.model.io.hsqldb;

import java.io.InputStream;
import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactType;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;

/** 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public final class HypersonicSession {

    /** An apache logger wrapper. */
    protected static final Log4JWrapper LOGGER;

    private static final String SQL_GET_IDENTITY_PRE = "select IDENTITY_VAL_LOCAL() \"ID\" from ";

    /** The local <code>TimeZone</code>. */
    private static final TimeZone TIME_ZONE;

    /** The universal <code>TimeZone</code>. */
    private static final TimeZone UNIVERSAL_TIME_ZONE;

    static {
        LOGGER = new Log4JWrapper("DESDEMONA_SQL_DEBUGGER");
        TIME_ZONE = TimeZone.getDefault();
        UNIVERSAL_TIME_ZONE = TimeZone.getTimeZone("Universal");
    }

    /** A <code>Connection</code>. */
	private Connection connection;

    /** A session unique id <code>JVMUniqueId</code>. */
	private final JVMUniqueId id;

	/** A <code>PreparedStatement</code>. */
	private PreparedStatement preparedStatement;

    /** A <code>ResultSet</code>. */
	private ResultSet resultSet;

    /** A <code>HypersonicSessionManager</code>. */
    private final HypersonicSessionManager sessionManager;

    /**
	 * Create a Session.
	 * 
	 * @param connection
	 *            The sql connection.
	 */
	HypersonicSession(final HypersonicSessionManager sessionManager,
            final Connection connection) {
		super();
		this.connection = connection;
		this.id = JVMUniqueId.nextId();
        this.sessionManager = sessionManager;
	}

	/**
     * Close the session.
     *
     */
	public void close() {
		assertConnectionIsOpen();
		try {
			close(preparedStatement, resultSet);
        } finally {
			try {
                connection.close();
            } catch (final SQLException sqlx) {
                throw panic(sqlx);
            } finally {
                connection = null;
                sessionManager.close(this);
            }
		}
	}

    /**
     * Commit the session.
     *
     */
	public void commit() {
		assertConnectionIsOpen();
		try {
            connection.commit();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof HypersonicSession)
			return id.equals(((HypersonicSession) obj).id);
		return false;
	}

    /**
     * Execute sql.
     * 
     * @param sql
     *            An sql <code>String</code>.
     */
	public void execute(final String sql) {
		assertConnectionIsOpen();
		logStatement(sql);
		try {
			final Statement s = connection.createStatement();
			s.execute(sql);
			commit();
		} catch (final SQLException sqlx) {
			rollback();
			throw panic(sqlx);
		}
	}

    /**
     * Execute a sequence of sql.
     * 
     * @param sql
     *            An sql <code>String[]</code>.
     */
	public void execute(final String[] sql) {
		assertConnectionIsOpen();
		logStatements(sql);
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for(final String s : sql) {
				statement.addBatch(s);
			}
			statement.executeBatch();
		}
		catch(final SQLException sqlx) { throw panic(sqlx); }
		finally { close(statement); }
	}

    /**
     * Execute the SQL query in the session's prepared statement and set the
     * result set.
     * 
     */
	public void executeQuery() {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
		try {
            resultSet = preparedStatement.executeQuery();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	/**
     * Execute the SQL query in the session's prepared statement and set the
     * result set. Return the number of rows updated.
     * 
     * @return The number of rows updated.
     */
	public int executeUpdate() {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
		try {
            return preparedStatement.executeUpdate();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    /**
     * Obtain the blob input stream from the result.
     * 
     * @param columnName
     *            A column name <code>String</code>.
     * @return An <code>InputStream</code>.
     */
    public InputStream getBlob(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Blob value = resultSet.getBlob(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value.getBinaryStream();
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public Boolean getBoolean(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Boolean value = resultSet.getBoolean(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
        }
        catch(final SQLException sqlx) { throw panic(sqlx); }
    }

    public byte[] getBytes(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final byte[] value = resultSet.getBytes(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    public Calendar getCalendar(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final Calendar localCalendar = DateUtil.getInstance();
            localCalendar.setTimeZone(TIME_ZONE);
            final Timestamp timestamp = resultSet.getTimestamp(columnName, localCalendar);
			if (resultSet.wasNull()) {
			    logColumnExtraction(columnName, null);
                return null;
			} else {
                localCalendar.setTimeInMillis(timestamp.getTime());
                logColumnExtraction(columnName, localCalendar);
                return localCalendar;
			}
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    /**
     * Obtain the clob input stream from the result.
     * 
     * @param columnName
     *            A column name <code>String</code>.
     * @return An <code>InputStream</code>.
     */
    public InputStream getClob(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Clob value = resultSet.getClob(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value.getAsciiStream();
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public EMail getEMail(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final String value = resultSet.getString(columnName);
            if (resultSet.wasNull()) {
                logColumnExtraction(columnName, null);
                return null;
            } else {
                logColumnExtraction(columnName, value);
                return EMailBuilder.parse(value);
            }
        } catch (final EMailFormatException efx) {
            throw new HypersonicException(efx);
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

    /**
	 * Obtain the session id.
	 * 
	 * @return The session id.
	 */
	public JVMUniqueId getId() {
        return id;
	}

	/**
     * Execute a query to obtain the identity created.
     * 
     * @return The identity value.
     */
	public Long getIdentity(final String table) {
        final String sql = new StringBuffer(SQL_GET_IDENTITY_PRE)
            .append(table).toString();
        try {
            prepareStatement(sql);
            executeQuery();
            nextResult();
            return getLong("ID");
        } finally {
            close(preparedStatement, resultSet);
        }
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
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final InputStream value = resultSet.getBinaryStream(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

	public Integer getInteger(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

    public Long getLong(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final Long value = resultSet.getLong(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
	}

    public JabberId getQualifiedUsername(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
			final String value = resultSet.getString(columnName);
			if (resultSet.wasNull()) {
                logColumnExtraction(columnName, null);
                return null;
			} else {
                logColumnExtraction(columnName, value);
                return JabberIdBuilder.parse(value);
			}
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		} catch (final IllegalArgumentException iax) {
            throw new HypersonicException(iax);
		}
	}

	public String getString(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final String value = resultSet.getString(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public ArtifactType getTypeFromInteger(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : ArtifactType.fromId(value);
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public ArtifactType getTypeFromString(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final String value = resultSet.getString(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : ArtifactType.valueOf(value);
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public UUID getUniqueId(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
        try {
            final String value = resultSet.getString(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : UUID.fromString(value);
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() {
		return id.hashCode();
	}

    /**
     * Move to the next result.
     * 
     * @return If there exists another result.
     */
	public boolean nextResult() {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            return resultSet.next();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    /**
     * Prepare a statement.
     * 
     * @param sql
     *            An sql <code>String</code>.
     */
	public void prepareStatement(final String sql) {
		assertConnectionIsOpen();
        logStatement(sql);
		try {
			preparedStatement = connection.prepareStatement(sql);
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    /**
     * Rollback any changes made within this session.
     * 
     */
	public void rollback() {
		assertConnectionIsOpen();
		try {
            connection.commit();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    /**
     * Set a clob column value.
     * 
     * @param index
     *            The column index.
     * @param value
     *            The column value.
     * @param bufferSize
     *            The size of the buffer to use when writing the value.
     */
    public void setAsciiStream(final Integer index, final InputStream value,
            final Long length, final Integer bufferSize) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            // NOTE possible loss of precision, long > int
            preparedStatement.setAsciiStream(index, value, length.intValue());
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

    /**
     * Set a blob column value.
     * 
     * @param index
     *            The column index.
     * @param value
     *            The column value.
     * @param bufferSize
     *            The size of the buffer to use when writing the value.
     */
    public void setBinaryStream(final Integer index, final InputStream value,
            final Long length, final Integer bufferSize) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            // NOTE possible loss of precision, long > int
            preparedStatement.setBinaryStream(index, value, length.intValue());
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

	public void setBoolean(final Integer index, final Boolean value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setBoolean(index, value);
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

	public void setBytes(final Integer index, final byte[] value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setBytes(index, value);
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public void setCalendar(final Integer index, final Calendar value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            final Calendar universalCalendar = (Calendar) value.clone();
            universalCalendar.setTimeZone(UNIVERSAL_TIME_ZONE);
			preparedStatement.setTimestamp(index,
					new Timestamp(universalCalendar.getTimeInMillis()), universalCalendar);
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public void setEMail(final Integer index, final EMail value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            if (null == value) {
                preparedStatement.setNull(index, Types.VARCHAR);
            } else {
                preparedStatement.setString(index, value.toString());
            }
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

    public void setEnumTypeAsString(final Integer index, final Enum<?> value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setString(index, value.toString());
        } catch(final SQLException sqlx) {
            throw panic(sqlx);
        }
    }

	public void setInt(final Integer index, final Integer value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setInt(index, value);
		} catch(final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public void setLong(final Integer index, final Long value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            if (null == value) {
                preparedStatement.setNull(index, Types.BIGINT);
            } else {
                preparedStatement.setLong(index, value);
            }
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public void setQualifiedUsername(final Integer index, final JabberId value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.getQualifiedUsername());
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    public void setString(final Integer index, final String value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value);
		} catch(final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    public void setTypeAsInteger(final Integer index, final ArtifactType value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setInt(index, value.getId());
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    public void setTypeAsString(final Integer index, final ArtifactType value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
	}

    public void setTypeAsString(final Integer index, final Enum<?> value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch(final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public void setUniqueId(final Integer index, final UUID value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        }
	}

	/**
	 * Obtain the database metadata.
	 * 
	 * @return The database metadata.
	 * @throws HypersonicException
	 */
	DatabaseMetaData getMetaData() throws HypersonicException {
		assertConnectionIsOpen();
		try {
            return connection.getMetaData();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	/**
     * Assert the connection is open.
     *
     */
	private void assertConnectionIsOpen() {
        try {
    		if (null == connection)
    			throw new HypersonicException("Connection is null.");
            if (connection.isClosed())
                throw new HypersonicException("Connection is closed.");
        } catch (final SQLException sqlx) {
            panic(sqlx);
        }
	}

    /**
     * Assert the prepared statement is set.
     *
     */
	private void assertPreparedStatementIsSet() {
		if (null == preparedStatement)
			throw new HypersonicException("Prepared statement is null.");
	}

    /**
     * Assert the result set is set.
     *
     */
    private void assertResultSetIsSet() {
		if (null == resultSet)
			throw new HypersonicException("Result set is null.");
	}

    /**
     * Close a result set.
     * 
     * @param resultSet
     *            A <code>ResultSet</code>
     */
	private void close(ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (final SQLException sqlx) {
                throw panic(sqlx);
            } finally {
                resultSet = null;
            }
        }
    }

    /**
     * Close a statement.
     * 
     * @param statement
     *            A <code>Statement</code>.
     */
	private void close(Statement statement) {
        if (null != statement) {
    		try {
    		    statement.close();
    		} catch (final SQLException sqlx) {
                throw panic(sqlx);
    		} finally {
    		    statement = null;      
            }
        }
	}

    /**
     * Close a statement as well as a result set.
     * 
     * @param statement
     *            A <code>Statement</code>.
     * @param resultSet
     *            A <code>ResultSet</code>.
     * 
     * @see HypersonicSession#close(Statement)
     * @see HypersonicSession#close(ResultSet)
     */
	private void close(final Statement statement, final ResultSet resultSet) {
		try {
            close(statement);
        } finally {
            close(resultSet);
        }
	}

	/**
     * Log the column name and value.
     * 
     * @param columnName
     *            The column name <code>String</code>.
     * @param columnValue
     *            The column value <code>Object</code>.
     */
    private void logColumnExtraction(final String columnName, final Object columnValue) {
        LOGGER.logDebug("Extract {0}:{1}", columnName, columnValue);
    }

    /**
     * Log the column name and value.
     * 
     * @param columnName
     *            The column name <code>String</code>.
     * @param columnValue
     *            The column value <code>Object</code>.
     */
    private void logColumnInjection(final Integer index, final Object columnValue) {
        LOGGER.logDebug("Inject {0}:{1}", index, columnValue);
    }

    /**
     * Log an sql statement.
     * 
     * @param sql
     *            An sql statement <code>String</code>.
     */
    private void logStatement(final String sql) {
        LOGGER.logDebug("Statement:{0}", sql);
    }

    /**
     * Log a sequence of sql statements.
     * 
     * @param sql
     *            An sql statement <code>String[]</code>.
     */
    private void logStatements(final String[] sql) {
        for(final String s : sql)
            logStatement(s);
    }

    /**
     * Panic. Create a hypersonic runtime exception.
     * 
     * @param sqlx
     *            An <code>SQLException</code>.
     */
    private HypersonicException panic(final SQLException sqlx) {
        return new HypersonicException(sqlx);
    }
}
