

package pbb.lobby.msghandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pbb.lobby.common.SessionInMessage;
import pbb.lobby.outbound.sModeMessage;

public class SessionHandler extends SimpleChannelInboundHandler<SessionInMessage> {
    private final MessageHandlerRepository handlers = MessageHandlers.repository();

    public SessionHandler() {
    }

    public void handlerAdded(ChannelHandlerContext context) {
        context.writeAndFlush(new sModeMessage(0));
    }

    protected void channelRead0(ChannelHandlerContext context, SessionInMessage message) {
        MessageHandler handler = this.getHandlerForMessage(message);
        handler.handle(context, message);
    }

    private MessageHandler getHandlerForMessage(SessionInMessage message) {
        return this.handlers.get(message.type());
    }

    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }
}
