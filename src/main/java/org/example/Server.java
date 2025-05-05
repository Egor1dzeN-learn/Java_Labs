package org.example;

import com.google.gson.Gson;
import org.example.Entity.ObjectDTO;
import org.example.Entity.Player;
import org.example.Entity.PlayerDB;
import org.example.Entity.TypeObjectDTO;
import org.example.Repositories.PlayerRepositories;
import org.example.Services.GsonFactory;
import org.example.Services.HibernateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Server {
    public static List<Socket> sockets = new ArrayList<>();
    public static final List<Player> playerList = new ArrayList<>();
    public static final int PLAYER_COUNT = 2;
    public static final int NEED_SCORE = 10;
    private static final PlayerRepositories playerRepositories = new PlayerRepositories();

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(PLAYER_COUNT);
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
                        do {
                            String data = in.readLine();
                            objectDTO = gson.fromJson(data, ObjectDTO.class);
                            System.out.println(objectDTO);
                        } while (objectDTO.getType() != TypeObjectDTO.READY);
                        cyclicBarrier.await();
                        out.println(gson.toJson(new ObjectDTO(TypeObjectDTO.READY, objectDTO.getName(), "")));
                        String data = in.readLine();
                        System.out.println(data);
                        objectDTO = gson.fromJson(data, ObjectDTO.class);
//                        System.out.println(objectDTO);
                        Player player = new Player();
                        if (objectDTO.getType() == TypeObjectDTO.POSITION) {
                            player.setName(objectDTO.getName());
                            String[] strings = objectDTO.getData().split(":");
                            player.setX(Integer.parseInt(strings[0]));
                            player.setScore(Integer.parseInt(strings[1]));
                            player.setCountShot(Integer.parseInt(strings[2]));
                            synchronized (playerList) {
                                playerList.add(player);
                                out.println(gson.toJson(playerList));
                            }
//                            System.out.println(playerList == null);



//                            System.out.println("New player " + player + ", current list: " + playerList);
                        }
                        while (true) {
                            data = in.readLine();
                            objectDTO = gson.fromJson(data, ObjectDTO.class);
//                            System.out.println("New info " + objectDTO);
                            player.setName(objectDTO.getName());
//                          player.setColor(objectDTO.getColor());
                            String[] strings = objectDTO.getData().split(":");
                            player.setX(Integer.parseInt(strings[0]));
                            player.setScore(Integer.parseInt(strings[1]));
                            player.setCountShot(Integer.parseInt(strings[2]));
                            out.println(gson.toJson(playerList));
//                            System.out.println("Send player list " + playerList);
//                            System.out.println("count: "+player.getCountShot());
                            if (player.getScore() >= Server.NEED_SCORE){
                                playerRepositories.incScore(player.getName());
                                return;
                            }
                            for (Player player_ : playerList) {
                                if (player_.getScore() >= Server.NEED_SCORE){
                                    System.out.println(player_.getName() +" win!!");
                                    return;
                                }
                            }
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
}
