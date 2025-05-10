package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Entity.Player;
import org.example.Entity.PlayerDB;
import org.example.Repositories.PlayerRepositories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ScoreTable extends JFrame {
    public ScoreTable() {
        ArrayList<PlayerDB> list = new ArrayList<>();
        try (Socket socket = new Socket("localhost", 8081);

             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("GET_PLAYERS");

            String jsonResponse = in.readLine();

            Gson gson = new Gson();
            list = gson.fromJson(jsonResponse, new TypeToken<ArrayList<PlayerDB>>() {
            }.getType());


        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle("Окно с таблицей");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Закрывает только это окно

        // Создаем модель таблицы
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Имя");
        model.addColumn("Кол-во побед");
        for (var player : list) {
            model.addRow(new Object[]{player.getId(), player.getName(), player.getCountWin()});
        }


        // Создаем таблицу
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // Позиционируем новое окно рядом с основным
//        setLocationRelativeTo(null);
    }
}
