/**
 * After the method throws an exception, the lock is released?
 */
public class QuestionSeven implements Runnable {
    
    private static QuestionSeven instance = new QuestionSeven();
    
    public synchronized void method1() {
        System.out.println("我是加锁方法1。我叫" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException();
    }
    
    public synchronized void method2() {
        System.out.println("我是加锁的方法2。我叫" + Thread.currentThread().getName());
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
