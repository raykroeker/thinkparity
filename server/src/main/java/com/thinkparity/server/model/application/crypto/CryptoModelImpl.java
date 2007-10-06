/*
 * Created On:  25-Jul-07 9:35:21 AM
 */
package com.thinkparity.desdemona.model.crypto;

import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.artifact.RemoteArtifact;
import com.thinkparity.desdemona.model.artifact.RemoteArtifactVersion;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;

/**
 * <b>Title:</b>thinkParity Desdemona Encryption Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CryptoModelImpl extends AbstractModelImpl implements
        CryptoModel, InternalCryptoModel {

    /**
     * Create CryptoModelImpl.
     *
     */
    public CryptoModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.crypto.CryptoModel#createSecret(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Secret createSecret(final DocumentVersion version) {
        try {
            final Container container = localizeBackupContainer(version);
            if (null == container) {
                /* this is the case prior to creating the first version of a
                 * container */
                final SecretKey secretKey = newSecretKey();

                final Secret secret = new Secret();
                secret.setAlgorithm(secretKey.getAlgorithm());
                secret.setFormat(secretKey.getFormat());
                secret.setKey(secretKey.getEncoded());
                getArtifactModel().createSecret(
                        resolveArtifactVersion(version), secret);
                return secret;
            } else {
                if (getArtifactModel().isDraftOwner(localizeArtifact(container))) {
                    /* this is the case after creating a draft for subsequent
                     * versions */
                    final SecretKey secretKey = newSecretKey();

                    final Secret secret = new Secret();
                    secret.setAlgorithm(secretKey.getAlgorithm());
                    secret.setFormat(secretKey.getFormat());
                    secret.setKey(secretKey.getEncoded());
                    getArtifactModel().createSecret(
                            resolveArtifactVersion(version), secret);
                    return secret;
                } else {
                    final String message = MessageFormat.format(
                            "User {0} is not the draft owner for package {0} for document {1}.",
                            user.getUsername(), version.getArtifactUniqueId(),
                            container.getUniqueId());
                    throw new IllegalArgumentException(message);
                }
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.crypto.CryptoModel#doesExistSecret(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Boolean doesExistSecret(final DocumentVersion version) {
        try {
            final InternalArtifactModel artifactModel = getArtifactModel();
            if (artifactModel.doesExist(version.getArtifactUniqueId())) {
                final Artifact artifact = localizeArtifact(version);
                if (artifactModel.doesExistVersion(artifact, version.getVersionId())) {
                    return artifactModel.doesExistSecret(
                            localizeArtifactVersion(artifact, version));
                }
            }
            return Boolean.FALSE;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.crypto.CryptoModel#readSecret(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Secret readSecret(final DocumentVersion version) {
        try {
            return getArtifactModel().readSecret(localizeArtifactVersion(version));
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Localize an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return A <code>Artifact</code>.
     */
    private Artifact localizeArtifact(final Artifact artifact) {
        return getArtifactModel().read(artifact.getUniqueId());
    }

    /**
     * Localize an artifact for a version.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @return A <code>Artifact</code>.
     */
    private Artifact localizeArtifact(final ArtifactVersion version) {
        return getArtifactModel().read(version.getArtifactUniqueId());
    }

    /**
     * Lookup the artifact version.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @return An <code>ArtifactVersion</code>.
     */
    private ArtifactVersion localizeArtifactVersion(final Artifact artifact,
            final ArtifactVersion version) {
        return getArtifactModel().readVersion(artifact,
                version.getVersionId());
    }

    /**
     * Lookup the artifact version.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @return An <code>ArtifactVersion</code>.
     */
    private ArtifactVersion localizeArtifactVersion(
            final ArtifactVersion version) {
        return localizeArtifactVersion(localizeArtifact(version), version);
    }

    /**
     * Localize a document version's container from the backup.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>Container</code>.
     */
    private Container localizeBackupContainer(final DocumentVersion version) {
        final InternalBackupModel backupModel = getBackupModel();
        final Document backupDocument = backupModel.readDocumentAuth(
                version.getArtifactUniqueId());
        if (null == backupDocument) {
            return null;
        } else {
            final List<Container> backupContainers =
                backupModel.readContainersForDocumentAuth(backupDocument);
            switch (backupContainers.size()) {
            case 0:
                return null;
            case 1:
                return backupContainers.get(0);
            default:
                throw Assert.createUnreachable(
                        "Unexpected list size.  Was {0} expected [0-1].",
                        backupContainers.size());
            }
        }
    }

    /**
     * Create a new secret key.
     * 
     * @return A <code>SecretKey</code>.
     */
    private SecretKey newSecretKey() throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator =
            KeyGenerator.getInstance(Constants.Security.Encryption.ALGORITHM);
        keyGenerator.init(Constants.Security.Encryption.KEY_SIZE);
        return keyGenerator.generateKey();
    }

    /**
     * Resolve a document version. If either the artifact or the artifact
     * version do not exist; they are created.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return An <code>ArtifactVersion</code>.
     */
    private ArtifactVersion resolveArtifactVersion(final DocumentVersion version) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        if (artifactModel.doesExist(version.getArtifactUniqueId())) {
            final Artifact artifact = localizeArtifact(version);
            if (!artifactModel.doesExistVersion(artifact, version.getVersionId())) {
                final ArtifactVersion localVersion = new RemoteArtifactVersion();
                localVersion.setArtifactId(artifact.getId());
                localVersion.setArtifactType(version.getArtifactType());
                localVersion.setArtifactUniqueId(version.getArtifactUniqueId());
                localVersion.setVersionId(version.getVersionId());
                artifactModel.createVersion(localVersion);
            }
            return localizeArtifactVersion(version);
        } else {
            final ArtifactVersion localVersion = new RemoteArtifactVersion();
            localVersion.setArtifactType(version.getArtifactType());
            localVersion.setArtifactUniqueId(version.getArtifactUniqueId());
            localVersion.setVersionId(version.getVersionId());

            final Artifact localArtifact = new RemoteArtifact();
            localArtifact.setCreatedOn(version.getCreatedOn());
            localArtifact.setType(version.getArtifactType());
            localArtifact.setUniqueId(version.getArtifactUniqueId());

            artifactModel.create(localArtifact, localVersion);
            localVersion.setArtifactId(localArtifact.getId());
            artifactModel.createVersion(localVersion);
            artifactModel.deleteDraft(localArtifact,
                    localArtifact.getCreatedOn(), Boolean.FALSE);
            return localizeArtifactVersion(version);
        }
    }
}
