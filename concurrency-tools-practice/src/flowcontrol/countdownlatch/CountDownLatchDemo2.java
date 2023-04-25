package flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用法二：多个线程等待某个线程的信号，同时开始执行。
 *
 * @Description: 模拟100米跑步，5名选手都准备好了，只等裁判员一声令下，所有人同时开始跑步。
 */
public class CountDownLatchDemo2 {
    
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch begin = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = () -> {
                System.out.println("NO." + no + "准备完毕，等待发令枪");
                try {
                    begin.await();
                    System.out.println("NO." + no + "开始跑步！");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            service.submit(runnable);
        }
        
        Thread.sleep(5000);
        System.out.println("发令枪响，比赛开始！");
        begin.countDown();
        service.shutdown();
    }
}
