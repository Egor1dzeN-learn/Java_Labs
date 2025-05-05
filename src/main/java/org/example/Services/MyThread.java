package org.example.Services;

import lombok.Getter;
import lombok.Setter;
import org.example.MainFrame;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.Duration;

@Getter
@Setter
public class MyThread extends Thread {
    private MyCanvas myCanvas;
    private MainFrame mainFrame;
    private boolean shot;
    private boolean start = false;
    private boolean cont = false;
    private BufferedReader in;
    private PrintWriter out;

    public MyThread(MyCanvas myCanvas) {
        this.myCanvas = myCanvas;
    }

    @Override
    public void run() {
        super.run();
        myCanvas.setIn(in);
        myCanvas.setOut(out);
        while (true) {
            myCanvas.setStart(start);
            if (shot) {
                myCanvas.actionPerformed(new ActionEvent(new Object(), 0, "shot"));
                System.out.println("Start: "+myCanvas.start);

                shot = false;
            } else {
                myCanvas.actionPerformed(null);
            }
            if (!myCanvas.isStart()) {
                System.out.println("Win");
//                return;
            }
            try {
                Thread.sleep(Duration.ofMillis(10));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
