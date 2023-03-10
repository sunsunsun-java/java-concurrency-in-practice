/**
 * @Description:
 */
public class SynchronizedOtherMethod {
    
    public synchronized void method1() {
        System.out.println("这是method1");
        method2();
    }
    
    public synchronized void method2() {
        System.out.println("这是method2");
    }
    
    public static void main(String[] args) {
        SynchronizedOtherMethod method = new SynchronizedOtherMethod();
        method.method1();
    }
}
