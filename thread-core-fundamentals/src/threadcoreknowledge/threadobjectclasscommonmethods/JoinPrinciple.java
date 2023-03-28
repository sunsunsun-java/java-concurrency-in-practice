package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @Description: 通过讲解 join 原理，分析出 join 的代替写法
 */
public class JoinPrinciple {
    
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "执行完毕！");
        });
        
        thread.start();
        System.out.println("开始等待子线程运行完毕");
//        thread.join();
        
        // 等价代码
        synchronized (thread) {
            thread.wait();
        }
        // JVM Thread 最后都会调用 notifyAll() 全部唤醒
        // 这也是为什么不推荐使用 Thread.wait() 写法
        
        System.out.println("所有子线程执行完毕");
    }
}
