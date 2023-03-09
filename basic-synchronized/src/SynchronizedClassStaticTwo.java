
public class SynchronizedClassStaticTwo implements Runnable {

    private static SynchronizedClassStaticTwo instance1 = new SynchronizedClassStaticTwo();
    private static SynchronizedClassStaticTwo instance2 = new SynchronizedClassStaticTwo();
    
    public void method() {
        synchronized (SynchronizedClassStaticTwo.class) {
            System.out.println("我是对象锁的方法修饰符形式。我叫" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "运行结束");
        }
    }
    
    @Override
    public void run() {
        method();
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(instance1);
        Thread t2 = new Thread(instance2);
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {
        
        }
        System.out.println("finished.");
    }
}
