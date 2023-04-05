/**
 * @Description: volatile适用的情况2
 */
public class UseVolatile2 {
    
    int a = 1;
    int c = 3;
    int d = 4;
    volatile int b = 2;
    
    private void change() {
        a = 3;
        c = 7;
        d = 8;
        b = 0;
    }
    
    private void print() {
        if (b == 0)
            System.out.println("b=" + b + ";a=" + a + ";c=" + c + ";d=" + d);
    }
    
    public static void main(String[] args) {
        UseVolatile2 test = new UseVolatile2();
        new Thread(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test.change();
        }).start();
    
        new Thread(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test.print();
        }).start();
        
    }
}
