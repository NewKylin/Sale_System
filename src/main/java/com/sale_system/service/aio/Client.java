package com.sale_system.service.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public class Client {
    private final static Charset CSUTF8 = Charset.forName("UTF-8");

    private final static int PORT  = 9090;

    private final static String HOST = "localhost";

    public static void main(String[] args) {
        AsynchronousSocketChannel channel;
        try{
            channel = AsynchronousSocketChannel.open();
        }catch (IOException ioe){
            System.out.println("Unable to open client socket channel");
            return;
        }

        try{
            channel.connect(new InetSocketAddress(HOST,PORT)).get();
        }catch (ExecutionException | InterruptedException eie){
            System.err.println("Sever not responding");
            return;
        }

        ClientAttachment att = new ClientAttachment();
        att.channel = channel;
        att.isReadModel = false;
        att.buffer = ByteBuffer.allocate(2048);
        att.mainThd = Thread.currentThread();

        byte[] data = "Hello".getBytes(CSUTF8);
        att.buffer.put(data);
        att.buffer.flip();
        channel.write(att.buffer,att,new ClientReadWriteHandler());

        try{
            att.mainThd.join();
        }catch (InterruptedException ie){
            System.out.println("client terminating");
        }
    }

}
