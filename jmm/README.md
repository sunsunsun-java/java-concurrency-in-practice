# JMM

## `JVM's Memory Structure` VS `Java's Memory Model` VS `Java's Object Model`

**Overall**

- `JVM's Memory Structure`  - related to **the runtime region** of the Java virtual machine.
- `Java's Memory Model` - related to the **concurrent programming** of the Java.
- `Java's Object Model` - related to **the representation** of Java objects **in the virtual machine**.

**`JVM's Memory Structure`**

<img src="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\jmm\JVM内存结构.png" alt="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\jmm" style="zoom:50%;" />

**`Java's Object Model`**

<img src="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\jmm\Java对象模型.png" style="zoom:50%;" />

`JVM` creates an `instanceKlass` for this class, which is stored in the method area and used to represent the Java class at the `JVM` level.

When an object is created using `new` in Java code, `JVM` creates an `instanceOopDesc` object, which contains the object header and instance data.

**`Java's Memory Model`**

 It's a set of specifications that require individual JVM implementations to adhere to JMM specifications so that developers can use them to more easily develop multi-threaded programs.

It's contains `atomicity`, `out-of-order-execution` and `visibility`.

## Out of Order Execution

**Advantages** - Improve execution efficiency

**Situation**:

- Compiler Optimization
- Command Out Of Order Execution
- "Out of Order Execution in memory" - The Out of Order Execution does not exist in-memory systems, but in-memory can have the same effect as Out of Order Execution. 

`OutOfOrderExecution.java`

## Visibility

### Why is there a visibility problem?

`FieldVisibility.java`

<img src="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\jmm\可见性问题.png" style="zoom:75%;" />

CPU has multiple levels of cache, causing read data to expire.

### JMM abstraction - main memory and local memory

<img src="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\jmm\主内存和本地内存.png" style="zoom:50%;" />

**JMM has the following provisions**

- All variables are stored in main memory, while each thread has its own separate working memory, and the contents of variables in working memory are copies of main memory.
- Threads cannot read or write variables in main memory directly, but can only manipulate variables in their own working memory before synchronizing them to main memory.
- Main memory is shared by multiple threads, but working memory is not shared between threads. If communication between threads is needed, it must be done with the help of main memory relay.

OverAll, all shared variables exist in main memory, each thread has its own local memory, and threads read and write shared data are also exchanged through local memory, which is what causes the visibility problem.

### Happens-Before Principles

If an operation happens-before another operation, then we say that the first operation is visible to the second operation.

#### Principles

1.Single-threaded principle

**2.Lock operation(`sychronized` and `Lock`)**

<img src="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\jmm\锁操作Happens-before1.png" style="zoom:50%;" />

<img src="C:\Users\sun\Desktop\homework\java-concurrency-in-practice\jmm\锁操作Happens-before2.png" style="zoom:50%;" />

**3.`volatile`**

4.Thread start

5.Thread `join`

6.Transmissibility

7.interrupt

8.the happens-before principle for tools

- `CountDownLatch`
- `Semaphore`
- `Future`
- `Thread Pool`
- `CyclicBarrier`



### `volatile` keyword

`volatile` is a synchronization mechanism that is lighter than `synchronized` or Lock-related classes because no overhead behavior such as context switching occurs with `volatile`.

If it's a variable that is modified by `volatile`, then the JVM knows that the variable may be modified concurrently.
Although `volatile` is used for synchronization to ensure thread safety, `volatile` **does not offer the same atomic protection as** `synchronized`.

**Not Applicable Scenarios**

`NoVolatile.java`

`NoVolatile2.java`

**Applicable Scenarios**

1. `boolean flag` (lf a shared variable is only assigned by individual threads and no other operations are performed, then `volatile` can be used instead of `synchronized` and other atomic variables because the assignment itself is atomic and `volatile` ensures visibility, so it is thread-safe.)

   `UseVolatile1.java`

2. As a trigger for refreshing previous variables.

   `UseVolatile2.java`

**Effects**

1. **Visibility** - Before reading a `volatile` variable, you need to invalidate the local cache and read the latest value in main memory.Writing a `volatile` variable will immediately flush it to main memory.
2. Prohibit command reordering optimization

**What is the relationship between `volatile`  and `synchronized`?**

`volatile` can be thought of as a lightweight version of `synchronized` - if a shared variable is only assigned by individual threads and no other operations are performed, then `volatile` can be used instead of `synchronized` because the assignment operation is atomic and `volatile` guarantees visibility, making it thread-safe.

**Summary**

1. The `volatile` modifier is suitable for scenarios where a property is shared by multiple threads and one of them modifies the property so that the other threads can immediately get the repaired value.
2. `volatile` property read and write operations are lock-free, it can not replace `synchronized`, because it does not provide atomicity and mutually exclusive body.
3. `volatile` provides the happens-before principle, where writes to a `volatile` variable `v` happens-before all subsequent reads for `v` by other threads.
4. `volatile` can make the assignment of `long` and `double` atomic.

### Measures to ensure visibility

1. In adition to `volatile`, which allows variables to guarantee visibility, `synchronized` / `Lock` / concurrent collections / `Thread.join()` / `Thread.start()` all guarantee visibility.
2. See happens-before principle

### The correct understanding of `synchronized` visibility

1. `synchronized` not only guarantees **atomicity**, but also **visibility**.
2. `synchronized` not only guarantees **code safety**, but also ensures **happens-before**.

## Atomicity

What are the atomic operations in Java?

1. Assignment operations for basic types(int / byte / boolean / short / char / float) other than long and double.
2. All assignment operations that refer to reference
3. Atomic operations for all classes in `java.concurrent.Atomic.*`

**Singleton**

`Singleton1.java`

`Singleton2.java`

`Singleton3.java`

`Singleton4.java`

`Singleton5.java`

`Singleton6.java`

`Singleton7.java`

`Singleton8.java`
