package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @Description: 两个线程交替打印0~100的奇偶数，用wait和notify
 */
public class WaitNotifyPrintOddEvenWait {
    
    private static volatile int count = 0;
    private static final Object lock = new Object();
    
    public static void main(String[] args) throws InterruptedException {
        new Thread(new TurningRunner(), "偶数").start();
        Thread.sleep(10);
        new Thread(new TurningRunner(), "奇数").start();
    }
    
    static class TurningRunner implements Runnable {
    
        @Override
        public void run() {
            while (count <= 100) {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + ":" + count++);
                    lock.notify();
                    if (count <= 100) { // 如果任务还没完成，就释放当前锁并休眠
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
}
