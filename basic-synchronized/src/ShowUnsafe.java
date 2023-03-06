/**
 * @Description: two threads execute "a++" at the same time, the final result will be less than expected.
 */
public class ShowUnsafe implements Runnable {

    static ShowUnsafe r = new ShowUnsafe();
    static int i = 0;
    
    @Override
    public void run() {
        for (int j = 0; j < 100000; j++)
            i++;
    }
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
