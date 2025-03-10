package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            Socket socket1 = serverSocket.accept();
            System.out.println("Client1 is connected");
            Socket socket2 = serverSocket.accept();
            System.out.println("Client2 is connected");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handle(Socket mySocket, Socket otherSocket) {

    }
}
