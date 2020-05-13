package com.sale_system.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient {

    private Selector selector;

    public static void main(String[] args) throws IOException {
        new NioClient().init("127.0.0.1",9981).listen();
    }
    public NioClient init(String serverIp,int port) throws IOException{
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        //获得通道管理器
        selector = Selector.open();

        channel.connect(new InetSocketAddress(serverIp,port));
        channel.register(selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws IOException{
        System.out.println("客户端启动");
        while (true){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> ite = keys.iterator();
            while (ite.hasNext()){
                SelectionKey key = ite.next();
                ite.remove();
                if(key.isConnectable()){
                    SocketChannel channel = (SocketChannel) key.channel();

                    //如果正在连接，则完成连接
                    if(channel.isConnectionPending()){
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);
                    //向服务器发送消息
                    channel.write(ByteBuffer.wrap("send message to server".getBytes()));
                    channel.register(selector,SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                }else if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    byte[] data = buffer.array();
                    String message  = new String(data);
                    System.out.println("receive message from server:"+message);
                }
            }
        }
    }
}
