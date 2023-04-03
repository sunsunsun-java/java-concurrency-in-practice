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



### `volatile` keyword



### Measures to ensure visibility



### The correct understanding of `synchronized` visibility

