

package pbb.lobby.msghandler;

import io.netty.channel.ChannelHandlerContext;
import pbb.lobby.common.SessionInMessage;

public interface MessageHandler {
    void handle(ChannelHandlerContext var1, SessionInMessage var2);
}
