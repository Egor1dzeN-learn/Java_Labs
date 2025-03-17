package org.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Server {
    public static List<Socket> sockets = new ArrayList<Socket>();
    public static volatile List<Player> playerList = new ArrayList<>();

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is working, " + serverSocket.getLocalSocketAddress());
            while (true) {
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                new Thread(() -> {
                    Socket mySocket = socket;
                    System.out.println("Клиент подключился " + mySocket.getLocalSocketAddress() + " : " + mySocket.getPort());
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        Gson gson = GsonFactory.getInstance();
                        ObjectDTO objectDTO;
                        while (true) {
                            String data = in.readLine();
                            objectDTO = gson.fromJson(data, ObjectDTO.class);
                            System.out.println(objectDTO);
                            if (objectDTO.getType() == TypeObjectDTO.READY)
                                break;
                        }
                        cyclicBarrier.await();
                        out.println(gson.toJson(new ObjectDTO(TypeObjectDTO.READY, objectDTO.getName(), "")));
                        String data = in.readLine();
                        objectDTO = gson.fromJson(data, ObjectDTO.class);
                        System.out.println(objectDTO);
                        Player player = new Player();
                        if (objectDTO.getType() == TypeObjectDTO.POSITION) {
                            player.setName(objectDTO.getName());
//                          player.setColor(objectDTO.getColor());
                            player.setX(Integer.parseInt(objectDTO.getData()));
                            playerList.add(player);
                            out.println(gson.toJson(playerList));
                            System.out.println("New player "+player);
                        }
                        while (true){
                            data = in.readLine();
                            objectDTO = gson.fromJson(data, ObjectDTO.class);
                            System.out.println("New info "+objectDTO);
                            player.setName(objectDTO.getName());
//                          player.setColor(objectDTO.getColor());
                            player.setX(Integer.parseInt(objectDTO.getData()));
                            out.println(gson.toJson(playerList));
                            System.out.println("Send player list "+playerList);
                        }
                    } catch (IOException e) {
                        System.err.println("Connection is failed!");
                    } catch (BrokenBarrierException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handle(Socket mySocket, Socket otherSocket) {

    }
}
