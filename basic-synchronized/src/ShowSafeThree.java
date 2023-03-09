/**
 * @Description:
 */
public class ShowSafeThree implements Runnable {

    static ShowSafeThree r = new ShowSafeThree();
    static int i = 0;
    
    @Override
    public void run() {
        synchronized (ShowSafeThree.class) {
            for (int j = 0; j < 100000; j++)
                i++;
        }
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
