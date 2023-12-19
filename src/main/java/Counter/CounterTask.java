package Counter;


public class CounterTask {
    public static void main(String[] args) throws InterruptedException {
        MyRunnable runnable = new MyRunnable();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("countInt = " + Counter.countInt);
        System.out.println("countLong = " + Counter.countLong);

    }
}

class Counter{
    static int countInt = 0;
    static long countLong = 0;
}

class MyRunnable implements Runnable{
    public synchronized void increment(){
        Counter.countInt++;
        Counter.countLong++;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i ++){
            increment();
        }
    }
}