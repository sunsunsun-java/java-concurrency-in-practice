package threadcoreknowledge.startthread;

/**
 * @Description: cannot call twice of the 'start' method
 */
public class CannotStartTwice {
    
    public static void main(String[] args) {
        Thread thread = new Thread();
        thread.start();
        thread.start();
    }
    
}
