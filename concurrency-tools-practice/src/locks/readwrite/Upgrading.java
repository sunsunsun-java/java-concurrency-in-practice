package locks.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description: 演示ReentrantReadWriteLock可以降级，不能升级
 */
public class Upgrading {
    
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);
    
}
