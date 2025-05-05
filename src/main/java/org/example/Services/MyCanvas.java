package org.example.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.example.Entity.ObjectDTO;
import org.example.Entity.Player;
import org.example.Entity.TypeObjectDTO;
import org.example.MainFrame;
import org.example.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

@Getter
@Setter
public class MyCanvas extends JPanel implements ActionListener {
    private final MainFrame frame;
    private final int radius1 = 50;
    private final int radius2 = 30;
    private int ratio;
    private int circleY1;
    private int circleY2;
    private int speedY1;
    private int speedY2;
    private int posArrowX = 30;
    private int speedArrowX = 0;
    private final JLabel countShot1;
    private final JLabel countShot2;
    private final JLabel score1;
    private final JLabel score2;
    private int countShot1_;
    private int countShot2_;
    private int score1_;
    private int score2_;
    public boolean start = false;

    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<Player> playerList = new ArrayList<>();
    private Type type;

    public MyCanvas(MainFrame frame, int speedY1, int speedY2, JLabel countShot1, JLabel countShot2, JLabel score1, JLabel score2) {
        this.frame = frame;
        this.speedY1 = speedY1;
        this.speedY2 = speedY2;
        this.countShot1 = countShot1;
        this.countShot2 = countShot2;
        this.score1 = score1;
        this.score2 = score2;
        this.countShot1.setText(countShot1_ + " ");
        System.out.println("contr");
        this.countShot2.setText(countShot2_ + " ");
        this.score1.setText(score1_ + " ");
        this.score2.setText(score2_ + " ");

        type = new TypeToken<ArrayList<Player>>() {
        }.getType();
    }

    public void showMessageDialog(boolean IAmWinner) {
        JOptionPane.showMessageDialog(null, IAmWinner ? "Вы выиграли" : "Вы проиграли", "Конец Игры", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose(); // закрываем окно
        System.exit(0);  // завершаем программу с кодом 0 (успешное завершение)
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.out.println("start  - "+start);
//        if (start) {
        if (e != null && e.getActionCommand().equals("shot")) {
            speedArrowX = 2;
            countShot1_++;
            countShot1.setText(countShot1_ + " ");
        }
        circleY1 += speedY1;
        circleY2 += speedY2;
        if (circleY1 + radius1 > getHeight() || circleY1 < 0) {
            speedY1 *= -1; // Меняем направление по Y
        }
        if (circleY2 + radius2 > getHeight() || circleY2 < 0) {
            speedY2 *= -1; // Меняем направление по Y
        }
        posArrowX += speedArrowX;
        if ((posArrowX + 30 > getWidth() || posArrowX < 0) && getWidth() > 0) {
            speedArrowX = 0;
            posArrowX = 30;
        }
        if (posArrowX + 30 >= getWidth() * 0.7 && posArrowX <= getWidth() * 0.7 + radius1 && getHeight() / 2 >= circleY1 && getHeight() / 2 <= circleY1 + radius1) {
            posArrowX = 30;
            speedArrowX = 0;
            score1_++;
            score1.setText(score1_ + " ");
        }
        if (posArrowX + 30 >= getWidth() * 0.85 && posArrowX <= getWidth() * 0.85 + radius2 && getHeight() / 2 >= circleY2 && getHeight() / 2 <= circleY2 + radius2) {
            posArrowX = 30;
            speedArrowX = 0;
            score1_ += 2;
            score1.setText(score1_ + " ");
        }
        ObjectDTO objectDTO = new ObjectDTO(TypeObjectDTO.POSITION, MainFrame.name, posArrowX + ":" + score1_ + ":" + countShot1_);
        Gson gson = GsonFactory.getInstance();
        out.println(gson.toJson(objectDTO));
        try {
            String data = in.readLine();
            playerList = gson.fromJson(data, type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (score1_ >= Server.NEED_SCORE) {
//                System.out.println("<<<<<<<>>>>>>>>>>>");
//                System.out.println("player.getScore() >= Server.NEED_SCORE");
            showMessageDialog(true);
            start = false;
        }
        repaint();
//        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.drawLine((int) (getWidth() * 0.7) + radius1 / 2, 0, (int) (getWidth() * 0.7) + radius1 / 2, getHeight());
        g.drawLine((int) (getWidth() * 0.85) + radius2 / 2, 0, (int) (getWidth() * 0.85) + radius2 / 2, getHeight());
        paintCircle(g, 0.7, circleY1, radius1);
        paintCircle(g, 0.85, circleY2, radius2);
        if (start) {
            for (Player player : playerList) {
                paintArrow(g, player.getX(), player.getName().equals(MainFrame.name) ? Color.YELLOW : Color.RED);
                if (!player.getName().equals(MainFrame.name)) {
                    frame.getPlayer2().setText(player.getName());
                    frame.getScore2().setText(player.getScore() + "");
                    frame.getCountShot2().setText(player.getCountShot() + "");
                    if (player.getScore() >= Server.NEED_SCORE) {
//                    System.out.println("<<<<<<<>>>>>>>>>>>");
//                    System.out.println("player.getScore() >= Server.NEED_SCORE");
                        showMessageDialog(false);
                        start = false;
                        break;
                    }
                }
            }
        }


    }

    private void paintCircle(Graphics g, double ratio, int posY, int radius) {
        g.setColor(Color.RED);
        g.fillOval((int) (getWidth() * ratio), posY, radius, radius);
    }

    private void paintArrow(Graphics g, int posX, Color color) {
        g.setColor(color);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(3));

        int startX = posX;
        int startY = getHeight() / 2;
        int endX = startX + 30;
        int endY = getHeight() / 2;

        g2d.drawLine(startX, startY, endX, endY);

        drawArrowHead(g2d, startX, startY, endX, endY);
    }

    private void drawArrowHead(Graphics2D g2d, int startX, int startY, int endX, int endY) {
        double angle = Math.atan2(endY - startY, endX - startX); // Угол наклона линии
        int arrowLength = 10; // Длина наконечника стрелки

        int x1 = (int) (endX - arrowLength * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (endY - arrowLength * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (endX - arrowLength * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (endY - arrowLength * Math.sin(angle + Math.PI / 6));

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(endX, endY); // Вершина треугольника
        arrowHead.addPoint(x1, y1); // Левая точка основания
        arrowHead.addPoint(x2, y2); // Правая точка основания
        g2d.fill(arrowHead); // Заливаем треугольник
    }
}
