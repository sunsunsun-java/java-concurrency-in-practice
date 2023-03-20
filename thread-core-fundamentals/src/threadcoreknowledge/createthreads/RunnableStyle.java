package threadcoreknowledge.createthreads;

/**
 * @Description: use 'Runnable' style realize thread
 */
public class RunnableStyle implements Runnable {
    @Override
    public void run() {
        System.out.println("use 'Runnable' style realize thread");
    }
    
    public static void main(String[] args) {
        Thread t = new Thread(new RunnableStyle());
        t.start();
    }
}
