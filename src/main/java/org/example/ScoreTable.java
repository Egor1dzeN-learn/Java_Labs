package org.example;

import org.example.Repositories.PlayerRepositories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ScoreTable extends JFrame {
    public ScoreTable() {
        setTitle("Окно с таблицей");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Закрывает только это окно

        // Создаем модель таблицы
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Имя");
        model.addColumn("Кол-во побед");
        var playerRepo = new PlayerRepositories();
        var playerList = playerRepo.findAll();
        for (var player : playerList) {
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
