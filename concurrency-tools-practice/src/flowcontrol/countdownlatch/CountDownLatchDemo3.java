package flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 模拟100米跑步，5名选手都准备好了，只等裁判员一声令下，所有人同时开始跑步。当所有人都到终点后，比赛结束
 *
 * 总结：是不能重用的。如果需要重新计数，可以考虑使用CyclicBarrier/创建新的CountDownLatch
 */
public class CountDownLatchDemo3 {
    
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = () -> {
                System.out.println("No." + no + "准备完毕，等待发令枪。");
                try {
                    begin.await();
                    System.out.println("No." + no + "开始跑步。");
                    Thread.sleep((long) (Math.random() *  1000));
                    System.out.println("No." + no + "跑到终点了。");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    end.countDown();
                }
            };
            
            service.submit(runnable);
        }
        
        // 起点
        Thread.sleep(5000);
        System.out.println("发令枪响，比赛开始。");
        begin.countDown();
        
        // 终点
        end.await();
        System.out.println("所有人到达终点，比赛结束。");
        
        service.shutdown();
    }
}
