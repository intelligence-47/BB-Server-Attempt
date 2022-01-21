

package pbb.lobby.outbound;

import io.netty.buffer.ByteBuf;
import pbb.lobby.common.MessageType;

public class sModeMessage implements OutboundMessage {
    private final int mode;

    public sModeMessage(int mode) {
        this.mode = mode;
    }

    public MessageType type() {
        return MessageType.S_MODE;
    }

    public int size() {
        return 1;
    }

    public void serialise(ByteBuf buffer) {
        buffer.writeByte(this.mode);
    }
}
