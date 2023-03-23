package threadcoreknowledge.sixstates;

/**
 * @Description: 展示线程的 NEW / RUNNABLE / TERMINATED 状态
 * 即使是正在运行，也是 RUNNABLE 状态，而不是 Running。
 */
public class NewRunnableTerminated implements Runnable {
    
    public static void main(String[] args) {
        Thread thread = new Thread(new NewRunnableTerminated());
        // 打印出NEW的状态
        System.out.println(thread.getState());
        thread.start();
        System.out.println(thread.getState());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 打印出RUNNABLE的状态
        System.out.println(thread.getState());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 打印出TERMINATED的状态
        System.out.println(thread.getState());
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
    }
}
