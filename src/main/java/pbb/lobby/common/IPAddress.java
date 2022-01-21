

package pbb.lobby.common;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class IPAddress {
    private final int address;
    private final int port;

    public IPAddress(int address, int port) {
        if (port >= 0 && port <= 65535) {
            this.address = address;
            this.port = port;
        } else {
            throw new IllegalArgumentException("Port out of valid range: " + port);
        }
    }

    public int address() {
        return this.address;
    }

    public int port() {
        return this.port;
    }

    public InetAddress inetAddress() throws UnknownHostException {
        // returneaza adresa ip transformata din int
        byte[] bytes = new byte[]{(byte)(this.address >> 24), (byte)(this.address >> 16), (byte)(this.address >> 8), (byte)this.address};
        return InetAddress.getByAddress(bytes);
    }

    public InetSocketAddress inetSocketAddress() throws UnknownHostException {
        // returneaza adresa ip transformata din int si portul
        return new InetSocketAddress(this.inetAddress(), this.port);
    }
}
