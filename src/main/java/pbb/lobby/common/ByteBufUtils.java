package pbb.lobby.common;

import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

public final class ByteBufUtils {
    private ByteBufUtils() {
    }

    public static String readWCharString(ByteBuf buffer) {
        // caractere pe 2 octeti, returnate intr-un string
        // primii 2 octeti reprezinta lungimea textului
        int length = buffer.readUnsignedShortLE();
        byte[] bytes = new byte[length * 2];
        buffer.readBytes(bytes);
        //System.out.println(new String(bytes));
        return new String(bytes, StandardCharsets.UTF_16LE);
    }

    public static String readCharString(ByteBuf buffer) {
        // caractere simple, returnate intr-un string
        // primii 2 octeti reprezinta lungimea textului
        int length = buffer.readUnsignedShortLE();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        //System.out.println(new String(bytes));
        return new String(bytes);
    }

    public static IPAddress readIPAddress(ByteBuf buffer) {
        // returneaza adresa ip si portul citite din buffer
        int ip = ~buffer.readIntLE();
        int port = ~buffer.readShortLE() & '\uffff';
        return new IPAddress(ip, port);
    }

    public static void writeWCharString(ByteBuf buffer, String string) {
        //pune lungimea stringului pe primii 2 octeti pentru caractere pe 2 octeti
        //pune stringul in buffer
        buffer.writeShortLE(string.length());
        byte[] bytes = string.getBytes(StandardCharsets.UTF_16LE);
        buffer.writeBytes(bytes);
    }

    public static void writeCharString(ByteBuf buffer, String string) {
        //pune lungimea stringului pe primii 2 octeti pentru caractere pe 1 octet
        //pune stringul in buffer
        buffer.writeShortLE(string.length());
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buffer.writeBytes(bytes);
    }

    public static void writeIPAddress(ByteBuf buffer, IPAddress ipAddress) {
        // scrie adresa ip si portul in buffer
        buffer.writeIntLE(~ipAddress.address());
        buffer.writeShortLE(~ipAddress.port());
    }
}
