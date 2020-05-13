package com.sale_system.service.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class ReadWriteHandler implements CompletionHandler<Integer,Attachment> {

    private final static Charset CSUTF8 = Charset.forName("UTF-8");

    @Override
    public void completed(Integer result, Attachment attachment) {
        if(result == -1){
            try {
                attachment.channelClient.close();
                System.out.printf("Stopped listening to client %s%n",attachment.clientAddr);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            return;
        }
        if(attachment.isReadModel){
            attachment.buffer.flip();
            int limit = attachment.buffer.limit();
            byte[] bytes = new byte[limit];
            attachment.buffer.get(bytes,0,limit);
            System.out.printf("client at %s send message:%s%n",attachment.clientAddr,new String(bytes,CSUTF8));
            attachment.isReadModel = false;
            attachment.buffer.rewind();
            String responseMsg = "Server accept message:"+new String(bytes,CSUTF8);
            attachment.channelClient.write(ByteBuffer.wrap(responseMsg.getBytes()),attachment,this);
        }else{
            attachment.isReadModel = true;
            attachment.buffer.clear();
            attachment.channelClient.read(attachment.buffer,attachment,this);
        }
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {
        System.out.println("Connection with client broken");
    }
}
