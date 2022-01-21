

package pbb.lobby.outbound;

import io.netty.buffer.ByteBuf;
import pbb.lobby.common.MessageType;

public class sNoTimeoutMessage implements OutboundMessage {
    public sNoTimeoutMessage() {
    }

    public MessageType type() {
        return MessageType.START;
    }

    public int size() {
        return 1;
    }

    public void serialise(ByteBuf buffer) {
        buffer.writeByte(0);
    }
}
