/*
 * Created On:  21-Nov-07 2:30:11 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.util.InvalidTokenException;

import com.thinkparity.service.AuthToken;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EnqueueTest extends MessageTestCase {

    /** A test name. */
    private static final String NAME = "Enqueue";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create EnqueueTest.
     *
     */
    public EnqueueTest() {
        super(NAME);
    }

    /**
     * Test enqueue message.
     * 
     */
    public void testEnqueue() {
        final Message message = new Message();
        final List<MessageData<Long>> longData = new ArrayList<MessageData<Long>>();
        longData.add(new MessageData<Long>("testLong", Long.valueOf(1000L)));
        message.setLongData(longData);

        final List<MessageData<String>> stringData = new ArrayList<MessageData<String>>();
        stringData.add(new MessageData<String>("testString", "stringValue"));
        message.setStringData(stringData);

        message.setNode(datum.getNode());
        message.setMessage("Test message.");
        message.setTimestamp(datum.now());
        try {
            datum.newMessageModel(datum.authToken).enqueue(
                    MessageBus.getToken(), datum.channel, message);
        } catch (final InvalidTokenException itx) {
            fail(itx, "Could not enqueue message {0}.", message);
        }
    }

    /**
     * Test enqueue message without data.
     * 
     */
    public void testEnqueueNoData() {
        final Message message = new Message();
        message.setLongData(Collections.<MessageData<Long>>emptyList());
        message.setStringData(Collections.<MessageData<String>>emptyList());

        message.setNode(datum.getNode());
        message.setMessage("Test message no data.");
        message.setTimestamp(datum.now());
        try {
            datum.newMessageModel(datum.authToken).enqueue(
                    MessageBus.getToken(), datum.channel, message);
        } catch (final InvalidTokenException itx) {
            fail(itx, "Could not enqueue message {0}.", message);
        }
    }

    /**
     * Test enqueue message without data.
     * 
     */
    public void testEnqueueNullData() {
        final Message message = new Message();
        final List<MessageData<Long>> longData = new ArrayList<MessageData<Long>>();
        longData.add(new MessageData<Long>("testLong", null));
        message.setLongData(longData);

        final List<MessageData<String>> stringData = new ArrayList<MessageData<String>>();
        stringData.add(new MessageData<String>("testString", null));
        message.setStringData(stringData);

        message.setNode(datum.getNode());
        message.setMessage("Test message.");
        message.setTimestamp(datum.now());
        try {
            datum.newMessageModel(datum.authToken).enqueue(
                    MessageBus.getToken(), datum.channel, message);
        } catch (final InvalidTokenException itx) {
            fail(itx, "Could not enqueue message {0}.", message);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.message.MessageTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
        datum.authToken = datum.login(datum.getApplicationUser().getSimpleUsername());
        final InternalMessageModel messageModel = datum.newInternalMessageModel(datum.authToken);
        final String channelName = "/com/thinkparity/testing/messagebus";
        Channel channel = messageModel.readChannel(MessageBus.getToken(), channelName);
        if (null == channel) {
            channel = new Channel();
            channel.setName(channelName);
            messageModel.createChannel(MessageBus.getToken(), channel);
        }
        datum.channel = channel;
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.message.MessageTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** <b>Title:</b>Enqueue Test Fixture<br> */
    private class Fixture extends MessageTestCase.Fixture {
        private AuthToken authToken;
        private Channel channel;
    }
}
