/*
 * Created On: Aug 6, 2005
 * $Id$
 */
package com.thinkparity.model.parity.model;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.l10n.L18nContext;

import com.thinkparity.model.LoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.audit.AuditModel;
import com.thinkparity.model.parity.model.audit.InternalAuditModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.download.DownloadModel;
import com.thinkparity.model.parity.model.download.InternalDownloadModel;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.model.parity.model.library.InternalLibraryModel;
import com.thinkparity.model.parity.model.library.LibraryModel;
import com.thinkparity.model.parity.model.message.system.InternalSystemMessageModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.release.InternalReleaseModel;
import com.thinkparity.model.parity.model.release.ReleaseModel;
import com.thinkparity.model.parity.model.session.Credentials;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.user.InternalUserModel;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.Base64;
import com.thinkparity.model.parity.util.MD5Util;
import com.thinkparity.model.util.l10n.ModelL18n;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.user.User;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * AbstractModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

	/**
	 * Assertion message to be displayed if the username is not set in the
	 * parity preferences.
	 */
	private static final String ASSERT_IS_SET_USERNAME = new StringBuffer()
		.append("Before you can create the first parity artifact; you will ")
		.append("need to establish a parity session.").toString();

	/**
	 * The session model context
	 * 
	 * @see #getSessionModelContext()
	 */
	private static Context sessionModelContext;

	/**
	 * Obtain the current date\time.
	 * 
	 * @return The current date\time.
	 */
	protected static Calendar currentDateTime() { return DateUtil.getInstance(); }

	protected static StringBuffer getModelId(final String model) {
        return new StringBuffer("[LMODEL] [").append(model).append("]");
    }

	/**
	 * Obtain the session model context.
	 * 
	 * @return The session model context.
	 */
	protected static Context getSessionModelContext() {
		if(null == sessionModelContext) {
			sessionModelContext = new Context(SessionModel.class);
		}
		return sessionModelContext;
	}

	/** The configuration io. */
    protected ConfigurationIOHandler configurationIO;

	/**
	 * The parity model context.
	 * 
	 */
	protected final Context context;

	/**
	 * The model's l18n.
	 * 
	 */
	protected final L18n l18n;

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Handle to the parity model preferences.
	 */
	protected final Preferences preferences;

    /**
	 * Handle to the parity model workspace.
	 */
	protected final Workspace workspace;

	/** The decryption cipher. */
    private transient Cipher decryptionCipher;

	/** The encryption cipher. */
    private transient Cipher encryptionCipher;

    /** The secret key spec. */
    private transient SecretKeySpec secretKeySpec;

    /**
	 * Create a AbstractModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	protected AbstractModelImpl(final Workspace workspace) {
		this(workspace, null);
	}

    /**
	 * Create an AbstractModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	protected AbstractModelImpl(final Workspace workspace,
			final L18nContext l18nContext) {
		super();
		this.context = new Context(getClass());
		this.l18n = null == l18nContext ? null : new ModelL18n(l18nContext);
        this.logger = LoggerFactory.getLogger(getClass());
		this.workspace = workspace;
		this.preferences = (null == workspace ? null : workspace.getPreferences());
	}

    /**
     * Assert that the artifact is closed.
     * 
     * @param assertion
     *            The assertion.
     * @param artifact
     *            The artifact.
     */
    protected void assertIsClosed(final String assertion,
            final Artifact artifact) {
        Assert.assertTrue(assertion, isClosed(artifact));
    }

	/**
     * Assert the user is the key holder. An assertion that the user is online
     * is also made.
     * 
     * @param assertion
     *            The assertion message.
     * @param artifactId
     *            The artifact id.
     */
    protected void assertIsKeyHolder(final String assertion,
            final Long artifactId) throws ParityException {
        Assert.assertTrue(assertion, isOnline());
        Assert.assertTrue(assertion, isKeyHolder(artifactId));
    }

	/**
     * Assert that the logged in user is not the key holder.
     * 
     * @param assertion
     *            The assertion message.
     * @param artifactId
     *            The artifact id.
     */
	protected void assertIsNotKeyHolder(final String assertion,
            final Long artifactId) throws ParityException {
		Assert.assertNotTrue(assertion, isKeyHolder(artifactId));
	}

    /**
	 * Assert that the model framework is initialized to a state where the user
	 * can start to create artifacts. This requires:
	 * <ol>
	 * <li>The user has logged in at least once.</li>
	 * </ol>
	 * 
	 */
	protected void assertIsSetCredentials() {
		Assert.assertTrue(ASSERT_IS_SET_USERNAME, isSetCredentials());
	}

    /**
     * Ensure the user is not online.
     * 
     * @param assertion
     *            The assertion.
     */
    protected void assertNotIsOnline(final String assertion) {
        Assert.assertNotTrue(assertion, isOnline());
    }

	/**
	 * Assert that the calling method has not yet been implemented.
	 *
	 */
	protected void assertNYI() {
		Assert.assertNotYetImplemented("The calling method has not yet been implemented.");
	}

	/**
     * Assert the user is online.
     *
     * @param assertion
     *      The assertion.
     */
    protected void assertOnline(final String assertion) {
        Assert.assertTrue(assertion, isOnline());
    }

    protected void assertOnline(final StringBuffer api) {
        assertOnline(api.toString());
    }

	/**
	 * Assert that the state transition from currentState to newState can be
	 * made safely.
	 * 
	 * @param currentState The artifact's current state.
	 * @param intentedState
	 *            The artifact's intended state.
	 * 
	 * @throws NotTrueAssertion
	 *             If the state cannot be moved.
	 */
	protected void assertStateTransition(final ArtifactState currentState,
			final ArtifactState intendedState) {
		switch(currentState) {
		case ACTIVE:
			// i can close it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new ArtifactState[] {ArtifactState.CLOSED}),
						ArtifactState.CLOSED == intendedState);
			break;
		case CLOSED:
			// i can reactivate it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new ArtifactState[] {ArtifactState.ACTIVE}),
						ArtifactState.ACTIVE == intendedState);
			break;
		default: Assert.assertUnreachable("Unknown artifact state:  " + currentState);
		}
	}

    /**
	 * Build a jabber id from a parity user.
	 * 
	 * @param user
	 *            The parity user.
	 * @return The jabber id.
	 */
	protected JabberId buildJabberId(final User user) {
		JabberId jabberId = null;
		try { jabberId =
			JabberIdBuilder.parseQualifiedJabberId(user.getUsername()); }
		catch(final IllegalArgumentException iax) {}
		if(null != jabberId) {
			try {
				jabberId =
					JabberIdBuilder.parseQualifiedUsername(user.getUsername());
			}
			catch(final IllegalArgumentException iax) {}
			if(null != jabberId)
				jabberId = JabberIdBuilder.parseUsername(user.getUsername());
		}
		return jabberId;
	}

	/**
     * Create the user credentials.
     * 
     * @param username
     *            The user's username.
     * @param password
     *            The user's password.
     */
    protected Credentials createCredentials(final String username,
            final String password) {
        final String cipherKey = "18273-4897-12-53974-816523-49-81623-95-4-91-8723-56974812-63498-612395-498-7125-349871265-47892-1539784-1523954-19-287356-4";
        try {
            getConfigurationHandler().create(ConfigurationKeys.USERNAME, encrypt(cipherKey, username));
            getConfigurationHandler().create(ConfigurationKeys.PASSWORD, encrypt(cipherKey, password));
        }
        catch(final BadPaddingException bpx) { throw new RuntimeException("", bpx); }
        catch(final IOException iox) { throw new RuntimeException("", iox); }
        catch(final IllegalBlockSizeException ibsx) { throw new RuntimeException("", ibsx); }
        catch(final InvalidKeyException ikx) { throw new RuntimeException("", ikx); }
        catch(final NoSuchAlgorithmException nsax) { throw new RuntimeException("", nsax); }
        catch(final NoSuchPaddingException nspx) { throw new RuntimeException("", nspx); }

        return readCredentials();
    }

	/**
     * Obtain the current user.
     *
     * @return The current user.
     */
    protected User currentUser() throws ParityException {
        assertOnline("[LMODEL] [ABSTRACTION] [CURRENT USER] [NOT ONLINE]");
        return getInternalSessionModel().getLoggedInUser();
    }

	/**
	 * Obtain the current user id.
	 * 
	 * @return The jabber id of the current user.
	 */
	protected JabberId currentUserId() {
        final Credentials credentials = readCredentials();
        if(null == credentials) { return null; }
        else { return JabberIdBuilder.parseUsername(credentials.getUsername()); }
	}

	protected Long getArtifactId(final UUID artifactUniqueId)
			throws ParityException {
		// NOTE I'm assuming document
		final InternalDocumentModel iDModel = getInternalDocumentModel();
		final Document d = iDModel.get(artifactUniqueId);
		if(null == d) {
			logger.warn("Local document:  " + artifactUniqueId + " does not exist.");
			return null;
		}
		else { return d.getId(); }
	}

	protected UUID getArtifactUniqueId(final Long artifactId)
			throws ParityException {
		// NOTE I'm assuming document :)
		final InternalDocumentModel iDocumentModel = getInternalDocumentModel();
		return iDocumentModel.get(artifactId).getUniqueId();
	}

    protected UUID getArtifactUniqueId(final Long artifactId,
			final ArtifactType artifactType) throws ParityException {
		switch(artifactType) {
		case DOCUMENT:
			final InternalDocumentModel iDocumentModel = getInternalDocumentModel();
			return iDocumentModel.get(artifactId).getUniqueId();
		default:
			throw Assert.createUnreachable("");
		}
	}

    /**
	 * Obtain the model's context.
	 * 
	 * @return The model's context.
	 */
	protected Context getContext() { return context; }

    /**
	 * Obtain a the parity document interface.
	 * 
	 * @return The parity document interface.
	 */
	protected DocumentModel getDocumentModel() { return DocumentModel.getModel(); }

    /**
     * Obtain the internal parity artifact interface.
     * 
     * @return The internal parity artifact interface.
     */
	protected InternalArtifactModel getInternalArtifactModel() {
		return ArtifactModel.getInternalModel(context);
	}

    /**
     * Obtain the internal parity audit interface.
     * 
     * @return The internal parity audit interface.
     */
    protected InternalAuditModel getInternalAuditModel() {
		return AuditModel.getInternalModel(context);
	}

	/**
     * Obtain the internal parity document interface.
     * 
     * @return The internal parity document interface.
     */
	protected InternalDocumentModel getInternalDocumentModel() {
		return DocumentModel.getInternalModel(context);
	}

    /**
     * Obtain the internal parity download interface.
     *
     * @return The internal parity download interface.
     */
    protected InternalDownloadModel getInternalDownloadModel() {
        return DownloadModel.getInternalModel(context);
    }

    /**
     * Obtain the internal parity library interface.
     *
     * @return The internal parity library interface.
     */
    protected InternalLibraryModel getInternalLibraryModel() {
        return LibraryModel.getInternalModel(context);
    }

    /**
     * Obtain the internal parity release interface.
     *
     * @return The internal parity release interface.
     */
    protected InternalReleaseModel getInternalReleaseModel() {
        return ReleaseModel.getInternalModel(getContext());
    }

	/**
     * Obtain the internal parity session interface.
     * 
     * @return The internal parity session interface.
     */
	protected InternalSessionModel getInternalSessionModel() {
		return SessionModel.getInternalModel(getContext());
	}

	/**
     * Obtain the internal parity system message interface.
     * 
     * @return The internal parity system message interface.
     */
	protected InternalSystemMessageModel getInternalSystemMessageModel() {
		return SystemMessageModel.getInternalModel(context);
	};

	/**
     * Obtain the internal parity user interface.
     * 
     * @return The internal parity user interface.
     */
    protected InternalUserModel getInternalUserModel() {
        return UserModel.getInternalModel(context);
    }

	/**
	 * Obtain the model's localization.
	 * 
	 * @return The model's localization.
	 */
	protected L18n getL18n() { return l18n; }

	protected StringBuffer getLogId(final Library library) {
        if(null == library) { return new StringBuffer("null"); }
        else {
            return new StringBuffer()
                .append(library.getId())
                .append(":").append(library.getGroupId())
                .append(":").append(library.getArtifactId())
                .append(":").append(library.getVersion())
                .append(":").append(DateUtil.format(
                        library.getCreatedOn(), DateUtil.DateImage.ISO));
        }
    }

    protected StringBuffer getLogId(final Release release) {
        if(null == release) { return new StringBuffer("null"); }
        else {
            return new StringBuffer()
                .append(release.getId())
                .append(":").append(release.getGroupId())
                .append(":").append(release.getArtifactId())
                .append(":").append(release.getVersion())
                .append(":").append(DateUtil.format(
                        release.getCreatedOn(), DateUtil.DateImage.ISO));
        }
    }

    /**
	 * Obtain a handle to the session model.
	 * 
	 * @return Obtain a handle to the session model.
	 */
	protected SessionModel getSessionModel() { return SessionModel.getModel(); }

    /**
	 * @see ModelL18n#getString(String)
	 * 
	 */
	protected String getString(final String localKey) {
		return l18n.getString(localKey);
	}

    /**
	 * @see ModelL18n#getString(String, Object[])
	 * 
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return l18n.getString(localKey, arguments);
	}

    /**
     * Determine whether or not the artifact is closed.
     * 
     * @param artifact
     *            The artifact.
     * @return True if the artifact is closed; false otherwise.
     */
    protected Boolean isClosed(final Artifact artifact) {
        return ArtifactState.CLOSED == artifact.getState();
    }

    /**
     * Determine whether or not the logged in user is the artifact key holder.
     *
     * @param artifactId
     *      The artifact id.
     * @return True if the user is the keyholder; false otherwise.
     */
    protected Boolean isKeyHolder(final Long artifactId) throws ParityException {
        return getSessionModel().isLoggedInUserKeyHolder(artifactId);
    }

    /**
     * Determine whether or not the user is online.
     *
     * @return True if the user is online; false otherwise.
     */
    protected Boolean isOnline() {
        return getInternalSessionModel().isLoggedIn();
    }

    /**
     * Read the credentials from the configuration.
     * 
     * @return The user's credentials.
     */
    protected Credentials readCredentials() {
        final String cipherKey = "18273-4897-12-53974-816523-49-81623-95-4-91-8723-56974812-63498-612395-498-7125-349871265-47892-1539784-1523954-19-287356-4";
        final String username = getConfigurationHandler().read(ConfigurationKeys.USERNAME);
        final String password = getConfigurationHandler().read(ConfigurationKeys.PASSWORD);

        if(null == username || null == password) { return null; }
        else {
            final Credentials credentials = new Credentials();
            try {
                credentials.setPassword(decrypt(cipherKey, password));
                credentials.setUsername(decrypt(cipherKey, username));
            }
            catch(final BadPaddingException bpx) { throw new RuntimeException("", bpx); }
            catch(final IOException iox) { throw new RuntimeException("", iox); }
            catch(final IllegalBlockSizeException ibsx) { throw new RuntimeException("", ibsx); }
            catch(final InvalidKeyException ikx) { throw new RuntimeException("", ikx); }
            catch(final NoSuchAlgorithmException nsax) { throw new RuntimeException("", nsax); }
            catch(final NoSuchPaddingException nspx) { throw new RuntimeException("", nspx); }

            return credentials;
        }
    }

    /**
     * Decrypt the cipher text into clear text using the cipher key.
     * 
     * @param cipherKey
     *            The cipher key.
     * @param cipherText
     *            The cipher text.
     * @return The clear text.
     * @throws BadPaddingException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private String decrypt(final String cipherKey, final String cipherText)
            throws BadPaddingException, IOException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        final Cipher cipher = getDecryptionCipher();
        return new String(cipher.doFinal(Base64.decodeBytes(cipherText)));
    }

    /**
     * Encrypt clear text into a base 64 encoded cipher text.
     * 
     * @param cipherKey
     *            The cipher key
     * @param clearText
     *            The clean text to encrypt.
     * @return The cipher text.
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private String encrypt(final String cipherKey, final String clearText)
            throws BadPaddingException, IOException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        final Cipher cipher = getEncryptionCipher();
        return Base64.encodeBytes(cipher.doFinal(clearText.getBytes()));
    }

    private String formatAssertion(final ArtifactState currentState,
			final ArtifactState intendedState,
			final ArtifactState[] allowedStates) {
		final StringBuffer assertion =
			new StringBuffer("Cannot move artifact state.  ")
			.append("Current State:  ").append(currentState)
			.append("  Attempted State:  ").append(intendedState)
			.append("  Allowed State(s):  ");
		int index = 0;
		for(final ArtifactState allowedState: allowedStates) {
			if(0 != index++) { assertion.append(","); }
			assertion.append(allowedState.toString());
		}
		return assertion.toString();
	}

    /**
     * Obtain the configuration io interface.
     * 
     * @return The configuraion io interface.
     */
    private ConfigurationIOHandler getConfigurationHandler() {
        if(null == configurationIO) {
            configurationIO = IOFactory.getDefault().createConfigurationHandler();
        }
        return configurationIO;
    }

    /**
     * Obtain the decryption cipher; creating it if necessary.
     * 
     * @return A decryption cipher.
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private Cipher getDecryptionCipher() throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        if(null == decryptionCipher) {
            decryptionCipher = Cipher.getInstance("AES");
            decryptionCipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec());
        }
        return decryptionCipher;
    }

    /**
     * Obtain the encryption cipher; creating it if need be.
     * 
     * @return The encryption cipher.
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private Cipher getEncryptionCipher() throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        if(null == encryptionCipher) {
            encryptionCipher = Cipher.getInstance("AES");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec());
        }
        return encryptionCipher;
    }

    /**
     * Obtain the secret key; creating it if necessary.
     * 
     * @return The secret key.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private SecretKeySpec getSecretKeySpec() throws IOException,
            NoSuchAlgorithmException {
        if(null == secretKeySpec) {
            final byte[] rawKey = MD5Util.md5("010932671-023769081237450981735098127-1280397-181-2387-6581972689-1728-9671-8276-892173-5971283-751-239875-182735-98712-85971-2897-867-9823-56823165-8365-89236-987-214981265-9-9-65623-5896-35-3296-289-65893-983-932-5928734-302894719825-99181-28497612-8375".getBytes());
            secretKeySpec = new SecretKeySpec(rawKey, "AES");
        }
        return secretKeySpec;
    }

    /**
     * Determine whether or not the user's credentials have been set.
     * 
     * @return True if the credentials have been set; false otherwise.
     */
    private Boolean isSetCredentials() { return null != readCredentials(); }

    /** Configuration keys. */
    private static class ConfigurationKeys {
        private static final String PASSWORD = "PASSWORD";
        private static final String USERNAME = "USERNAME";
    }
}