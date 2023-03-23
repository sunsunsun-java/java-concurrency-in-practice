package threadcoreknowledge.stopthread.volatiledemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Description: 演示用 volatile 的局限part2：陷入阻塞时，volatile 是无法结束线程的。
 * 此案例中，生产者的生产速度很快，消费者消费速度慢，所以阻塞队列满了以后，生产者会阻塞，等待消费者进一步消费。
 */
public class WrongWayVolatileCannotStop {
    
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> storage = new ArrayBlockingQueue<>(10);
        
        Producer producer = new Producer(storage);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        Thread.sleep(1000);
        
        Consumer consumer = new Consumer(storage);
        while (consumer.needMoreNums()) {
            System.out.println(consumer.storage.take() + "被消费了");
            Thread.sleep(100);
        }
        System.out.println("消费者不需要更多数据了。");
        
        // 一旦消费不需要更多数据了，我们应该让生产者也停下来，但是实际情况下，被阻塞了
        producer.canceled = true;
        System.out.println(producer.canceled);
    }
    
    
}

class Producer implements Runnable {
    public volatile boolean canceled = false;
    BlockingQueue<Integer> storage;
    
    public Producer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }
    
    @Override
    public void run() {
        int num = 0;
        try {
            while (num <= 100000 && !canceled) {
                if (num % 100 == 0) {
                    storage.put(num); // 会在此处阻塞
                    System.out.println(num + "是100的倍数,被放到仓库中了。");
                }
                num++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("生产者停止运行");
        }
    }
}

class Consumer {
    BlockingQueue<Integer> storage;
    
    public Consumer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }
    
    public boolean needMoreNums() {
        return !(Math.random() > 0.95);
    }
}