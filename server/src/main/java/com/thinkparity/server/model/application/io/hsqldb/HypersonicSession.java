/*
 * Created On: Sep 4, 2006 4:01:31 PM
 */
package com.thinkparity.desdemona.model.io.hsqldb;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
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

/** 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public final class HypersonicSession {

    /** An apache logger wrapper. */
    protected static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper("SQL_DEBUGGER");
    }

    /** A <code>Connection</code>. */
	private final Connection connection;

	/** A session unique id <code>JVMUniqueId</code>. */
	private final JVMUniqueId id;

    /** A <code>PreparedStatement</code>. */
	private PreparedStatement preparedStatement;

    /** A <code>ResultSet</code>. */
	private ResultSet resultSet;

	/**
	 * Create a Session.
	 * 
	 * @param connection
	 *            The sql connection.
	 */
	HypersonicSession(final Connection connection) {
		super();
		this.connection = connection;
		this.id = JVMUniqueId.nextId();
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
                HypersonicSessionManager.close(this);
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
			final Calendar value = DateUtil.getInstance();
			final Timestamp timestamp = resultSet.getTimestamp(columnName, value);
			if (resultSet.wasNull()) {
			    logColumnExtraction(columnName, null);
                return null;
			} else {
				value.setTime(timestamp);
                logColumnExtraction(columnName, value);
				return value;
			}
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
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
	public Long getIdentity() {
	    assertConnectionIsOpen();
        final String sql = "CALL IDENTITY()";
        logStatement(sql);
		ResultSet resultSet = null;
		Statement statement = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return null;
            }
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        } finally {
            close(statement, resultSet);
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
			preparedStatement.setTimestamp(index,
					new Timestamp(value.getTimeInMillis()), value);
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

	public void setEMail(final Integer index, final EMail value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setString(index, value.toString());
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
            preparedStatement.setLong(index, value);
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
	private void close(final ResultSet resultSet) {
		try {
            if (null != resultSet)
                resultSet.close();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
		}
	}

    /**
     * Close a statement.
     * 
     * @param statement
     *            A <code>Statement</code>.
     */
	private void close(final Statement statement) {
		try {
            if(null != statement)
                statement.close();
		} catch (final SQLException sqlx) {
            throw panic(sqlx);
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
		close(statement);
		close(resultSet);
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
