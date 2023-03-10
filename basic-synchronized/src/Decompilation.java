/**
 * @Description: Steps for decompiling
 * 1.cd 'absolute path of the file'
 * 2.javac Decompilation.java
 * 3.javap -verbose Decompilation.class
 */
public class Decompilation {

    private Object object = new Object();
    
    public void insert(Thread thread) {
        synchronized (object) {
        
        }
    }

}
