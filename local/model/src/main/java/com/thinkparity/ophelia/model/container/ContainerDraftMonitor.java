/*
 * Created On:  25-Dec-06 1:52:37 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Timer;
import java.util.TimerTask;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerDraftListener;

/**
 * <b>Title:</b>thinkParity Document Monitor<br>
 * <b>Description:</b>A document monitor. This class will using the java timer
 * abstraction in order to monitor a file on the file system for in document
 * state within the draft.<br>
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
    private ContainerDraft draft;

    /** A <code>ContainerEventGenerator</code>. */
    private final ContainerEventGenerator eventGenerator;

    /** A <code>ContainerDraftListener</code>. */
    private final ContainerDraftListener listener;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

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
        this.logger = new Log4JWrapper(getClass());
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
                    final ArtifactState state = draft.getState(document);
                    logger.logInfo("Monitoring {0} document {1}.",
                            state.name().toLowerCase(), document.getName());
                    switch (state) {
                    case ADDED:     // cannot go anywhere from added
                        break;
                    case NONE:
                        if (documentModel.isDraftModified(document.getId())) {
                            notifyStateChanged(document);
                        }
                        break;
                    case MODIFIED:
                        if (!documentModel.isDraftModified(document.getId())) {
                            notifyStateChanged(document);
                        }                        
                        break;
                    case REMOVED:   // can only go to non via revert
                        break;
                    default:
                        Assert.assertUnreachable(
                                "Illegal state transition from {0} to modified for document {1}.",
                                state.name().toLowerCase(), document.getName());
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
    /**
     * Fire a state changed event for a document.
     * 
     * @param document
     *            A <code>Document</code>.
     */
    private void notifyStateChanged(final Document document) {
        final ContainerDraft currentDraft = containerModel.readDraft(draft.getContainerId());
        if (null == currentDraft) {
            logger.logInfo("Draft for {0} was deleted.", document.getName());
        } else {
            draft = currentDraft;
            listener.stateChanged(eventGenerator.generate(
                    containerModel.read(currentDraft.getContainerId()),
                    currentDraft, documentModel.read(document.getId())));
        }
    }
}
