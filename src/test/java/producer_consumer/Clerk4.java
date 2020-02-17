package producer_consumer;

public class Clerk4 {
    private int book;

    public synchronized void get() { // 進貨
        while (book >= 10) {
            System.out.println("書已滿");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " ,幾本書=" + ++book);
        this.notifyAll();

    }

    public synchronized void sell() {
        while (book <= 0) {
            System.out.println("缺貨");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " ,幾本書=" + --book);
        this.notifyAll();
    }
}
