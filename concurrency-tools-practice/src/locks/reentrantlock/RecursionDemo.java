package locks.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 递归获取可重入锁
 */
public class RecursionDemo {
    private static final ReentrantLock lock = new ReentrantLock();
    
    private static void accessResource() {
        lock.lock();
        try {
            System.out.println("已经对资源进行了处理");
            if (lock.getHoldCount() < 5) {
                System.out.println(lock.getHoldCount());
                accessResource();
                System.out.println(lock.getHoldCount());
            }
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        accessResource();
    }
}
