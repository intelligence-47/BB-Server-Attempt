

package pbb.lobby.common;

import com.ning.compress.lzf.LZFEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import pbb.lobby.outbound.OutboundMessage;

public class SessionMessageEncoder extends MessageToByteEncoder<OutboundMessage> {
    private int writeCounter = 0;

    public SessionMessageEncoder() {
    }

    protected void encode(ChannelHandlerContext context, OutboundMessage in, ByteBuf out) {
        ByteBuf uncompressed = Unpooled.buffer(3 + in.size());
        uncompressed.writeByte(11);
        uncompressed.writeByte(this.writeCounter++);
        uncompressed.writeByte(in.type().id());
        in.serialise(uncompressed);
        byte[] compressed = LZFEncoder.encode(uncompressed.array());
        boolean isCompressed = (compressed[2] & 255) == 1;
        int length;
        if (isCompressed) {
            length = compressed.length - 7;
            this.encodePacketLength(out, (long)length);
            out.writeBytes(compressed, 7, length);
        } else {
            length = compressed.length - 5;
            out.writeByte(length + 1);
            out.writeByte(length - 1);
            out.writeBytes(compressed, 5, length);
        }

    }

    private void encodePacketLength(ByteBuf buffer, long length) {
        int neededBytes = this.calculatePacketLengthNeededBytes(length);
        buffer.ensureWritable(neededBytes);
        switch(neededBytes) {
            case 1:
                buffer.writeByte((byte)((int)length));
                break;
            case 2:
                buffer.writeByte((byte)((int)(length >> 8 | 64L)));
                buffer.writeByte((byte)((int)length));
                break;
            case 3:
            default:
                throw new IllegalStateException();
            case 4:
                buffer.writeByte((byte)((int)(length >> 24 | 128L)));
                buffer.writeByte((byte)((int)(length >> 16)));
                buffer.writeByte((byte)((int)(length >> 8)));
                buffer.writeByte((byte)((int)length));
                break;
            case 5:
                buffer.writeByte(-64);
                buffer.writeByte((byte)((int)(length >> 24)));
                buffer.writeByte((byte)((int)(length >> 16)));
                buffer.writeByte((byte)((int)(length >> 8)));
                buffer.writeByte((byte)((int)length));
        }

    }

    private int calculatePacketLengthNeededBytes(long length) {
        if (length <= 63L) {
            return 1;
        } else if (length <= 16383L) {
            return 2;
        } else if (length <= 1073741823L) {
            return 4;
        } else if (length <= 4294967295L) {
            return 5;
        } else {
            throw new IllegalStateException();
        }
    }
}
