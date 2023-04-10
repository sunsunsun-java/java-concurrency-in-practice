package threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: ThreadLocal用法1：1000个打印日期的任务，用线程池来执行
 */
public class ThreadLocalNormalUsage00 {
    
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int a = i;
            threadPool.submit(() -> System.out.println(new ThreadLocalNormalUsage00().date(a)));
        }
        threadPool.shutdown();
    }
    
    public String date(int seconds) {
        Date date = new Date(1000 * seconds);
        // 可优化的地方
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
