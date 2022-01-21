

package pbb.lobby.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.StandardCharsets;
import pbb.lobby.common.ByteBufUtils;
import pbb.lobby.common.MessageType;
import pbb.lobby.common.SessionMessageDecoder;
import pbb.lobby.common.SessionMessageEncoder;
import pbb.lobby.msghandler.SessionHandler;

public class LobbyHandshakeHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private LobbyHandshakeHandler.State state;

    public LobbyHandshakeHandler() {
        this.state = LobbyHandshakeHandler.State.START;
    }

    public void channelRegistered(ChannelHandlerContext context) {
        ByteBuf buffer = context.alloc().buffer(16);
        buffer.writeBytes("PlanB_Lobby\u0000".getBytes(StandardCharsets.UTF_8));
        buffer.writeInt(1145324612);
        context.writeAndFlush(buffer);
        this.transition(LobbyHandshakeHandler.State.AWAITING_CLIENT_INFO);
    }

    protected void channelRead0(ChannelHandlerContext context, ByteBuf in) {
        if (in.isReadable()) {
            switch(this.state) {
                case AWAITING_CLIENT_INFO:
                    in.readUnsignedByte();
                    in.readUnsignedShortLE();
                    in.readUnsignedShortLE();
                    in.readUnsignedIntLE();
                    ByteBufUtils.readCharString(in);
                    in.readUnsignedShortLE();
                    ByteBufUtils.readCharString(in);
                    ByteBufUtils.readCharString(in);
                    this.sendSessionInfo(context);
                    this.transition(LobbyHandshakeHandler.State.AWAITING_CLIENT_READY);
                    break;
                case AWAITING_CLIENT_READY:
                    in.readUnsignedByte();
                    this.sendStartSession(context);
                    context.pipeline().remove(this);
                    context.pipeline().addLast("encoder", new SessionMessageEncoder());
                    context.pipeline().addLast("decoder", new SessionMessageDecoder());
                    context.pipeline().addLast("handler", new SessionHandler());
                    this.transition(LobbyHandshakeHandler.State.DONE);
                    break;
                default:
                    throw new IllegalStateException(this.state.toString());
            }

        }
    }

    private void sendSessionInfo(ChannelHandlerContext context) {
        ByteBuf buffer = context.alloc().buffer();
        buffer.writeByte(MessageType.S_SESSION_INFO.id());
        buffer.writeLongLE(2L);
        String stringA = "TCP default write stream";
        buffer.writeShortLE(stringA.length());
        buffer.writeBytes(stringA.getBytes(StandardCharsets.UTF_8));
        String stringB = "TCP default read stream";
        buffer.writeShortLE(stringB.length());
        buffer.writeBytes(stringB.getBytes(StandardCharsets.UTF_8));
        context.writeAndFlush(buffer);
    }

    private void sendStartSession(ChannelHandlerContext context) {
        ByteBuf buffer = context.alloc().buffer(1);
        buffer.writeByte(MessageType.S_START_SESSION.id());
        context.writeAndFlush(buffer);
    }

    private void transition(LobbyHandshakeHandler.State state) {
        if (state == null) {
            throw new IllegalArgumentException();
        } else {
            this.state = state;
        }
    }

    private static enum State {
        START,
        AWAITING_CLIENT_INFO,
        AWAITING_CLIENT_READY,
        DONE;

        private State() {
        }
    }
}
