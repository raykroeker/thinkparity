/*
 * Created On:  21-Jul-07 2:14:47 PM
 */
package com.thinkparity.ophelia.model.document.delegate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.crypto.CryptoMonitor;
import com.thinkparity.codebase.crypto.EncryptStream;
import com.thinkparity.codebase.io.StreamOpener;

import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.upload.UploadFile;

import com.thinkparity.ophelia.model.container.monitor.PublishData;
import com.thinkparity.ophelia.model.container.monitor.PublishStep;
import com.thinkparity.ophelia.model.crypto.InternalCryptoModel;
import com.thinkparity.ophelia.model.document.DocumentDelegate;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Ophelia Model Document Upload Stream Delegate<br>
 * <b>Description:</b>An implementation of the upload stream api<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UploadStream extends DocumentDelegate {

    /** The process monitor. */
    private ProcessMonitor monitor;

    /** The document name. */
    private String name;

    /** The document version. */
    private DocumentVersion version;

    /**
     * Create UploadStream.
     *
     */
    public UploadStream() {
        super();
    }

    /**
     * Set the stream monitor.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void setMonitor(final ProcessMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Set the version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     */
    public void setVersion(final DocumentVersion version) {
        this.name = version.getArtifactName();
        this.version = version;
    }

    /**
     * Upload a document version to the streaming server.
     * 
     * @throws IOException
     */
    public void uploadStream() throws IOException {
        final File encrypted = createTempFile(name);
        try {
            encrypt(resolveSecret(), encrypted);
            upload(encrypted);
        } finally {
            // TEMPFILE - UploadStream#uploadStream()
            encrypted.delete();
        }
    }

    /**
     * Encrypt the document version content.
     * 
     * @param secret
     *            A <code>Secret</code>.
     * @param target
     *            A <code>File</code>.
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private void encrypt(final Secret secret, final File target)
            throws IOException {
        final Key key = new SecretKeySpec(secret.getKey(), secret.getAlgorithm());
        documentIO.openStream(version.getArtifactId(), version.getVersionId(), new StreamOpener() {
            public void open(final InputStream stream) throws IOException {
                try {
                    final EncryptStream encryptStream = new EncryptStream(
                            newCryptoMonitor(), secret.getAlgorithm());
                    synchronized (getBufferLock()) {
                            encryptStream.encrypt(key, stream, target,
                                    getBufferArray());
                    }
                } catch (final GeneralSecurityException gsx) {
                    throw new IOException(gsx);
                }
            }
        });
    }

    /**
     * Create a crypto monitor. The monitor is responsible for translating
     * encryption progress events into a publish monitor's progress events.
     * 
     * @return A <code>CryptoMonitor</code>.
     */
    private CryptoMonitor newCryptoMonitor() {
        return new CryptoMonitor() {
            /** A publish monitor data. */
            private final PublishData monitorData = new PublishData();
            /**
             * @see com.thinkparity.codebase.crypto.CryptoMonitor#chunkDecrypted(int)
             *
             */
            @Override
            public void chunkDecrypted(final int chunkSize) {
                throw Assert.createUnreachable("Cannot decrypt from here.");
            }
            /**
             * @see com.thinkparity.codebase.crypto.CryptoMonitor#chunkEncrypted(int)
             *
             */
            @Override
            public void chunkEncrypted(final int chunkSize) {
                monitorData.setDocumentVersion(version);
                monitorData.setBytes(chunkSize);
                notifyStepBegin(monitor, PublishStep.ENCRYPT_DOCUMENT_VERSION_BYTES, monitorData);
                notifyStepEnd(monitor, PublishStep.ENCRYPT_DOCUMENT_VERSION_BYTES);
            }
        };
    }

    /**
     * Create an upload stream monitor. The stream events are transitioned to
     * events on the process monitor.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param streamName
     *            A stream name <code>String</code>.
     * @return A <code>StreamMonitor</code>.
     */
    private StreamMonitor newUploadStreamMonitor(final ProcessMonitor monitor,
            final String streamName) {
        return new StreamMonitor() {
            /** A publish monitor data. */
            private final PublishData monitorData = new PublishData();
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkReceived(int)
             * 
             */
            @Override
            public void chunkReceived(final int chunkSize) {
                // not possible for upload
                Assert.assertUnreachable("");
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#chunkSent(int)
             *
             */
            @Override
            public void chunkSent(final int chunkSize) {
                monitorData.setDocumentVersion(version);
                monitorData.setBytes(chunkSize);
                notifyStepBegin(monitor, PublishStep.UPLOAD_DOCUMENT_VERSION_BYTES, monitorData);
                notifyStepEnd(monitor, PublishStep.UPLOAD_DOCUMENT_VERSION_BYTES);
            }
            /**
             * @see com.thinkparity.codebase.model.stream.StreamMonitor#getName()
             *
             */
            @Override
            public String getName() {
                return MessageFormat.format(
                        "UploadStream#newUploadStreamMonitor({0})", streamName);
            }
        };
    }

    /**
     * Create a new stream session for a version and file.
     * 
     * @param file
     *            A <code>File</code>.
     * @return A <code>StreamSession</code>.
     */
    private StreamSession newUpstreamSession(final File file) throws IOException {
        final StreamInfo streamInfo = new StreamInfo();
        streamInfo.setMD5(checksum(file));
        streamInfo.setSize(Long.valueOf(file.length()));
        return getStreamModel().newUpstreamSession(streamInfo, version);
    }

    /**
     * If the secret for the document version already exists; download it;
     * otherwise create a new secret.
     * 
     * @return A <code>Secret</code>.
     */
    private Secret resolveSecret() {
        final InternalCryptoModel cryptoModel = getCryptoModel();
        if (cryptoModel.doesExistSecret(version)) {
            return cryptoModel.readSecret(version);
        } else {
            return cryptoModel.createSecret(version);
        }
    }

    /**
     * Upload a file.
     * 
     * @param file
     *            A <code>File</code>.
     */
    private void upload(final File file) throws IOException {
        new UploadFile(newUploadStreamMonitor(monitor, file.getName()),
                newUpstreamSession(file)).upload(file);
    }
}
