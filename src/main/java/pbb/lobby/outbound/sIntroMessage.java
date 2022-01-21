

package pbb.lobby.outbound;

import io.netty.buffer.ByteBuf;
import pbb.lobby.common.MessageType;

public class sIntroMessage implements OutboundMessage {
    private final int stage;
    private final int code;

    public sIntroMessage(int stage, int code) {
        this.stage = stage;
        this.code = code;
    }

    public MessageType type() {
        return MessageType.S_INTRO;
    }

    public int size() {
        return 2;
    }

    public void serialise(ByteBuf buffer) {
        buffer.writeByte(this.stage);
        buffer.writeByte(this.code);
    }
}
