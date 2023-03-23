package threadcoreknowledge.stopthread;

/**
 * @Description: 注意 Thread.interrupted() 方法的目标对象是"当前线程",而不管本方法来自于哪个对象
 */
public class RightWayInterrupted {
    
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                
                }
            }
        });
    
        // 启动线程
        thread.start();
        // 设置中断标志
        thread.interrupt();
        // 获取中断标志
        System.out.println("isInterrupted: " + thread.isInterrupted());
        // 获取中断标志并重置
        System.out.println("isInterrupted: " + thread.interrupted());
        // 获取中断标志并重置
        System.out.println("isInterrupted: " + Thread.interrupted());
        // 获取中断标志
        System.out.println("isInterrupted: " + thread.isInterrupted());
        System.out.println("Main thread is over.");
    }
}
