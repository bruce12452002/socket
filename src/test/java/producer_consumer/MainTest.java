package producer_consumer;

import java.util.concurrent.TimeUnit;

public class MainTest {
    public static void main(String[] args) {
//        Clerk1 clerk = new Clerk1(); // 已經知道缺貨或書已滿了，還一直重覆跑，浪費效能
        Clerk2 clerk = new Clerk2(); // 連線不會關閉
//        Clerk3 clerk = new Clerk3(); // 資料出錯
//        Clerk4 clerk = new Clerk4(); // 有考慮假喚醒

        Runnable pro = () -> {
            for (var i = 0; i < 20; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                    clerk.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable csm = () -> {
            for (var i = 0; i < 20; i++) {
                clerk.sell();
            }
        };

        new Thread(pro, "p1").start();
        new Thread(csm, "c1").start();
        new Thread(pro, "p2").start();
        new Thread(csm, "c2").start();
    }
}
