package locks.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description:
 */
public class CinemaReadWriteQueue {
    
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    
    private static void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放读锁");
            readLock.unlock();
        }
    }
    
    private static void write() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放写锁");
            writeLock.unlock();
        }
    }
    
    public static void main(String[] args) {
        new Thread(CinemaReadWriteQueue::write, "Thread1").start();
        new Thread(CinemaReadWriteQueue::read, "Thread2").start();
        new Thread(CinemaReadWriteQueue::read, "Thread3").start();
        new Thread(CinemaReadWriteQueue::write, "Thread4").start();
        new Thread(CinemaReadWriteQueue::read, "Thread5").start();
    }
}
