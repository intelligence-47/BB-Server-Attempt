package pbb.lobby.outbound;

import io.netty.buffer.ByteBuf;
import pbb.lobby.common.ByteBufUtils;
import pbb.lobby.common.MessageType;

public class sUserStartMessage implements OutboundMessage {
    private final int uid;
    private final String username;
    private final String nickname;

    public sUserStartMessage(int uid, String username, String nickname) {
        if (username == null) {
            throw new IllegalArgumentException();
        } else if (nickname == null) {
            throw new IllegalArgumentException();
        } else {
            this.uid = uid;
            this.username = username;
            this.nickname = nickname;
        }
    }

    public MessageType type() {
        return MessageType.S_USER_START;
    }

    public int size() {
        return 4 + 2 + this.username.length() + 2 + this.nickname.length() * 2 + 8 + 4;
    }

    public void serialise(ByteBuf buffer) {
        buffer.writeIntLE(this.uid);
        ByteBufUtils.writeCharString(buffer, "[GM]Kerry");
        ByteBufUtils.writeWCharString(buffer, "[GM]Kerry");
        buffer.writeLongLE(0L);
        buffer.writeIntLE(0);
    }
}
