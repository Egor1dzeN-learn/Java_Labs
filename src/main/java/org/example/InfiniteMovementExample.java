package org.example;

import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
public class InfiniteMovementExample extends JPanel implements ActionListener {
    private int circleX = 50; // Начальная позиция кружка по X
    private int circleY = 50; // Начальная позиция кружка по Y
    private final int circleSize = 50; // Размер кружка
    private int deltaX = 5; // Шаг перемещения по X
    private int deltaY = 5; // Шаг перемещения по Y

    public InfiniteMovementExample() {
        // Создаем таймер с интервалом 50 мс (20 кадров в секунду)
//        Timer timer = new Timer(20, this);
//        timer.start(); // Запускаем таймер
        Thread thread = new Thread(() -> {
            while (true) {
                actionPerformed(null);
                try {
                    Thread.sleep(Duration.ofMillis(10L));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println("Infinite Movement Example");
            }
        });
        thread.start();
//        thread.interrupt();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Рисуем кружок
        g.setColor(Color.RED);
        g.fillOval((int) (getWidth()*0.9), circleY, circleSize, circleSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Обновляем координаты кружка
        circleX += deltaX;
        circleY += deltaY;

        // Проверяем границы окна и меняем направление, если кружок достиг края
        if (circleX + circleSize > getWidth() || circleX < 0) {
            deltaX = -deltaX; // Меняем направление по X
        }
        if (circleY + circleSize > getHeight() || circleY < 0) {
            deltaY = -deltaY; // Меняем направление по Y
        }

        repaint(); // Перерисовываем компонент
    }

    public int getCircleX() {
        return circleX;
    }
}
