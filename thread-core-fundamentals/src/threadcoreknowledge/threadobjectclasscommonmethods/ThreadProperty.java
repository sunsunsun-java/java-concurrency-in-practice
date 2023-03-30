package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @Description:
 * 1.ID从 1 开始，并且JVM运行后，自己创建的线程ID已经更后面了
 */
public class ThreadProperty {
    public static void main(String[] args) {
        Thread threadId = new Thread();
        System.out.println("主线程的ID" + Thread.currentThread().getId());
        System.out.println("子线程的ID" + threadId.getId());
    
    }
}