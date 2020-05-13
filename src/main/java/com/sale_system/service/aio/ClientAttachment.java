package com.sale_system.service.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class ClientAttachment {

    public AsynchronousSocketChannel channel;

    public boolean isReadModel;

    public ByteBuffer buffer;

    public Thread mainThd;

}
