/*
 * Created On:  25-Dec-06 1:52:37 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Timer;
import java.util.TimerTask;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerDraftListener;

/**
 * <b>Title:</b>thinkParity Document Monitor<br>
 * <b>Description:</b>A document monitor. This class will using the java timer
 * abstraction in order to monitor a file on the file system for changes.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerDraftMonitor {

    /** A count of the number of started monitors. */
    private static int monitorCount;

    static {
        monitorCount = 0;
    }

    /** An <code>InternalContainerModel</code>. */
    private final InternalContainerModel containerModel;

    /** An <code>InternalDocumentModel</code>. */
    private final InternalDocumentModel documentModel;

    /** A <code>ContainerDraft</code> to monitor. */
    private final ContainerDraft draft;

    /** A <code>ContainerEventGenerator</code>. */
    private final ContainerEventGenerator eventGenerator;

    /** A <code>ContainerDraftListener</code>. */
    private final ContainerDraftListener listener;

    /** A <code>Timer</code>. */
    private Timer timer;

    /**
     * Create ContainerDraftMonitor.
     * 
     * @param internalModelFactory
     *            An <code>InternalModelFactory</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param listener
     *            A <code>ContainerDraftListener</code>.
     */
    ContainerDraftMonitor(final InternalModelFactory internalModelFactory,
            final ContainerDraft draft,
            final ContainerEventGenerator eventGenerator,
            final ContainerDraftListener listener) {
        super();
        this.containerModel = internalModelFactory.getContainerModel();
        this.documentModel = internalModelFactory.getDocumentModel();
        this.eventGenerator = eventGenerator;
        this.draft = draft;
        this.listener = listener;
    }

    /**
     * Obtain the container id.
     * 
     * @return A container id <code>Long</code>.
     */
    public Long getContainerId() {
        return draft.getContainerId();
    }

    /**
     * Start the document monitor.
     *
     */
    public void start() {
        timer = new Timer("TPS-OpheliaModel-CDMonitor", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (final Document document : draft.getDocuments()) {
                    final ContainerDraft.ArtifactState state = draft.getState(document);
                    if (ContainerDraft.ArtifactState.NONE == state ||
                        ContainerDraft.ArtifactState.MODIFIED == state ) {
                        final Boolean documentModified =
                            ContainerDraft.ArtifactState.NONE == state ? Boolean.FALSE : Boolean.TRUE;
                        if (documentModel.isDraftModified(document.getId()) != documentModified) {
                            listener.documentModified(
                                    eventGenerator.generate(
                                            containerModel.read(draft.getContainerId()),
                                            containerModel.readDraft(draft.getContainerId()),
                                            documentModel.read(document.getId())));
                        }
                    }
                }
            }
        }, (monitorCount % 100) + 1, 10 * 1000);
        monitorCount++;
    }

    /**
     * Stop the document monitor.
     *
     */
    public void stop() {
        if (null != timer) {
            timer.cancel();
            timer = null;
            monitorCount--;
        }
    }
}
