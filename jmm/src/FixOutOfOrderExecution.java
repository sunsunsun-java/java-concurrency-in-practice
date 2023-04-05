import java.util.concurrent.CountDownLatch;

/**
 * @Description: 修复重排序问题
 */
public class FixOutOfOrderExecution {
    private volatile static int x = 0, y = 0;
    private volatile static int a = 0, b = 0;
    
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for ( ; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            
            CountDownLatch latch = new CountDownLatch(3);
            
            Thread one = new Thread(() -> {
                try {
                    latch.countDown();
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                a = 1;
                x = b;
            });
            Thread two = new Thread(() -> {
                try {
                    latch.countDown();
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                b = 1;
                y = a;
            });
            one.start();
            two.start();
            latch.countDown();
            one.join();
            two.join();
            
            String res = "第" + i + "次（" + x + "," + y + ")";
            if (x == 0 && y == 0) {
                System.out.println(res);
                break;
            } else {
                System.out.println(res);
            }
        }
    }
}
