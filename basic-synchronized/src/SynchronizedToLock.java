import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 */
public class SynchronizedToLock {
    
    Lock lock = new ReentrantLock();
    
    private synchronized void method1() {
        System.out.println("我是Synchronized形式");
    }
    
    private void method2() {
        lock.lock();
        try {
            System.out.println("我是Lock形式");
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        SynchronizedToLock s = new SynchronizedToLock();
        s.method1();
        s.method2();
    }
}
