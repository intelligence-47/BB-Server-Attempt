

package pbb.lobby.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SessionInMessage {
    private final MessageType type;
    private final byte[] payload;
    private final ByteBuf buffer;

    public SessionInMessage(MessageType type, byte[] payload) {
        if (payload == null) {
            throw new IllegalArgumentException();
        } else {
            this.type = type;
            this.payload = payload;
            this.buffer = Unpooled.wrappedBuffer(payload).asReadOnly();
        }
    }

    public MessageType type() {
        return this.type;
    }

    public ByteBuf buffer() {
        return this.buffer;
    }

    public String toString() {
        return String.format("[SessionInMessage(type: %s, payload: %s)]", this.formatType(), this.formatPayload());
    }

    private String formatType() {
        return String.format("%s(%d)", this.type.toString(), this.type.id());
    }

    private String formatPayload() {
        if (this.payload.length == 0) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder(this.payload.length * 3);
            byte[] var2 = this.payload;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                String c = Integer.toString(b & 255, 16);
                if (c.length() == 1) {
                    builder.append("0");
                }

                builder.append(c.toUpperCase());
                builder.append(' ');
            }

            return builder.substring(0, builder.length() - 1);
        }
    }
}
