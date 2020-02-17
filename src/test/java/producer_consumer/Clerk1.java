package producer_consumer;

public class Clerk1 {
    private int book;

    public void get() { // 進貨
        if (book >= 10) {
            System.out.println("書已滿");
        } else {
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + ++book);
        }
    }

    public void sell() {
        if (book <= 0) {
            System.out.println("缺貨");
        } else {
            System.out.println(Thread.currentThread().getName() + " ,幾本書=" + --book);
        }
    }
}
