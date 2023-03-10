/**
 * Simultaneous access to static synchronized and non-static synchronized methods?
 */
public class QuestionSix implements Runnable {
    
    private static QuestionSix instance = new QuestionSix();
    
    public static synchronized void method1() {
        System.out.println("我是加锁方法1, 静态。我叫" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "运行结束");
    }
    
    public synchronized void method2() {
        System.out.println("我是加锁的方法2, 非静态。我叫" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "运行结束");
    }
    
    @Override
    public void run() {
        if (Thread.currentThread().getName().equals("Thread-0"))
            method1();
        else
            method2();
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {
        
        }
        System.out.println("finished.");
    }
}
