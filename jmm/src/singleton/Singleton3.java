package singleton;

/**
 * @Description: 懒汉式（线程不安全）（不推荐）
 */
public class Singleton3 {
    
    private static Singleton3 instance;
    
    private Singleton3() {
    
    }
    
    public static Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}
