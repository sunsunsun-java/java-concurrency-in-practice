package threadcoreknowledge.stopthread;

/**
 * @Description: 最佳实践：catch 了 InterruptedException 之后的
 * 优先选择 -- 在方法签名中抛出异常, 那么在 run() 就会强制 try/catch
 */
public class RightWayStopThreadInProd implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("go");
            try {
                throwInMethod();
            } catch (InterruptedException e) {
                System.out.println("保存日志");
                e.printStackTrace(); // 正确响应中断：保存日志、停止程序...
                break;
            }
        }
    }
    
    private void throwInMethod() throws InterruptedException {
        Thread.sleep(2000);
    }
    
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThreadInProd());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
