package com.sale_system.service;

import java.io.IOException;
import java.net.Socket;

public class BIOMysqlClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",9098);
            socket.getOutputStream().write("send meesage to server".getBytes());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
