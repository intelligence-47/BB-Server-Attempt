

package pbb.lobby.outbound;

import io.netty.buffer.ByteBuf;
import pbb.lobby.common.MessageType;

public interface OutboundMessage {
    MessageType type();

    int size();

    void serialise(ByteBuf var1);
}
