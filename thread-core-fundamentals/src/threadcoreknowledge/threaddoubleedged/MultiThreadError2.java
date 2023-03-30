package threadcoreknowledge.threaddoubleedged;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 第三种逸出：方法返回一个private对象
 */
public class MultiThreadError2 {
    private Map<String, String> states;
    
    public MultiThreadError2() {
        states = new HashMap<>();
        states.put("1", "周一");
        states.put("2", "周二");
        states.put("3", "周三");
        states.put("4", "周四");
    }
    
    public Map<String, String> getStates() {
        return states;
    }
    
    public Map<String, String> getStatesImproved() {
        return new HashMap<>(states);
    }
    
    public static void main(String[] args) {
        MultiThreadError2 multiThreadsError3 = new MultiThreadError2();
        Map<String, String> states = multiThreadsError3.getStates();
//        System.out.println(states.get("1"));
//        states.remove("1");
//        System.out.println(states.get("1"));
        
        System.out.println(multiThreadsError3.getStatesImproved().get("1"));
        multiThreadsError3.getStatesImproved().remove("1");
        System.out.println(multiThreadsError3.getStatesImproved().get("1"));
    
    }
}
