

package pbb.lobby.msghandler;

import io.netty.channel.ChannelHandlerContext;
import pbb.lobby.common.ByteBufUtils;
import pbb.lobby.common.MessageType;
import pbb.lobby.common.SessionInMessage;
import pbb.lobby.outbound.sIntroMessage;
import pbb.lobby.outbound.sModeMessage;
import pbb.lobby.outbound.sNoTimeoutMessage;
import pbb.lobby.outbound.sUserInfoMessage;
import pbb.lobby.outbound.sUserStartMessage;

public class MessageHandlers {
    private static String nickname;

    public MessageHandlers() {
    }

    public static MessageHandlerRepository repository() {
        MessageHandlerRepository repository = new MessageHandlerRepository();
        repository.register(MessageType.START, MessageHandlers::handleNoTimeout);
        repository.register(MessageType.C_INTRO, MessageHandlers::handleIntro);
        repository.register(MessageType.C_MODE, MessageHandlers::handleMode);
        return repository;
    }

    private static void handleNoTimeout(ChannelHandlerContext context, SessionInMessage message) {
        context.writeAndFlush(new sNoTimeoutMessage());
    }

    private static void handleIntro(ChannelHandlerContext context, SessionInMessage message) {
        System.out.println(message.toString());
        int subtype = message.buffer().readUnsignedByte();
        if (subtype == 2) {
            nickname = ByteBufUtils.readWCharString(message.buffer());
            context.writeAndFlush(new sIntroMessage(0, 1));
        } else if (subtype == 3) {
            nickname = ByteBufUtils.readWCharString(message.buffer());
            context.writeAndFlush(new sIntroMessage(1, 1));
        } else if (subtype == 4) {
            int classId = message.buffer().readUnsignedByte();
            System.out.println("class id:"+classId);
            message.buffer().readShortLE();
            message.buffer().readShortLE();
            message.buffer().readShortLE();
            message.buffer().readShortLE();
            message.buffer().readShortLE();
            context.writeAndFlush(new sUserStartMessage(1, "asdf", nickname));
            context.writeAndFlush(new sUserInfoMessage(nickname, classId));
            context.writeAndFlush(new sModeMessage(2));
        }

    }

    private static void handleMode(ChannelHandlerContext context, SessionInMessage message) {
        int mode = message.buffer().readUnsignedByte();
        if (mode == 24) {
            context.writeAndFlush(new sModeMessage(3));
        } else if (mode == 25) {
            context.writeAndFlush(new sModeMessage(4));
        } else if (mode == 26) {
            context.writeAndFlush(new sModeMessage(6));
        } else if (mode == 27) {
            context.writeAndFlush(new sModeMessage(7));
        } else if (mode == 28) {
            context.writeAndFlush(new sModeMessage(8));
        } else if (mode == 29) {
            context.writeAndFlush(new sModeMessage(10));
        } else if (mode == 30) {
            context.writeAndFlush(new sModeMessage(11));
        } else if (mode == 31) {
            context.writeAndFlush(new sModeMessage(20));
        } else {
            context.writeAndFlush(new sModeMessage(2));
        }

    }
}
