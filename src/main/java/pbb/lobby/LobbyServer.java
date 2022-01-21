

package pbb.lobby;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import pbb.lobby.handshake.LobbyHandshakeHandler;

public class LobbyServer {
    public LobbyServer() {
    }

    public static void main(String[] args) {
        int port = 25000;
        NioEventLoopGroup loop = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = ((ServerBootstrap)(new ServerBootstrap()).channel(NioServerSocketChannel.class)).group(loop).childOption(ChannelOption.TCP_NODELAY, true).childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel channel) {
                    channel.pipeline().addLast("handler", new LobbyHandshakeHandler());
                }
            });
            ChannelFuture future = bootstrap.bind(port).syncUninterruptibly();
            System.out.println("The server is connected 127.0.0.1:" + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        } finally {
            loop.shutdownGracefully();
        }

    }
}
