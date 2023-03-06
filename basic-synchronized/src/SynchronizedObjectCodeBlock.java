/**
 * @Description:
 */
public class SynchronizedObjectCodeBlock implements Runnable {
    
    private static final SynchronizedObjectCodeBlock instance = new SynchronizedObjectCodeBlock();
    final Object lock1 = new Object();
    final Object lock2 = new Object();
    
    @Override
    public void run() {
//        synchronized (this) {
//            System.out.println("I'm the form synchronized object code block. My name is: " + Thread.currentThread().getName());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println(Thread.currentThread().getName() + " work over.");
//        }
    
        synchronized (lock1) {
            System.out.println("I'm the form synchronized object code block lock1. My name is: " + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " work over.");
        }
    
        synchronized (lock2) {
            System.out.println("I'm the form synchronized object code block lock2. My name is: " + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " work over.");
        }
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {
        
        }
        System.out.println("The program is finished.");
    }
}
