package threadcoreknowledge.threaddoubleedged;

/**
 * @Description: 第二点：死锁
 */
public class MultiThreadError1 implements Runnable {
    
    int flag = 1;
    static final Object o1 = new Object();
    static final Object o2 = new Object();
    
    public static void main(String[] args) {
        MultiThreadError1 r1 = new MultiThreadError1();
        MultiThreadError1 r2 = new MultiThreadError1();
        r1.flag = 1;
        r2.flag = 0;
        new Thread(r1).start();
        new Thread(r2).start();
    }
    
    @Override
    public void run() {
        System.out.println("flag = " + flag);
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (o2) {
                    System.out.println("1");
                }
            }
        }
    
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (o1) {
                    System.out.println("1");
                }
            }
        }
    }
}
