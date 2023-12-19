package Print;

public class PrintNumber {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Printer(1);
        Thread t2 = new Printer(2);
        Thread t3 = new Printer(3);

        t3.start();
        t3.join();
        t2.start();
        t2.join();
        t1.start();
        t1.join();
    }
}
class Printer extends Thread{
    int number = 0;

    public Printer(int number) {
        this.number = number;
    }

    public void first() {System.out.println("First");}

    public void second() {System.out.println("Second");}

    public void third() {System.out.println("Third");}

    public void run(){
        switch (this.number) {
            case (1) -> first();
            case (2) -> second();
            case (3) -> third();
        }
    }
}
