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

### The wrong way to stop

1.`stop()` `suspend()`  `resume()` abandoned methods

`StopThread.java`

`suspend()`  `resume()` cause dead lock

2.`volatile` set the `boolean` flag bit

`WrongWayVolatile.java` 

`WrongWayVolatileCannotStop.java`

If a thread blocks for a long time(for example - the case in the producer-consumer pattern), there is no way to wake it up in time, or it will never wake up.`interrupt` is designed to take long term blocking such as wait into account as a special case, so we should use `interrupt` to stop the thread. 

### Functions related to stopping threads

**`interrupt`**

Method to determine if an interruption has been made

- `static boolean interrupted()`
- `boolean isInterrupted()`

`Thread.interrupted()` target object

### Q：How to stop thread?

First at all, use `interrupt` requesting to stop a thread instead of forcing, this is safe.

Secondly, to stop a thread, the requesting party, the stopped party, and the submethod invoked party need to cooperate with each other to do so.

1. the requesting party - send an interrupt signal.
2. the stopped party - check for interrupt signals in each loop or when appropriate, and handle interrupt signals where an `InterrupedException` may be thrown.
3. the submethod invoked party (methods called by threads) -  priority is given to throwing an exception at the method level or setting the interrupt state again when an interrupt signal is checked.

At last, the wrong way to stop has `stop()/suspend()`, and `volatile` which sets the `boolean` flag bit.



## The state of the thread

<img src="C:\Users\sun\Desktop\Study-Notes\Java并发\技术图片\线程的6个状态.png" style="zoom:75%;" />

the state of block - BLOCKED、WAITING、TIMED_WAITING

**Attention**

1.When you make up from `Object.wait()` state, you usually can't grab the monitor lock immediately, so you  will go from `WAITING` state to `BLOCKED` state first, and then switch to `RUNNABLE` state after grabbing the lock.

2.If an exception occurs, you can jump directly to the `TERMINATED` state and do not have to follow the path.



## Important methods about thread in `Thread` and `Object`

### `Object`

#### `wait` `notify` `notifyAll`

Let threads rest temporarily and wake up.

**Blocking phase**

Until one of the following 4 conditions occurs, it will be awakened.

1. Another thread calls the `notify()` method of this object and it is this thread that happens to be woken up.
2. Another thread calls the `notifyAll()` method of this object.
3. After the timeout specified by the `wait(long timeout)` method of this object.(the parameter passed in 0 represents a permanent wait)
4. The thread itself calls `interrupt()`

**Wake-up phase**

**Interruption encounted**

`Wait4Object.java`

`WaitNotifyAll.java`

`WaitNotifyReleaseOwnMonitor.java`

**Nature**

- You must have the monitor lock before you can use it.
- The notification can only wake up one of them at random.
- Belong to the Object class.
- Similar functions are `Condition`

**Principle**

<img src="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\thread-core-fundamentals\wait原理.png" style="zoom:50%;" />

When you make up from `Object.wait()` state, you usually can't grab the monitor lock immediately, so you  will go from `WAITING` state to `BLOCKED` state first, and then switch to `RUNNABLE` state after grabbing the lock.

If an exception occurs, you can jump directly to the `TERMINATED` state and do not have to follow the path.

#### Common Interview Questions

1. Producer-Consumer Design Pattern

   `ProducerConsumerModel.java`

2. Use the program to implement two threads to alternately print the odd and even numbers from 0 to 100

   `WaitNotifyPrintOddEvenSyn.java` `WaitNotifyPrintOddEveWait.java`

3. Why does `wait()` need to be used within a synchronous code block, but `sleep()` does not?

   `synchronized` ensures that the `wait` method must be executed before the `notify`method.`sleep` is only for its own thread, not for other threads.

4. Why are the methods `wait()`, `notify()` and `notifyAll()` for thread communication defined in the `Object.java` class? And `sleep()` is defined in the `Thread.java` class?

    `wait()`, `notify()` and `notifyAll()` are lock-level operations where the lock belongs to an object, so the lock is bound to an object and not to a thread.If they were defined in threads, there would be limitations to their use.When a thread has multiple locks and the locks need to cooperate with each other, this definition does not meet the requirements. 

5. What happens if `Thread.wait()` is called?

    This will cause a problem.When the thread exits it will automatically call `notify`, which will interfere with your own design of the wake-up mechanism.So it is not recommended to use the `wait` method of Thread class.

### `Thread`

#### `sleep`

Only want the thread to execute at the expected time and not take CPU resurces at other times.

1. In addition, it does not release the lock(eg: `synchronized` `lock`).

   `SleepDontReleaseMonitor.java` `SleepDontReleaseLock.java`

2. it responds to interrupts(throw `InterruptedException` and clear interrupt status)

   `SleepInterrupted.java`

**Summary**

The `sleep` method allows the thread to enter the `WAITING` state and does not occupy CPU resources, but does not release the lock until after a specified time.If it is interrupted during sleep, an exception is thrown and the interrupted state is cleared.

**Q: What are the similarities and differences between the `wait()` and `sleep()` methods?**

The similarities

1. They can both block the thread, corresponding to the thread state `WAITING` or `TIME_WAITING`
2. They can both respond to interrupts `Thread.interrupt()`

The differences

1. The execution of the `wait()` method must be done in the synchronous method, while `sleep()` is not required.
2. The monitor lock is not released when the `sleep()` method is executed in the synchronous method, but the `wait()` method can release the monitor lock.
3. The `sleep()` method will voluntarily exit blocking after a short hibernation, while the `wait()` method, which has no specified time, needs to be interrupted by other threads before it can exit blocking. 
4. The `wait()` is a method of the `Object` class, but the `sleep()` is a method of the `Thread` class.

#### `join`

Wait for other threads to finish executing.

`JoinOne.java`

`JoinInterrupt.java`

`JoinThreadState.java`

**Principle**

`JoinPrinciple.java`

```java
thread.join();
==>
synchronized(thread) {
    thread.wait();
}
```

**Q: Which thread state is the thread in during the `join`**

#### `yield`

Give up the CPU resources already acquired.

The difference between `yield()` and `sleep()` methods - Is it possible to be scheduled again?

#### `currentThread`

Get a reference to the currently executing thread.

#### `start` `run`

Start-up related

#### `interrupt`

Interrupting threads

#### `stop` `suspend` `resuem`

Deprecated



## Properties of threads



## How to handle `UncaughtException` in threads



## Threads are double-edged swords



## Common Interview Questions
