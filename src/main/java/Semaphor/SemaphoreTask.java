package Semaphor;

import java.util.concurrent.Semaphore;

public class SemaphoreTask {
    public static void main(String[] args) {
        Semaphore kp = new Semaphore(5);
        for (int i = 1; i <= 12; i ++) {
            new Truck("Truck" + i, kp);
        }
    }
}

class Truck extends Thread {
    String name;
    private Semaphore kp;

    public Truck(String name, Semaphore kp) {
        this.name = name;
        this.kp = kp;
        this.start();
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " ждет...");
            kp.acquire();
            System.out.println(name + " взвешивается");
            sleep(2000);
            System.out.println(name + " завершил взвешивание");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            kp.release();
        }
    }
}