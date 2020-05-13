package com.sale_system.service.aio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel,Attachment> {
    @Override
    public void completed(AsynchronousSocketChannel channelClient, Attachment attachment) {
        try {
            SocketAddress clientAddr = channelClient.getRemoteAddress();
            System.out.printf("Accept connection from %s%n", clientAddr);

            attachment.channelServer.accept(attachment, this);

            Attachment newAtt = new Attachment();
            newAtt.channelServer = attachment.channelServer;
            newAtt.channelClient = channelClient;
            newAtt.isReadModel = true;
            newAtt.buffer = ByteBuffer.allocate(2048);
            newAtt.clientAddr = clientAddr;

            ReadWriteHandler rwh = new ReadWriteHandler();
            channelClient.read(newAtt.buffer, newAtt, rwh);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {
        System.out.println("Failed to accept connection");
    }
}
