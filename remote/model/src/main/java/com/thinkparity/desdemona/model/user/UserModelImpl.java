/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.VCardReader;
import com.thinkparity.codebase.model.util.VCardWriter;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.UserSql;

/**
 * <b>Title:</b>thinkParity DesdemonaModel User Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UserModelImpl extends AbstractModelImpl implements UserModel,
        InternalUserModel {

    /** User sql interface. */
    private UserSql userSql;

    /**
     * Create UserModelImpl.
     *
     */
    public UserModelImpl() {
        super();
    }

	/**
     * @see com.thinkparity.desdemona.model.user.InternalUserModel#doesExistUser(com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    @Override
    public Boolean doesExistUser(final Product product, final Release release) {
        try {
            return userSql.doesExistUser(product, release);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }


    /**
     * @see com.thinkparity.desdemona.model.user.UserModel#read()
     * 
     */
    public List<User> read() {
        try {
            return read(FilterManager.createDefault());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.user.UserModel#read(com.thinkparity.codebase.email.EMail)
     * 
     */
    public User read(final EMail email) {
        try {
            return userSql.read(email);
        } catch(final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.user.UserModel#read(com.thinkparity.codebase.filter.Filter)
     * 
     */
    public List<User> read(final Filter<? super User> filter) {
        logApiId();
        logVariable("filter", filter);
        try {
            final List<User> users = userSql.read();
            FilterManager.filter(users, filter);
            for (final User user : users) {
                inject(user, readVCard(user, new UserVCard()));
            }
            return users;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.user.UserModel#read(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public User read(final JabberId userId) {
        try {
            final User user = userSql.read(userId);
            if (null == user) {
                return null;
            } else {
                return inject(user, readVCard(user, new UserVCard()));
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * @see com.thinkparity.desdemona.model.user.UserModel#read(java.util.List)
     * 
     */
    public List<User> read(final List<JabberId> userIds) {
        logApiId();
        logVariable("userIds", userIds);
		try {
			final List<User> users = new ArrayList<User>(userIds.size());
			for(final JabberId userId : userIds) {
				users.add(read(userId));
			}
			return users;
		} catch(final Throwable t) {
            throw translateError(t);
		}
	}

    /**
     * @see com.thinkparity.desdemona.model.user.InternalUserModel#read(java.lang.Long)
     *
     */
    public User read(final Long userId) {
        try {
            final User user = userSql.read(userId);
            if (null == user) {
                return null;
            } else {
                return inject(user, readVCard(user, new UserVCard()));
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.user.InternalUserModel#readProductFeatures(java.lang.String)
     *
     */
    public List<Feature> readProductFeatures(final String name) {
        try {
            return userSql.readProductFeatures(user, name);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.user.UserModel#readVCard(com.thinkparity.codebase.model.user.UserVCard)
     * 
     */
    public <T extends com.thinkparity.codebase.model.user.UserVCard> T readVCard(
            final T vcard) {
        try {
            return readVCard(user, vcard);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.user.UserModel#updateVCard(com.thinkparity.codebase.model.user.UserVCard)
     * 
     */
    public void updateVCard(
            final com.thinkparity.codebase.model.user.UserVCard vcard) {
        try {
            userSql.updateVCard(user.getLocalId(), vcard, new VCardWriter<com.thinkparity.codebase.model.user.UserVCard>() {
                public void write(final com.thinkparity.codebase.model.user.UserVCard vcard, final Writer writer) throws IOException {
                    final StringWriter stringWriter = new StringWriter();
                    XSTREAM_UTIL.toXML(vcard, stringWriter);
                    try {
                        writer.write(encrypt(stringWriter.toString()));
                    } catch (final GeneralSecurityException gsx) {
                        logger.logError(gsx, "Could not encrypt vcard.");
                        throw new IOException(gsx);
                    }
                }
            });
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
        userSql = new UserSql();
    }

    /**
     * Inject the user's vcard fields into the user object.
     * 
     * @param user
     *            A <code>User</code>.
     * @param vcard
     *            A user's vcard dom4j <code>Element</code>.
     */
    private User inject(final User user, final UserVCard vcard) {
        user.setName(vcard.getName());
        user.setOrganization(vcard.getOrganization());
        user.setTitle(vcard.getTitle());
        return user;
    }


    /**
     * Read a vcard for a user.
     * 
     * @param <T>
     *            A vcard type.
     * @param user
     *            A <code>User</code>.
     * @param vcard
     *            A <code>VCard</code>.
     * @return A <code>UserVCard</code>.
     */
    private <T extends com.thinkparity.codebase.model.user.UserVCard> T readVCard(
            final User user, final T vcard) {
        try {
            return userSql.readVCard(user.getLocalId(), vcard, new VCardReader<T> () {
                public void read(final T vcard, final Reader reader) throws IOException {
                    try {
                        final StringBuilder encrypted = new StringBuilder();
                        final char[] buffer = new char[1024];
                        int charsRead;
                        while (true) {
                            charsRead = reader.read(buffer);
                            if (-1 == charsRead) {
                                break;
                            }
                            encrypted.append(buffer);
                        }
                        XSTREAM_UTIL.fromXML(new StringReader(decrypt(
                                encrypted.toString())), vcard);
                    } catch (final GeneralSecurityException gsx) {
                        throw new IOException(gsx);
                    }
                }
            });
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
}
