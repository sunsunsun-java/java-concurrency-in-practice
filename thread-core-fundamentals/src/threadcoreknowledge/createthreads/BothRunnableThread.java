package threadcoreknowledge.createthreads;

/**
 * @Description: use two ways to realize thread at the same moment
 */
public class BothRunnableThread {
    
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I'm from 'Runnable'. ");
            }
        }) {
            @Override
            public void run() {
                System.out.println("I'm from 'Thread'. ");
            }
        }.start();
    }
    
}
