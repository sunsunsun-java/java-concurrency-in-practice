/**
 * @Description: synchronized的happens-before原则
 */
public class FieldVisibilityABCD {
    int a = 1;
    int b = 2;
    int c = 2;
    int d = 2;
    
    private void change() {
        a = 3;
        b = 4;
        c = 5;
        synchronized (this) {
            d = 6;
        }
    }
    
    
    private void print() {
        synchronized (this) {
            int aa = a;
        }
        int bb = b;
        int cc = c;
        int dd = d;
        
        System.out.println("b=" + b + ";a=" + a + ";c=" + c + ";d=" + d);
    }
    
    public static void main(String[] args) {
        FieldVisibilityABCD test = new FieldVisibilityABCD();
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
