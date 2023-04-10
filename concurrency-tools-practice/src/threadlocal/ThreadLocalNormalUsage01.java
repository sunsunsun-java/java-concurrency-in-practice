package threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: ThreadLocal用法1：基于00的错误的优化方案
 */
public class ThreadLocalNormalUsage01 {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int a = i;
            threadPool.submit(() -> System.out.println(new ThreadLocalNormalUsage01().date(a)));
        }
        threadPool.shutdown();
    }
    
    public String date(int seconds) {
        Date date = new Date(1000 * seconds);
        return format.format(date);
    }
}
