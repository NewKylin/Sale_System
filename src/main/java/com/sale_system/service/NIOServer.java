package com.sale_system.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    private Selector selector;

    public static void main(String[] args) throws IOException {
        new NIOServer().init(9981).listen();
    }
    public NIOServer init(int port) throws IOException {
        //打开一个监听到访TCP连接的套接字
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //设置非阻塞
        serverChannel.configureBlocking(false);
        //绑定端口
        serverChannel.bind(new InetSocketAddress(port));
        //新建Selector
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    public void listen() throws IOException{
        System.out.println("服务器启动");
        //轮询selector
        while (true){
            //当有注册的事件到达的时候返回，否则阻塞
            selector.select();
            //调用select()方法返回的时候，表明已经有一个或者多个channel已经有它关注的事件发生了
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> ite = selectionKeys.iterator();
            while (ite.hasNext()){
                SelectionKey key = ite.next();
                ite.remove();
                //测试是否准备好接收一个新的连接
                if(key.isAcceptable()){
                    //新建一个可以监听到访TCP连接的服务Channel
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    //建立和客户端的连接通道
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    channel.write(ByteBuffer.wrap("server has accept you request".getBytes()));
                    channel.register(selector,SelectionKey.OP_READ);
                    System.out.println("与客户端已经建立连接");
                }else if(key.isReadable()){//有可读数据事件
                    //获取客户端传输数据通道
                    SocketChannel channel = (SocketChannel) key.channel();
                    //分配缓冲区
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    int bytesRead = channel.read(buffer);
                    byte[] bytes = buffer.array();
                    String message = new String(bytes);
                    if(bytesRead == -1){
                        key.cancel();
                        key.channel().close();
                    }
                    System.out.println("accept message:"+message);
                }
            }
        }
    }


}
