/*
 * Feb 9, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
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

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.ArtifactVersionFlag;
import com.thinkparity.codebase.model.user.UserFlag;
import com.thinkparity.codebase.model.user.UserVCard;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.ophelia.model.audit.AuditEventType;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.io.md.MetaData;
import com.thinkparity.ophelia.model.io.md.MetaDataType;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity SQL Session<br>
 * <b>Description:</b>A thinkParity sql session. A wrapper around establishing
 * a connection; creation of cached query statements; as well as statement
 * variable injection and result parameter extraction.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.19
 */
public final class Session {

    /** An apache logger wrapper. */
    protected static final Log4JWrapper LOGGER;

    /** An <code>XStreamUtil</code> instance. */
    protected static final XStreamUtil XSTREAM_UTIL;

    /** Sql prefix to retreive an identity value. */
    private static final String SQL_GET_IDENTITY_PRE = "select IDENTITY_VAL_LOCAL() \"ID\" from ";

    /** The local <code>TimeZone</code>. */
    private static final TimeZone TIME_ZONE;

    /** The universal <code>TimeZone</code>. */
    private static final TimeZone UNIVERSAL_TIME_ZONE;

	static {
        LOGGER = new Log4JWrapper("SQL_DEBUGGER");
        TIME_ZONE = TimeZone.getDefault();
        UNIVERSAL_TIME_ZONE = TimeZone.getTimeZone("Universal");
        XSTREAM_UTIL = XStreamUtil.getInstance();
    }

    /** The sql connection. */
	private Connection connection;

    /** The session id. */
	private final JVMUniqueId id;

    /** The connection meta data. */
    private DatabaseMetaData metaData;

    /** The session's prepared statement. */
	private PreparedStatement preparedStatement;

	/** The session's query. */
    private String query;

    /** The prepared statement's result set. */
	private ResultSet resultSet;

    /** The <code>SessionManager</code>. */
    private final SessionManager sessionManager;

    /**
     * Create a Session.
     * 
     * @param sessionManager
     *            The <code>SessionManager</code>.
     * @param connection
     *            The sql <code>Connection</code>.
     */
	Session(final SessionManager sessionManager, final Connection connection) {
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
            close(metaData, preparedStatement, resultSet);
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof Session)
			return id.equals(((Session) obj).id);
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
        } catch (final SQLException sqlx) {
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
        } catch (final SQLException sqlx) {
            throw panic(sqlx);
        } finally {
            close(statement);
        }
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

    public List<ArtifactFlag> getArtifactFlags(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Integer value = resultSet.getInt(columnName);
            if (resultSet.wasNull()) {
                logColumnExtraction(columnName, null);
                return Collections.emptyList();
            } else {
                logColumnExtraction(columnName, value);
                return ArtifactFlag.fromIdSum(value);
            }
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        } catch (final IllegalArgumentException iax) {
            throw new HypersonicException(iax);
        }
    }

    public List<ArtifactVersionFlag> getArtifactVersionFlags(
            final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Integer value = resultSet.getInt(columnName);
            if (resultSet.wasNull()) {
                logColumnExtraction(columnName, null);
                return Collections.emptyList();
            } else {
                logColumnExtraction(columnName, value);
                return ArtifactVersionFlag.fromIdSum(value);
            }
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        } catch (final IllegalArgumentException iax) {
            throw new HypersonicException(iax);
        }
    }

    public AuditEventType getAuditEventTypeFromInteger(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : AuditEventType.fromId(value);
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public byte[] getBytes(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final byte[] value = resultSet.getBytes(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	public Calendar getCalendar(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
			final Calendar universalCalendar = DateUtil.getInstance();
            universalCalendar.setTimeZone(UNIVERSAL_TIME_ZONE);
			final Timestamp timestamp = resultSet.getTimestamp(columnName, universalCalendar);
			if (resultSet.wasNull()) {
                logColumnExtraction(columnName, null);
                return null;
			}
			else {
                final Calendar localCalendar = DateUtil.getInstance();
                localCalendar.setTimeZone(TIME_ZONE);
                localCalendar.setTimeInMillis(timestamp.getTime());
                logColumnExtraction(columnName, localCalendar);
				return localCalendar;
			}
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public ContainerDraft.ArtifactState getContainerStateFromString(
            final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final String value = resultSet.getString(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : ContainerDraft.ArtifactState.valueOf(value);
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
            throw new HypersonicException(sqlx);
        }
    }

    public ArtifactFlag getFlagFromInteger(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : ArtifactFlag.fromId(value);
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public ArtifactFlag getFlagFromString(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final String value = resultSet.getString(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : ArtifactFlag.valueOf(value);
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    /**
	 * Obtain the session id.
	 * 
	 * @return The session id.
	 */
	public JVMUniqueId getId() { return id; }

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

    public Integer getInteger(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : value;
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
            throw new HypersonicException(sqlx);
        }
	}

    public void getMetaDataTables() {
        getMetaDataTables(null);
    }

    public void getMetaDataTables(final String tableName) {
        assertConnectionIsOpen();
        assertMetaDataIsSet();
        try {
            resultSet = metaData.getTables(null, null, tableName,
                    new String[] { "TABLE" });
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public MetaDataType getMetaDataTypeFromInteger(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : MetaDataType.fromId(value);
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
            throw new HypersonicException(sqlx);
		} catch (final IllegalArgumentException iax) {
            throw new HypersonicException(iax);
		}
	}

    public ArtifactState getStateFromInteger(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : ArtifactState.fromId(value);
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public ArtifactState getStateFromString(final String columnName) {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            final String value = resultSet.getString(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : ArtifactState.valueOf(value);
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
            throw new HypersonicException(sqlx);
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
            throw new HypersonicException(sqlx);
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
            throw new HypersonicException(sqlx);
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

	public UserFlag getUserFlagFromInteger(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Integer value = resultSet.getInt(columnName);
            logColumnExtraction(columnName, value);
            return resultSet.wasNull() ? null : UserFlag.fromId(value);
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

	public List<UserFlag> getUserFlags(final String columnName) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final Integer value = resultSet.getInt(columnName);
            if (resultSet.wasNull()) {
                logColumnExtraction(columnName, null);
                return Collections.emptyList();
            } else {
                logColumnExtraction(columnName, value);
                return UserFlag.fromIdSum(value);
            }
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        } catch (final IllegalArgumentException iax) {
            throw new HypersonicException(iax);
        }
    }

	public <T extends UserVCard> T getVCard(final String columnName,
            final T vcard) {
        assertConnectionIsOpen();
        assertResultSetIsSet();
        try {
            final String vcardXML = resultSet.getString(columnName);
            if (resultSet.wasNull()) {
                logColumnExtraction(columnName, null);
                return null;
            }
            else {
                logColumnExtraction(columnName, vcardXML);
                final StringReader vcardXMLReader = new StringReader(vcardXML);
                XSTREAM_UTIL.fromXML(vcardXMLReader, vcard);
                return vcard;
            }
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() {
		return id.hashCode();
	}

	public boolean nextResult() {
		assertConnectionIsOpen();
		assertResultSetIsSet();
		try {
            return resultSet.next();
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	/**
	 * Obtain the database metadata.
	 * 
	 * @return The database metadata.
	 * @throws HypersonicException
	 */
	public void openMetaData() {
		assertConnectionIsOpen();
		try {
            metaData = connection.getMetaData();
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void prepareStatement(final String query) {
		assertConnectionIsOpen();
		logStatement(query);
		try {
            this.query = query;
			preparedStatement = connection.prepareStatement(query);
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void setArtifactFlags(final Integer index, final List<ArtifactFlag> value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setInt(index, ArtifactFlag.toIdSum(value));
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public void setArtifactVersionFlags(final Integer index,
            final List<ArtifactVersionFlag> value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setInt(index, ArtifactVersionFlag.toIdSum(value));
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
        } catch(final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public void setBytes(final Integer index, final byte[] value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setBytes(index, value);
		} catch(final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
            throw new HypersonicException(sqlx);
		}
	}

    public void setEMail(final Integer index, final EMail value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setString(index, value.toString());
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public void setEnumTypeAsString(final Integer index, final Enum<?> value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setString(index, value.toString());
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

	public void setFlagAsInteger(final Integer index, final ArtifactFlag value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setInt(index, value.getId());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	public void setFlagAsString(final Integer index, final ArtifactFlag value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	public void setInt(final Integer index, final Integer value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setInt(index, value);
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
            throw new HypersonicException(sqlx);
		}
	}

    public void setMetaDataAsString(final Integer index, final MetaData value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	public void setQualifiedUsername(final Integer index, final JabberId value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.getQualifiedUsername());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	public void setStateAsInteger(final Integer index, final ArtifactState value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setInt(index, value.getId());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void setStateAsString(final Integer index, final ArtifactState value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setString(index, value.toString());
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public void setStateAsString(final Integer index, final ContainerDraft.ArtifactState value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setString(index, value.toString());
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public void setString(final Integer index, final String value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            if (null == value) {
                preparedStatement.setNull(index, Types.VARCHAR);
            } else {
                preparedStatement.setString(index, value);
            }
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	public void setTypeAsInteger(final Integer index, final ArtifactType value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setInt(index, value.getId());
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public void setTypeAsInteger(final Integer index, final AuditEventType value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setInt(index, value.getId());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void setTypeAsInteger(final Integer index, final MetaDataType value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setInt(index, value.getId());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void setTypeAsInteger(final Integer index, final UserFlag value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setInt(index, value.getId());
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public void setTypeAsString(final Integer index, final ArtifactType value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
	}

	public void setTypeAsString(final Integer index, final AuditEventType value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void setTypeAsString(final Integer index, final Enum<?> value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void setTypeAsString(final Integer index, final MetaDataType value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

	public void setUniqueId(final Integer index, final UUID value) {
		assertConnectionIsOpen();
		assertPreparedStatementIsSet();
        logColumnInjection(index, value);
		try {
            preparedStatement.setString(index, value.toString());
		} catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
		}
	}

    public void setUserFlags(final Integer index, final List<UserFlag> value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            preparedStatement.setInt(index, UserFlag.toIdSum(value));
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    public <T extends UserVCard> void setVCard(final Integer index,
            final T value) {
        assertConnectionIsOpen();
        assertPreparedStatementIsSet();
        logColumnInjection(index, value);
        try {
            final StringWriter valueWriter = new StringWriter();
            XSTREAM_UTIL.toXML(value, valueWriter);
            preparedStatement.setString(index, valueWriter.toString());
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
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
            throw panic(sqlx);
        }
    }

    /**
     * Assert the meta data is set.
     *
     */
	private void assertMetaDataIsSet() {
        if (null == metaData)
            throw new HypersonicException("Meta data is null.");
    }

	/**
     * Assert the prepared statement is set.
     *
     */
    private void assertPreparedStatementIsSet() {
        if (null == query)
            throw new HypersonicException("Query is not set.");
        if (null == preparedStatement)
            throw new HypersonicException("Prepared statement is not set.");
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
     * Close datbase meta data.
     * 
     * @param statement
     *            A <code>Statement</code>.
     */
    private void close(DatabaseMetaData databaseMetaData) {
        if(null != databaseMetaData) {
            databaseMetaData = null;
        }
    }

    private void close(final DatabaseMetaData databaseMetaData,
            final Statement statement, final ResultSet resultSet) {
        try {
            close(databaseMetaData);
        } finally {
            close(statement, resultSet);
        }
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
        if(null != statement) {
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
     * @param t
     *            A <code>Throwable</code>.
     */
    private HypersonicException panic(final Throwable t) {
        if (HypersonicException.class.isAssignableFrom(t.getClass())) {
            return (HypersonicException) t;
        } else {
            return new HypersonicException(t);
        }
    }
}
