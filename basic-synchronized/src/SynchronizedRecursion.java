/**
 *
 */
public class SynchronizedRecursion {
    
    int a = 0;
    
    public synchronized void method() {
        System.out.println("这是method, a = " + a);
        if (a == 0) {
            a++;
            method();
        }
    }
    
    public static void main(String[] args) {
        SynchronizedRecursion synchronizedRecursion = new SynchronizedRecursion();
        synchronizedRecursion.method();
    }
}
