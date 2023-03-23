package threadcoreknowledge.stopthread.volatiledemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Description: 用中断修复刚才的无尽等待的问题。
 */
public class WrongWayVolatileFixed {
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
        
        producerThread.interrupt();
    }
    
    static class Producer implements Runnable {
        BlockingQueue<Integer> storage;
        
        public Producer(BlockingQueue<Integer> storage) {
            this.storage = storage;
        }
        
        @Override
        public void run() {
            int num = 0;
            try {
                while (num <= 100000 && !Thread.currentThread().isInterrupted()) {
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
    
    static class Consumer {
        BlockingQueue<Integer> storage;
        
        public Consumer(BlockingQueue<Integer> storage) {
            this.storage = storage;
        }
        
        public boolean needMoreNums() {
            return !(Math.random() > 0.95);
        }
    }
}
