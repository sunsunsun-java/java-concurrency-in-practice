package locks.reentrantlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 演示公平和不公平两种情况
 */
public class FairLock {
    public static void main(String[] args) {
        Waiting waiting = new Waiting();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new DoSomething(waiting));
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class DoSomething implements Runnable {
    Waiting waiting;
    
    public DoSomething(Waiting waiting) {
        this.waiting = waiting;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "开始");
        waiting.printJob();
        System.out.println(Thread.currentThread().getName() + "干活完毕");
    }
}

class Waiting {
    private final Lock queueLock = new ReentrantLock(false);
    
    public void printJob() {
        queueLock.lock();
        try {
            int duration = new Random().nextInt(10) + 1;
            System.out.println(Thread.currentThread().getName() + "正在干活，需要" + duration + "秒");
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            queueLock.unlock();
        }
    
        queueLock.lock();
        try {
            int duration = new Random().nextInt(10) + 1;
            System.out.println(Thread.currentThread().getName() + "正在干活，需要" + duration + "秒");
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            queueLock.unlock();
        }
    }
}
