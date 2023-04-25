package flowcontrol.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Description: 相比与CountDownLatch而言，CyclicBarrier能重置并再次使用。
 */
public class CyclicBarrierDemo {
    
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> System.out.println("所有人都在场了，大家统一出发！"));
        for (int i = 0; i < 10; i++) {
            new Thread(new Task(i, cyclicBarrier)).start();
        }
    }
    
    record Task(int id, CyclicBarrier cyclicBarrier) implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("线程" + id + "现在前往集合地点");
                cyclicBarrier.await();
                System.out.println("线程" + id + "出发了");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
