/**
 * Two threads access the synchronized method of two objects?
 */
public class QuestionTwo implements Runnable {

    private static QuestionTwo instance1 = new QuestionTwo();
    private static QuestionTwo instance2 = new QuestionTwo();
    
    public  void method() {
        synchronized (this) {
            System.out.println("我是对象锁的方法修饰符形式。我叫" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "运行结束");
        }
    }
    
    @Override
    public void run() {
        method();
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(instance1);
        Thread t2 = new Thread(instance2);
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {
        
        }
        System.out.println("finished.");
    }
}
