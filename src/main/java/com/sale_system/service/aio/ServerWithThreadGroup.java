package com.sale_system.service.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

public class ServerWithThreadGroup {
    private final static int PORT = 9090;

    private final static String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withFixedThreadPool(20, Executors.defaultThreadFactory());

        AsynchronousServerSocketChannel channelServer;
        try {
            channelServer = AsynchronousServerSocketChannel.open(group);
            channelServer.bind(new InetSocketAddress(HOST, PORT));
            System.out.printf("Server listening at %s%n", channelServer.getLocalAddress());
        } catch (IOException ioe) {
            System.out.println("Unable to open or bind server socket channel");
            return;
        }

        Attachment att = new Attachment();
        att.channelServer = channelServer;
        channelServer.accept(att, new ConnectionHandler());

        try {
            Thread.currentThread().join();
        } catch (InterruptedException ie) {
            System.out.println("Server terminating");
        }
    }
}
