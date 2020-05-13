package com.sale_system.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOMysqlServer {
    static byte[] bs = new byte[10];
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9098);
            while (true){
                final Socket clientSocket = serverSocket.accept();
                System.out.println("accept....");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            clientSocket.getInputStream().read(bs);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("read.....");
                        System.out.println(new String(bs));
                    }
                });
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
