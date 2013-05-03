/*
 * Created On: Jul 6, 2006 3:47:44 PM
 */
package com.thinkparity.ophelia.model.audit;

import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.event.*;

/**
 * <b>Title:</b>thinkParity Abstract History Item Builder<br>
 * <b>Description:</b>An abstraction of a historyi item builder used to create
 * an artifact's history.
 * 
 * @author raymond@thinkparity.com
 * @version
 */
public abstract class HistoryItemBuilder<T extends HistoryItem> {

    /** The history item localization. */
    protected final L18n l18n;

    /** An apache logger. */
    protected final Logger logger;

    /**
     * Create DocumentHistoryBuilder.
     * 
     * @param l18n
     *            The history localization.
     */
    protected HistoryItemBuilder(final L18n l18n) {
        super();
        this.l18n = l18n;
        this.logger = Logger.getLogger(getClass());
    }
    
    /**
     * Create a history item.
     * 
     * @param event
     *            An audit event.
     * @return A history item.
     */
    protected abstract T createItem(final AuditEvent event);

    /**
     * Create a history item from an audit event.
     * 
     * @param iterator
     *            The audit event list iterator.
     * @param event
     *            An audit event.
     * @return A history item.
     */
    protected T createItem(final ListIterator<AuditEvent> iterator,
            final AuditEvent event) {
        final T item = createItem(event);
        switch(event.getType()) {
        case ARCHIVE:
            customize(item, (ArchiveEvent) event);
            break;
        case CLOSE:
            customize(item, (CloseEvent) event);
            break;
        case CREATE:
            customize(item, (CreateEvent) event);
            break;
        case CREATE_REMOTE:
            customize(item, (CreateRemoteEvent) event);
            break;
        case KEY_RESPONSE_DENIED:
            customize(item, (KeyResponseDeniedEvent) event);
            break;
        case KEY_REQUEST_DENIED:
            customize(item, (KeyRequestDeniedEvent) event);
            break;
        case PUBLISH:
            customize(item, (PublishEvent) event);
            break;
        case REACTIVATE:
            customize(item, (ReactivateEvent) event);
            break;
        case RECEIVE:
            customize(item, (ReceiveEvent) event);
            break;
        case RECEIVE_KEY:
            customize(item, (ReceiveKeyEvent) event);
            break;
        case RENAME:
            customize(item, (RenameEvent) event);
            break;
        case REQUEST_KEY:
            customize(item, (RequestKeyEvent) event, null);
            break;
        case SEND:
            final SendEvent sendEvent = (SendEvent) event;
            final SendConfirmEvent confirmEvent = findConfirmation(iterator, sendEvent);
            customize(item, sendEvent, confirmEvent);
            break;
        case SEND_KEY:
            customize(item, (SendKeyEvent) event);
            break;
        case ADD_TEAM_MEMBER:
            final AddTeamMemberEvent atmEvent = (AddTeamMemberEvent) event;
            final AddTeamMemberConfirmEvent atmcEvent = findConfirmation(iterator, atmEvent);
            customize(item, atmEvent, atmcEvent);
            break;
        case ADD_TEAM_MEMBER_CONFIRM:
        case SEND_CONFIRM:
            // we do nothing
            break;
        default:
            throw Assert.createUnreachable("");
        }
        return item;
    }

    /**
     * Customize a history item based upon an add team member event; and its
     * (optional) confirmation.
     * 
     * @param event
     *            An add team member event.
     * @param confirmEvent
     *            An add team member confirm event. If null; a pending state
     *            will be applied to the history item.
     * @return A history item.
     */
    protected HistoryItem customize(final HistoryItem item, final AddTeamMemberEvent event, final AddTeamMemberConfirmEvent confirmEvent) {
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

    /**
     * Customize a history item for an archive audit event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            An archive audit event.
     * @return The customized history item.
     */
    protected HistoryItem customize(final HistoryItem item, final ArchiveEvent event) {
    	final Object[] arguments = new Object[] {
    			event.getCreatedOn().getTime()
    	};
    	item.setEvent(getString("eventText.ARCHIVE", arguments));
    	return item;
    }

    /**
     * Create the core history item. It will apply the following attributes:
     * 
     * @param event
     *            The audit event.
     * @return The history item.
     */
    protected HistoryItem customize(final HistoryItem item,
            final AuditEvent event) {
        item.setDate(event.getCreatedOn());
        item.setId(event.getId());
        item.setPending(Boolean.FALSE);
        return item;
    }

    /**
     * Customize a history item for a close audit event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A close audit event.
     * @return The custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final CloseEvent event) {
    	final Object[] arguments = new Object[] {
    		getName(event.getClosedBy())
    	};
    	item.setEvent(getString("eventText.CLOSE", arguments));
    	return item;
    }

    /**
     * Customize a history item for a creat audit event.
     * 
     * @param item
     *            The history item.
     * @param event
     *            The create audit event.
     * @return The custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final CreateEvent event) {
    	item.setEvent(getString("eventText.CREATE"));
    	return item;
    }

    /**
     * Customize a history item for a create remote audit event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A create remote audit event.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item,
            final CreateRemoteEvent event) {
        final String localKey = "eventText.CREATE_REMOTE";
        final Object[] arguments = new Object[] {getName(event.getReceivedFrom())};
    	item.setEvent(getString(localKey, arguments));
    	return item;
    }

    /**
     * Customize a history item for a key request denied audit event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A key request denied audit event.
     * @return The custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final KeyRequestDeniedEvent event) {
    	final Object[] arguments = new Object[] {
    			getName(event.getDeniedBy())
    	};
    	item.setEvent(getString("eventText.KEY_REQUEST_DENIED", arguments));
    	return item;
    }

    /**
     * Customize a history item for a key response denied event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A key response denied event.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final KeyResponseDeniedEvent event) {
    	final Object[] arguments = new Object[] {
    			getName(event.getRequestedBy())
    	};
    	item.setEvent(getString("eventText.KEY_RESPONSE_DENIED", arguments));
    	return item;
    }

    /**
     * Customize a history item for a publishe event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A publish event.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final PublishEvent event) {
        item.setEvent(getString("eventText.PUBLISH"));
        return item;
    }

    /**
     * Customize a history item for a reactivate event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A reactivate event.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final ReactivateEvent event) {
        item.setEvent(getString("eventText.REACTIVATE",
                new Object[] {getName(event.getReactivatedBy())}));
        return item;
    }

    /**
     * Customize a history item for a receive key event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A receive key event.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final ReceiveKeyEvent event) {
    	item.setEvent(getString(
                "eventText.RECEIVE_KEY",
                new Object[] {getName(event.getReceivedFrom())}));
    	return item;
    }

    /**
     * Customize a history item for a rename event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A rename event.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final RenameEvent event) {
        item.setEvent(getString(
                "eventText.RENAME",
                new Object[] {event.getFrom(), event.getTo()}));
        return item;
    }

    /**
     * Customize a history item for a request key audit event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A request key audit event.
     * @param localUser
     *            The local user.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item,
            final RequestKeyEvent event, final JabberId localUser) {
    	final Object[] arguments = new Object[] {
    	        getName(event.getRequestedBy()),
    	        getName(event.getRequestedFrom())
    	};
    	final String localKey;
    	if (event.getRequestedBy().getId().equals(localUser)) {
    		localKey = "eventText.REQUEST_KEY_BY";
    	} else if (event.getRequestedFrom().getId().equals(localUser)) {
    		localKey = "eventText.REQUEST_KEY_FROM";			
    	} else {
    		throw Assert.createUnreachable(
    				"[HISTORY ITEM BUILDER] [CUSTOMIZE] [REQUEST KEY EVENT CONTAINS NEITHER TO NOR FROM USER]");
    	}
    	item.setEvent(getString(localKey, arguments));
    	return item;
    }

    /**
     * Customize a history item for the send audit event. Note that we do not
     * include the version id because it will be included in the publish event
     * which will preceed all send events.
     * 
     * @param event
     *            A send event.
     * @param confirmEvent
     *            A send confirm event. If null; the history item will be set as
     *            pending.
     * @see DocumentHistoryBuilder#customize(PublishEvent)
     */
    protected HistoryItem customize(final HistoryItem item,
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
            if (DateUtil.isSameDay(event.getCreatedOn(),
                    confirmEvent.getCreatedOn())) {
                localKey = "eventText.SEND_CONFIRMED_SAME_DAY";
            } else {
                localKey = "eventText.SEND_CONFIRMED";
            }
            pending = Boolean.FALSE;
        }
        item.setEvent(getString(localKey, arguments));
        item.setPending(pending);
    	return item;
    }

    /**
     * Customize a history item for a send key event.
     * 
     * @param item
     *            A history item.
     * @param event
     *            A send key event.
     * @return A custom history item.
     */
    protected HistoryItem customize(final HistoryItem item, final SendKeyEvent event) {
    	final Object[] arguments = new Object[] {getName(event.getSentTo())};
    	item.setEvent(getString("eventText.SEND_KEY", arguments));
    	return item;
    }

    /**
     * Find a confirmation event belonging to the add team member event.
     * 
     * @param iterator
     *            A list iterator of audit events.
     * @param event
     *            An add team member audit event.
     * @return A confirmation audit event corresponding to the add team member
     *         event; or null if the confirmation does not exist.
     */
    protected AddTeamMemberConfirmEvent findConfirmation(
            final ListIterator<AuditEvent> iterator,
            final AddTeamMemberEvent event) {
        AuditEvent nextEvent;
        while(iterator.hasNext()) {
            nextEvent = iterator.next();
            if(event.getArtifactId().equals(nextEvent.getArtifactId())) {
                if(AuditEventType.ADD_TEAM_MEMBER_CONFIRM == nextEvent.getType()) {
                    if(((AddTeamMemberConfirmEvent) nextEvent).getTeamMember().equals(event.getTeamMember())) {
                        return (AddTeamMemberConfirmEvent) nextEvent;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find a confirmation event belonging to the send event.
     * 
     * @param iterator
     *            A list iterator of audit events.
     * @param event
     *            A send audit event.
     * @return A confirmation event corresponding to the send event; or null if
     *         the confirmation does not exist.
     */
    protected SendConfirmEvent findConfirmation(
            final ListIterator<AuditEvent> iterator, final SendEvent event) {
        AuditEvent nextEvent;
        while(iterator.hasNext()) {
            nextEvent = iterator.next();
            if(AuditEventType.SEND_CONFIRM == nextEvent.getType()) {
                if(event.getArtifactVersionId().equals(((SendConfirmEvent) nextEvent).getArtifactVersionId())) {
                    if(event.getSentTo().equals(((SendConfirmEvent) nextEvent).getConfirmedBy())) {
                        return (SendConfirmEvent) nextEvent;
                    }
                }
            }
        }
        return null;
    }

    protected String getName(final User user) {
        final Object[] arguments = new Object[] { user.getName(), user.getOrganization()};
        return getString("user.name", arguments);
    }

    /**
     * Obtain a localized string.
     * 
     * @param localKey
     *            The local key.
     * @return The localized string.
     * @see L18n#getString(String)
     */
    protected String getString(final String localKey) {
        return l18n.getString(localKey);
    }

    /**
     * Obtain a formatted localized string.
     * 
     * @param localKey
     *            The local key.
     * @param arguments
     *            The formatting arguments.
     * @return The localized string.
     * @see L18n#getString(String)
     */
    protected String getString(final String localKey, final Object[] arguments) {
    	return l18n.getString(localKey, arguments);
    }
}
