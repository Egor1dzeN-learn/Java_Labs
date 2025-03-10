package org.example;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;

@Getter
@Setter
public class MyThread extends Thread {
    private MyCanvas myCanvas;
    private boolean shot;
    private boolean start = false;

    MyThread(MyCanvas myCanvas) {
        this.myCanvas = myCanvas;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            myCanvas.setStart(start);
            if (shot) {
                myCanvas.actionPerformed(new ActionEvent(new Object(), 0, "shot"));
                shot = false;
            } else {
                myCanvas.actionPerformed(null);
            }
//            System.out.println(running);
            try {
                Thread.sleep(Duration.ofMillis(10));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
