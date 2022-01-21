

package pbb.lobby.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class SessionMessageDecoder extends ByteToMessageDecoder {
    private final ByteBuf buffer = Unpooled.buffer(1024);
    private int readCounter;

    public SessionMessageDecoder() {
    }

    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        int length = this.decodePacketLength(in);
        if (length != -1 && in.isReadable(length)) {
            byte[] bytes = new byte[length];
            in.readBytes(bytes);
            int read = LZFDecompression.decodeChunk(bytes, 0, this.buffer.array(), 0);
            this.buffer.readerIndex(0);
            this.buffer.writerIndex(read);
            int header = this.buffer.readUnsignedByte();
            if (header != 11) {
                throw new IllegalStateException();
            } else {
                int counter = this.buffer.readUnsignedByte();
                if (counter != this.readCounter) {
                    throw new IllegalStateException();
                } else {
                    this.readCounter = this.readCounter + 1 & 255;
                    int typeId = this.buffer.readUnsignedByte();
                    MessageType type = null;
                    MessageType[] var11 = MessageType.values();
                    int var12 = var11.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        MessageType value = var11[var13];
                        if (value.id() == typeId) {
                            type = value;
                            break;
                        }
                    }

                    if (type == null) {
                        throw new IllegalStateException();
                    } else {
                        byte[] payload = new byte[this.buffer.readableBytes()];
                        this.buffer.readBytes(payload);
                        SessionInMessage s=new SessionInMessage(type, payload);
                        System.out.println(s.toString());
                        out.add(s);
                    }
                }
            }
        } else {
            in.resetReaderIndex();
        }
    }

    private int decodePacketLength(ByteBuf buffer) {
        int readable = buffer.readableBytes();
        if (readable <= 0) {
            throw new IllegalStateException();
        } else {
            int length = buffer.readUnsignedByte();
            int type = length & 192;
            short mb;
            short lb;
            short lsb;
            int msb;
            switch(type) {
                case 0:
                    return length;
                case 64:
                    if (readable < 2) {
                        return -1;
                    }

                    msb = length & 63;
                    mb = buffer.readUnsignedByte();
                    return msb << 8 | mb;
                case 128:
                    if (readable < 4) {
                        return -1;
                    }

                    msb = length & 63;
                    mb = buffer.readUnsignedByte();
                    lb = buffer.readUnsignedByte();
                    lsb = buffer.readUnsignedByte();
                    return msb << 24 | mb << 16 | lb << 8 | lsb;
                case 192:
                    if (readable < 5) {
                        return -1;
                    } else {
                        msb = buffer.readUnsignedByte();
                        mb = buffer.readUnsignedByte();
                        lb = buffer.readUnsignedByte();
                        lsb = buffer.readUnsignedByte();
                        int value = msb << 24 | mb << 16 | lb << 8 | lsb;
                        if (((long)value & 4294967295L) > 2147483647L) {
                            throw new IllegalStateException();
                        }

                        return value;
                    }
                default:
                    throw new IllegalStateException();
            }
        }
    }
}
