package mycache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 最简单的缓存：HashMap
 */
public class Cache1 {

    private final Map<String, Integer> cache = new HashMap<>();

    public synchronized Integer computer(String userId) throws InterruptedException {
        Integer result = cache.get(userId);
        if (result == null) {
            result = doCompute(userId);
            cache.put(userId, result);
        }
        return result;
    }

    private Integer doCompute(String userId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return Integer.valueOf(userId);
    }

    public static void main(String[] args) throws InterruptedException {
        Cache1 cache1 = new Cache1();
        System.out.println("开始计算了");
        Integer result = cache1.computer("13");
        System.out.println("第一次计算结果：" + result);
        result = cache1.computer("13");
        System.out.println("第二次计算结果：" + result);
    }
}