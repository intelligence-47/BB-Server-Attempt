

package pbb.lobby.outbound;

import io.netty.buffer.ByteBuf;
import pbb.lobby.common.ByteBufUtils;
import pbb.lobby.common.IPAddress;
import pbb.lobby.common.MessageType;

public class sUserInfoMessage implements OutboundMessage {
    private final String nickname;
    private final int classId;

    public sUserInfoMessage(String nickname, int classId) {
        this.nickname = nickname;
        this.classId = classId;
    }

    public MessageType type() {
        return MessageType.S_USER_INFO;
    }

    public int size() {
        return 1024;
    }

    public void serialise(ByteBuf buffer) {
        this.serialiseSubType0(buffer);
    }

    private void serialiseSubType0(ByteBuf buffer) {
        buffer.writeByte(0);
        ByteBufUtils.writeWCharString(buffer, "[GM]Admin_4");
        buffer.writeLongLE(1L);
        buffer.writeByte(this.classId);
        buffer.writeBytes(new byte[310]);
        buffer.writeShortLE(1);
        buffer.writeByte(1);
        buffer.writeBytes(new byte[12]);
        buffer.writeByte(4);
        buffer.writeByte(1);
        buffer.writeByte(100);
        buffer.writeIntLE(1);
        ByteBufUtils.writeWCharString(buffer, "HelloB");
        buffer.writeShortLE(1);
        buffer.writeBoolean(true);
        //ByteBufUtils.writeIPAddress(buffer, new IPAddress(2130706433, 6543));
        //ByteBufUtils.writeIPAddress(buffer, new IPAddress(2130706433, 5432));
    }
}
