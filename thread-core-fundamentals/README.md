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



## The state of the thread



## Important methods about thread in `Thread` and `Object`



## Properties of threads



## How to handle `UncaughtException` in threads



## Threads are double-edged swords



## Common Interview Questions
