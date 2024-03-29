package atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

/**
 * @Description: 演示LongAccumulator的用法
 */
public class LongAccumulatorDemo {
    
    public static void main(String[] args) {
        LongAccumulator accumulator = new LongAccumulator(Long::sum, 0);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        IntStream.range(1, 10).forEach(i -> executor.submit(() -> accumulator.accumulate(i)));
        
        executor.shutdown();
        while (!executor.isTerminated()) {
        
        }
        System.out.println(accumulator.getThenReset());
    }
    
}
