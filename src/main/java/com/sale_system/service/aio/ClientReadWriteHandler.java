package com.sale_system.service.aio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class ClientReadWriteHandler implements CompletionHandler<Integer,ClientAttachment> {

    private final static Charset CSUTF8 = Charset.forName("UTF-8");

    private BufferedReader conReader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void completed(Integer result, ClientAttachment attachment) {
        if(attachment.isReadModel){
            attachment.buffer.flip();
            int limit = attachment.buffer.limit();
            byte[] bytes = new byte[limit];
            attachment.buffer.get(bytes,0,limit);
            String msg = new String(bytes,CSUTF8);
            System.out.printf("Server responded:%s%n",msg);

            try {
                msg = "";
                while (msg.length() == 0){
                    System.out.print("Enter message(\"end\" to quit):");
                    msg = conReader.readLine();
                }
                if(msg.equalsIgnoreCase("end")){
                    attachment.mainThd.interrupt();
                    return;
                }
            } catch (IOException e) {
                System.err.println("Unable to read from console");
            }

            attachment.isReadModel = false;
            attachment.buffer.clear();
            byte[] data =  msg.getBytes(CSUTF8);
            attachment.buffer.put(data);
            attachment.buffer.flip();
            attachment.channel.write(attachment.buffer,attachment,this);
        }else{
            attachment.isReadModel = true;
            attachment.buffer.clear();
            attachment.channel.read(attachment.buffer,attachment,this);
        }
    }

    @Override
    public void failed(Throwable exc, ClientAttachment attachment) {
        System.err.println("Server not responding");
        System.exit(1);
    }
}
