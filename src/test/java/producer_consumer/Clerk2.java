package producer_consumer;

public class Clerk2 {
    private int book;

    public synchronized void get() { // 進貨
        if (book >= 10) {
            System.out.println("書已滿");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + ++book);
            this.notifyAll();
        }
    }

    public synchronized void sell() {
        if (book <= 0) {
            System.out.println("缺貨");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + --book);
            this.notifyAll();
        }
    }
}
