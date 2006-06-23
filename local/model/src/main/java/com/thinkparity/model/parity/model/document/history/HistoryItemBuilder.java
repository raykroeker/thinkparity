/*
 * Created On: Feb 22, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document.history;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18n;

import com.thinkparity.model.LoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * The history item builder is used to create history items based upon the audit
 * events for an artifact.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class HistoryItemBuilder {

	/** An apache logger. */
    protected final Logger logger;

    /** A localization wrapper. */
	private final L18n l18n;

    /**
	 * Create HistoryItemBuilder.
	 * 
     * @param l18n
     *      The parity model localization.
	 */
	public HistoryItemBuilder(final L18n l18n) {
		super();
		this.l18n = l18n;
        this.logger = LoggerFactory.getLogger(getClass());
	}

	/**
     * Build the history of a document for its audit events.
     * 
     * @param iAuditEvents
     *            An iterable of audit events.
     * @return The history.
     * @throws ParityException
     */
	public List<HistoryItem> build(final List<AuditEvent> auditEvents,
            final JabberId loggedInUser) throws ParityException {
		final List<HistoryItem> history = new LinkedList<HistoryItem>();

		final ListIterator<AuditEvent> li = auditEvents.listIterator();
        AuditEvent auditEvent;
        HistoryItem historyItem;
        while(li.hasNext()) {
            auditEvent = li.next();
            historyItem = createCore(auditEvent);
            logger.debug(auditEvent);
            switch(auditEvent.getType()) {
            case ARCHIVE:
                history.add(customize(historyItem, (ArchiveEvent) auditEvent));
                break;
            case CLOSE:
                history.add(customize(historyItem, (CloseEvent) auditEvent));
                break;
            case CREATE:
                history.add(customize(historyItem, (CreateEvent) auditEvent));
                break;
            case CREATE_REMOTE:
                history.add(applyCreateRemoteAttributes(historyItem, (CreateRemoteEvent) auditEvent));
                break;
            case KEY_RESPONSE_DENIED:
                history.add(customize(historyItem, (KeyResponseDeniedEvent) auditEvent));
                break;
            case KEY_REQUEST_DENIED:
                history.add(customize(historyItem, (KeyRequestDeniedEvent) auditEvent));
                break;
            case PUBLISH:
                history.add(customize(historyItem, (PublishEvent) auditEvent));
                break;
            case REACTIVATE:
                history.add(customize(historyItem, (ReactivateEvent) auditEvent));
                break;
            case RECEIVE:
                history.add(customize(historyItem, (ReceiveEvent) auditEvent));
                break;
            case RECEIVE_KEY:
                history.add(customize(historyItem, (ReceiveKeyEvent) auditEvent));
                break;
            case RENAME:
                history.add(customize(historyItem, (RenameEvent) auditEvent));
                break;
            case REQUEST_KEY:
                history.add(customize(historyItem, loggedInUser, (RequestKeyEvent) auditEvent));
                break;
            case SEND:
                final SendEvent sendEvent = (SendEvent) auditEvent;
                final SendConfirmEvent confirmEvent =
                    findConfirmation(auditEvents.listIterator(li.nextIndex()), sendEvent);
                history.add(customize(historyItem, sendEvent, confirmEvent));
                break;
            case SEND_CONFIRM:
                // we do nothing
                break;
            case SEND_KEY:
                history.add(customize(historyItem, (SendKeyEvent) auditEvent));
                break;
            case ADD_TEAM_MEMBER:
                final AddTeamMemberEvent atmEvent = (AddTeamMemberEvent) auditEvent;
                final AddTeamMemberConfirmEvent atmcEvent =
                    findConfirmation(auditEvents.listIterator(li.nextIndex()), atmEvent);
                history.add(customize(historyItem, atmEvent, atmcEvent));
                break;
            case ADD_TEAM_MEMBER_CONFIRM:
                // we do nothing
                break;
            default:
                logger.warn("[LMODEL] [DOCUMENT] [BUILD HISTORY] [UNSUPPORTED AUDIT EVENT {0}]");
            }
		}
		return history;
	}

    private HistoryItem applyCreateRemoteAttributes(final HistoryItem item,
            final CreateRemoteEvent event) {
		item.setEvent(
                getString("eventText.CREATE_REMOTE",
                new Object[] {getName(event.getReceivedFrom())}));
		return item;
	}

    /**
     * Create the core history item. It will apply the following attributes:
     * <ul>
     * <li>Date
     * <li>Document id
     * <li>Id
     * <li>Version id (if the event is a version event)
     * <li>Pending is false
     * </ul>
     * 
     * @param event
     *            The audit event.
     * @return The history item.
     */
    private HistoryItem createCore(final AuditEvent event) {
        final HistoryItem item = new HistoryItem();
        item.setDate(event.getCreatedOn());
        item.setDocumentId(event.getArtifactId());
        item.setId(event.getId());
        if(event instanceof AuditVersionEvent)
            item.setVersionId(((AuditVersionEvent) event).getArtifactVersionId());
        item.setPending(Boolean.FALSE);
        return item;
    }

    /**
     * Create a history item based upon the team member added audit event.
     * 
     * @param event
     *            An add team member event.
     * @param confirmEvent
     *            An add team member confirm event. If null; a pending state
     *            will be applied to the history item.
     * @return A history item.
     */
    private HistoryItem customize(final HistoryItem item,
            final AddTeamMemberEvent event,
            final AddTeamMemberConfirmEvent confirmEvent) {
		final Object[] arguments;
        final String localKey;
        final Boolean pending;
        if(null == confirmEvent) {
            arguments = new Object[] {getName(event.getTeamMember())};
            localKey = "eventText.ADD_TEAM_MEMBER";
            pending = Boolean.TRUE;
        }
        else {
            arguments = new Object[] {
                getName(event.getTeamMember()),
                confirmEvent.getCreatedOn().getTime()
            };
            if(DateUtil.isSameDay(
                    event.getCreatedOn(), confirmEvent.getCreatedOn())) {
                localKey = "eventText.ADD_TEAM_MEMBER_CONFIRMED_SAME_DAY";
            }
            else {
                localKey = "eventText.ADD_TEAM_MEMBER_CONFIRMED";
            }
            pending = Boolean.FALSE;
        }
        item.setEvent(getString(localKey, arguments));
        item.setPending(pending);

        return item;
    }

	private HistoryItem customize(final HistoryItem item, final ArchiveEvent event) {
		final Object[] arguments = new Object[] {
				event.getCreatedOn().getTime()
		};
		item.setEvent(getString("eventText.ARCHIVE", arguments));
		return item;
	}

    private HistoryItem customize(final HistoryItem item,
            final CloseEvent event) {
		final Object[] arguments = new Object[] {
			getName(event.getClosedBy())
		};
		item.setEvent(getString("eventText.CLOSE", arguments));
		return item;
	}

	private HistoryItem customize(final HistoryItem item,
            final CreateEvent event) {
		item.setEvent(getString("eventText.CREATE"));
		return item;
	}

	private HistoryItem customize(final HistoryItem item,
            final JabberId loggedInUser, final RequestKeyEvent event) {
		final Object[] arguments = new Object[] {
		        getName(event.getRequestedBy()),
		        getName(event.getRequestedFrom())
		};
		final String localKey;
		if(event.getRequestedBy().getId().equals(loggedInUser)) {
			localKey = "eventText.REQUEST_KEY_BY";
		}
		else if(event.getRequestedFrom().getId().equals(loggedInUser)) {
			localKey = "eventText.REQUEST_KEY_FROM";			
		}
		else {
			throw Assert.createUnreachable(
					"[LMODEL] [DOCUMENT HISTORY] [CREATE] [EVENT CONTAINS NEITHER TO NOR FROM USER]");
		}
		item.setEvent(getString(localKey, arguments));
		return item;
	}

	private HistoryItem customize(final HistoryItem item,
            final KeyRequestDeniedEvent event) {
		final Object[] arguments = new Object[] {
				getName(event.getDeniedBy())
		};
		item.setEvent(getString("eventText.KEY_REQUEST_DENIED", arguments));
		return item;
	}

	private HistoryItem customize(
            final HistoryItem item, final KeyResponseDeniedEvent event) {
		final Object[] arguments = new Object[] {
				getName(event.getRequestedBy())
		};
		item.setEvent(getString("eventText.KEY_RESPONSE_DENIED", arguments));
		return item;
	}

	private HistoryItem customize(final HistoryItem item,
            final PublishEvent event) {
        item.setEvent(getString("eventText.PUBLISH"));
        return item;
    }

    private HistoryItem customize(final HistoryItem item,
            final ReactivateEvent event) {
        item.setEvent(getString("eventText.REACTIVATE",
                new Object[] {getName(event.getReactivatedBy())}));
        return item;
    }

	private HistoryItem customize(final HistoryItem item,
            final ReceiveEvent event) {
		item.setEvent(getString(
                "eventText.RECEIVE",
                new Object[] {getName(event.getReceivedFrom())}));
		return item;
	}

	private HistoryItem customize(final HistoryItem item,
            final ReceiveKeyEvent event) {
		item.setEvent(getString(
                "eventText.RECEIVE_KEY",
                new Object[] {getName(event.getReceivedFrom())}));
		return item;
	}

    private HistoryItem customize(final HistoryItem item, final RenameEvent event) {
        item.setEvent(getString(
                "eventText.RENAME",
                new Object[] {event.getFrom(), event.getTo()}));
        return item;
    }

	/**
     * Create the history entry for the send audit event.  Note that we do not
     * include the version id because it will be included in the publish
     * event which will preceed all send events.
     *
     * @param event
     *      A send event.
     * @param confirmEvent
     *      A send confirm event.  If null; the history item will be set as
     *      pending.
     * @see HistoryItemBuilder#customize(PublishEvent)
     */
	private HistoryItem customize(final HistoryItem item,
            final SendEvent event, final SendConfirmEvent confirmEvent) {
		final Object[] arguments;
        final String localKey;
        final Boolean pending;
        if(null == confirmEvent) {
            arguments = new Object[] {getName(event.getSentTo())};
            localKey = "eventText.SEND";
            pending = Boolean.TRUE;
        }
        else {
            arguments = new Object[] {
                getName(event.getSentTo()),
                confirmEvent.getCreatedOn().getTime()
            };
            if(DateUtil.isSameDay(
                    event.getCreatedOn(), confirmEvent.getCreatedOn())) {
                localKey = "eventText.SEND_CONFIRMED_SAME_DAY";
            }
            else { localKey = "eventText.SEND_CONFIRMED"; }
            pending = Boolean.FALSE;
        }
        item.setEvent(getString(localKey, arguments));
        item.setPending(pending);
        item.setVersionId(null);        // we do not want to see the version for
                                        // send events
		return item;
	}

	private HistoryItem customize(final HistoryItem item,
            final SendKeyEvent event) {
		final Object[] arguments = new Object[] {getName(event.getSentTo())};
		item.setEvent(getString("eventText.SEND_KEY", arguments));
        item.setVersionId(null);        // we do not want to see the version for
                                        // send key events
		return item;
	}

    /**
     * Find a confirmation event belonging to the add team member event.
     * 
     * @param li
     *      A list iterator for the audit events.
     * @param teamMember
     *      An add team member event.
     * @return The confirmation event corresponding to the add team member
     * event; or null if the event does not exist.
     */
    private AddTeamMemberConfirmEvent findConfirmation(
            final ListIterator<AuditEvent> li,
            final AddTeamMemberEvent addTeamMember) {
        AuditEvent event;
        while(li.hasNext()) {
            event = li.next();
            if(addTeamMember.getArtifactId().equals(event.getArtifactId())) {
                if(AuditEventType.ADD_TEAM_MEMBER_CONFIRM == event.getType()) {
                    if(((AddTeamMemberConfirmEvent) event).getTeamMember().equals(addTeamMember.getTeamMember())) {
                        return (AddTeamMemberConfirmEvent) event;
                    }
                }
            }
        }
        return null;
    }

	/**
     * Find a confirmation event belonging to the send event.
     * 
     * @param li
     *      A list iterator for the audit events.
     * @param send
     *      A send event.
     * @return The confirmation event corresponding to the send event; or null
     *         if the confirmation event does not exist.
     */
    private SendConfirmEvent findConfirmation(
            final ListIterator<AuditEvent> li, final SendEvent send) {
        AuditEvent event;
        while(li.hasNext()) {
            event = li.next();
            if(AuditEventType.SEND_CONFIRM == event.getType()) {
                if(send.getArtifactVersionId().equals(((SendConfirmEvent) event).getArtifactVersionId())) {
                    if(send.getSentTo().equals(((SendConfirmEvent) event).getConfirmedBy())) {
                        return (SendConfirmEvent) event;
                    }
                }
            }
        }
        return null;
    }

	private String getName(final User user) {
		if(user.isSetOrganization()) {
		    final Object[] arguments = new Object[] {
		            user.getName(), user.getOrganization()};
		    return getString("user.name", arguments);
		}
		else {
		    final Object[] arguments = new Object[] {user.getName()};
		    return getString("user.nameMinusOrganization", arguments);
		}
	}

	/**
	 * @see L18n#getString(java.lang.String)
	 * 
	 */
	private String getString(final String localKey) {
		return l18n.getString(localKey);
	}

	/**
	 * @see L18n#getString(java.lang.String, java.lang.Object[])
	 * 
	 */
	private String getString(final String localKey, final Object[] arguments) {
		return l18n.getString(localKey, arguments);
	}
}
