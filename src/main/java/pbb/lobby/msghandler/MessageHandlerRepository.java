

package pbb.lobby.msghandler;

import java.util.HashMap;
import java.util.Map;
import pbb.lobby.common.MessageType;

public class MessageHandlerRepository {
    private final Map<MessageType, MessageHandler> handlers = new HashMap();
    private MessageHandler defaultHandler = (context, message) -> {
    };

    public MessageHandlerRepository() {
    }

    public void assignDefault(MessageHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException();
        } else {
            this.defaultHandler = handler;
        }
    }

    public void register(MessageType type, MessageHandler handler) {
        if (type == null) {
            throw new IllegalArgumentException();
        } else if (handler == null) {
            throw new IllegalArgumentException();
        } else if (this.handlers.containsKey(type)) {
            throw new IllegalArgumentException("Duplicate handler for type: " + type.toString());
        } else {
            this.handlers.put(type, handler);
        }
    }

    public void deregister(MessageType type) {
        this.handlers.remove(type);
    }

    public MessageHandler get(MessageType type) {
        MessageHandler handler = (MessageHandler)this.handlers.get(type);
        return handler == null ? this.defaultHandler : handler;
    }
}
