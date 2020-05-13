package com.sale_system.service.aio;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {
    public AsynchronousServerSocketChannel channelServer;

    public AsynchronousSocketChannel channelClient;

    public boolean isReadModel;

    public ByteBuffer buffer;

    public SocketAddress clientAddr;
}
