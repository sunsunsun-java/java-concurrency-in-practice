package threadcoreknowledge.threaddoubleedged;

/**
 * @Description: 逸出：初始化未完毕，就this赋值
 */
public class MultiThreadError3 {
    
    static Point point;
    
    public static void main(String[] args) throws InterruptedException {
        new PointMaker().start();
//        Thread.sleep(10);
        Thread.sleep(105);
        if (point != null) {
            System.out.println(point);
        }
    }
    
}

class Point {
    
    private final int x, y;
    
    public Point(int x, int y) throws InterruptedException {
        this.x = x;
        MultiThreadError3.point = this;
        Thread.sleep(100);
        this.y = y;
    }
    
    @Override
    public String toString() {
        return x + "," + y;
    }
}

class PointMaker extends Thread {
    
    @Override
    public void run() {
        try {
            new Point(1, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}