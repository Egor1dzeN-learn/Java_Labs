package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyArrow extends JPanel implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Устанавливаем толщину линии
        g2d.setStroke(new BasicStroke(3));

        // Координаты начала и конца стрелки
        int startX = 50;
        int startY = 100;
        int endX = 300;
        int endY = 100;

        // Рисуем линию (стержень стрелки)
        g2d.drawLine(startX, startY, endX, endY);
        drawArrowHead(g2d, startX, startY, endX, endY);
    }
    private void drawArrowHead(Graphics2D g2d, int startX, int startY, int endX, int endY) {
        double angle = Math.atan2(endY - startY, endX - startX); // Угол наклона линии
        int arrowLength = 20; // Длина наконечника стрелки

        // Координаты для треугольника (наконечника)
        int x1 = (int) (endX - arrowLength * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (endY - arrowLength * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (endX - arrowLength * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (endY - arrowLength * Math.sin(angle + Math.PI / 6));

        // Рисуем треугольник
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(endX, endY); // Вершина треугольника
        arrowHead.addPoint(x1, y1); // Левая точка основания
        arrowHead.addPoint(x2, y2); // Правая точка основания
        g2d.fill(arrowHead); // Заливаем треугольник
    }
}
