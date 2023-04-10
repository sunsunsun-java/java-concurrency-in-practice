package threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: ThreadLocal用法1：基于01的问题修复：加锁
 * 虽然解决了，但性能并不好
 */
public class ThreadLocalNormalUsage02 {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int a = i;
            threadPool.submit(() -> System.out.println(new ThreadLocalNormalUsage02().date(a)));
        }
        threadPool.shutdown();
    }
    
    public String date(int seconds) {
        Date date = new Date(1000 * seconds);
        String s = null;
        synchronized (ThreadLocalNormalUsage02.class) {
            s = format.format(date);
        }
        return s;
    }
}
