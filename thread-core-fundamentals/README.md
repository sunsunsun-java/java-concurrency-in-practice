# Thread-core-fundamentals

## Methods of implementing multi-threading

There are **two ways** to create a new thread of execution.

One is to declare a class to be a subclass of `Thread`.This subclass should override the `run` method of class `Thread`.

The other way to create a thread is to declare a class that implements the `Runnable` interface.That class then implements the `run` method.

**Comparison of the above two methods**

There is no difference between these two methods in the nature of their implementation of multi-threading.Both of them end up calling the `start()` method to create new threads.The main difference is in the source of the `run()` method.

```java
@Override
public void run() {
    if (target != null) {
        target.run();
    }
}
```

The first way is to **override the `run`** method.

The second way is to **implement the `run`** method, then call `target.run()` .

**Conclusion**

We can only create threads in one way by creating a new Thread class, but there are two ways to implement the `run()` method inside the class.The first one overrides the `run()` method, and the second one implements the `run()` method of the `Runnable` interface and then passes that `Runnable` instance to the Thread class.



## Methods for starting threads

`start()`  is what actually starts a thread, as if you call `run()`  directly, then it's just a normal method that has nothing to do with the thread's lifecycle.

What is the execution process of the `start()` method?

1. Check the status of threads. Only threads in the `NEW` state can continue, otherwise an `ILLegalThreadStateException` will be thrown.
2. Added to the thread group.
3. Call the `start0()` method to start the thread.

**Attention**

The `start()` method is a method modified by `synchronized`  to ensure thread safety.

The `main` method threads and `system` group threads created by the JVM are not started by  `start()`



## Methods for stopping threads

**principle - Use `interrupt` to notify, not to force.**

In Java, the best way to stop a thread is to use `interrupt` , but this only notifies the terminated thread that "you should stop running", the terminated thread itself has the right to decide(if and when to stop), depending on both the requesting and the terminated party adhering to an agreed coding specification.

Java doesn't provide any mechanism to safely terminate threads. But it does provide `interrupt()`, a collaborative mechanism that enables one thread to terminate the current work of another thread.

### The right way to stop - `interrupt`

1.When is a thread usually stopped in the ordinary case?

`RightWayStopThreadWithoutSleep.java`

2.Threads may be blocked.

`RightWayStopThreadWithSleep.java`

3.If a thread blocks after each iteration.

`RightWayStopThreadWithSleepEveryLoop.java`

4.The problem of `try/catch` within `while` 

`CannotInterrupt.java`

5 .Two best practices in practical development

- **Preferences - Passing Interrupts** `RightWayStopThreadInProd.java`
- **Don't want to  do or can't pass InterruptedException - Recovery interruption **`RightWayStopThreadInProd2.java`

Another, Interrupts should not be blocked.

6.Summary list of methods for responding to interrupts

- `Object.wait()/wait(long)/wait(long, int)`
- `Thread.sleep(long)/slepp(long, int)`
- `Thread.join()/join(long)/join(long, int)`
- `java.util.concurrent.BlockingQueue.take()/put(E)`
- `java.util.concurrent.locks.Lock.lockInterruptibly()`
- `java.util.concurrent.CountDownLatch.await()`
- `java.util.concurrent.CyclicBarrier.exchange(V)`
- `java.nio.channels.InterruptibleChannel` Related methods
- `java.nio.channels.Selector` Related methods





## The state of the thread



## Important methods about thread in `Thread` and `Object`



## Properties of threads



## How to handle `UncaughtException` in threads



## Threads are double-edged swords



## Common Interview Questions
