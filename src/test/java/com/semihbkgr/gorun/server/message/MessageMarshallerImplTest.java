package com.semihbkgr.gorun.server.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageMarshallerImplTest {

    MessageMarshallerImpl messageMarshaller = new MessageMarshallerImpl();

    @Test
    @DisplayName("MarshallMessageHasBody")
    void marshallMessageHasBody() {
        var message = Message.of(Action.RUN, "code");
        var marshalledMessage = messageMarshaller.marshall(message);
        assertEquals("[RUN:code]", marshalledMessage);
    }

    @Test
    @DisplayName("MarshallMessageHasNoBody")
    void marshallMessageHasNoBody() {
        var message = Message.of(Action.RUN_ACK);
        var marshalledMessage = messageMarshaller.marshall(message);
        assertEquals("[RUN_ACK]", marshalledMessage);
    }

    @Test
    @DisplayName("UnmarshallMessageHasBody")
    void unmarshallMessageHasBody() {
        var message = "[RUN:code]";
        var unmarshalledMessage = messageMarshaller.unmarshall(message);
        assertEquals(Message.of(Action.RUN, "code"), unmarshalledMessage);
    }

    @Test
    @DisplayName("UnmarshallMessageHasNoBody")
    void unmarshallMessageHasNoBody() {
        var message = "[RUN_ACK]";
        var unmarshalledMessage = messageMarshaller.unmarshall(message);
        assertEquals(Message.of(Action.RUN_ACK), unmarshalledMessage);
    }

}