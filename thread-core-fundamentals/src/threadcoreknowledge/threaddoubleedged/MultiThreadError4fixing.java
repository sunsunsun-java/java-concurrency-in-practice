package threadcoreknowledge.threaddoubleedged;

/**
 * @Description: 用工厂模式修复刚才的初始化问题
 */
public class MultiThreadError4fixing {
    int count;
    private EventListener listener;
    
    private MultiThreadError4fixing(MySource source) {
        listener = new EventListener() {
            @Override
            public void onEvent(MultiThreadError4fixing.Event e) {
                System.out.println("\n我得到的数字是" + count);
            }
            
        };
        for (int i = 0; i < 10000; i++) {
            System.out.print(i);
        }
        count = 100;
    }
    
    public static MultiThreadError4fixing getInstance(MySource source) {
        MultiThreadError4fixing safeListener = new MultiThreadError4fixing(source);
        source.registerListener(safeListener.listener);
        return safeListener;
    }
    
    public static void main(String[] args) {
        MySource mySource = new MySource();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mySource.eventCome(new MultiThreadError4fixing.Event() {
                });
            }
        }).start();
        MultiThreadError4fixing multiThreadsError7 = new MultiThreadError4fixing(mySource);
    }
    
    static class MySource {
        
        private EventListener listener;
        
        void registerListener(EventListener eventListener) {
            this.listener = eventListener;
        }
        
        void eventCome(MultiThreadError4fixing.Event e) {
            if (listener != null) {
                listener.onEvent(e);
            } else {
                System.out.println("还未初始化完毕");
            }
        }
        
    }
    
    interface EventListener {
        
        void onEvent(MultiThreadError4fixing.Event e);
    }
    
    interface Event {
    
    }
}
