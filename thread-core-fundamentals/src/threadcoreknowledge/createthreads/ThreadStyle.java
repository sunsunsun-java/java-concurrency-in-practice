package threadcoreknowledge.createthreads;

/**
 * @Description: use 'Thread' realize thread
 */
public class ThreadStyle extends Thread {
    
    @Override
    public void run() {
        System.out.println("use 'Thread' realize thread");
    }
    
    public static void main(String[] args) {
        new ThreadStyle().start();
    }
}
