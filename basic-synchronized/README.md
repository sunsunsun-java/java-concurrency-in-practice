# Synchronized

## Introduction

### role

It's guaranted that at most one thread executes the code at the same moment to ensure concurrency safety.



## Usage

### object lock

- method lock -- The default object lock is `this`, the currence object.
- synchronized code block lock -- specify the lock object yourself.

### class lock

**concept** -- `Java` class can have many objects, but only have one class object.

**attention** -- class lock can only be possessed by one object at the same moment.

- a static method decorated with `synchronized`.
- `Class object` is acted as a lock.

### <a id="questions">Question</a>

1. Two threads access the synchronized method of **an object** at the same time? **Safe**
2.  Two threads access the synchronized method of **two objects**? **Unsafe**
3. Two threads access **static methods** decorated with synchronized?**Safe**
4. Simultaneous access to synchronized and unsynchronized methods?**Unsafe**
5. Different common synchronization methods that access the **same object**?**Safe**
6. Simultaneous access to static synchronized and non-static synchronized methods?**Unsafe**
7. After the method throws an exception, the lock is released?**Yes**



## Properties

### Reentrant

This means that after the outer function of the same thread obtains the lock, the inner function directly obtains the lock again.

**advantages** -- avoiding deadlocks and enhancing encapsulation

### Non-interruptible

Once the lcok has been acquired by someone, if I still want to acquire it, I have no choice to wait or block until another thread releases the lock.If someone else never releases the lock, then I have to wait forever.



## Principles

### Adding and releasing lock

(Look at byte code)

**Time** -- enter and exit synchronous code blocks(include throw exception)

**Equivalence Code**  -- `synchronized` and `ReentrantLock`

**Look at byte code** -- **monitor-related commands**

take `Decompilation.java` as an example 

1. cd `absolute path of the file`
2. javac `Decompilation.java`
3. javap -verbose `Decompilation.class`

You can see two commands -- `monitorenter` and `monitorexit`

### Reentrant

**Lock counters**

`JVM` keeps track of the number of times a lock has been added.

The first time the lock is added, the number changes from 0 to 1, after that if the lock is added again, it changes from 1 to 2, and so on.

When exiting a layer of synchronous code block, the count is reduced by one, and when the count is 0, it means the lock is released.

### Ensure visibility

**Memory Model**

The result of one thread's execution is not necessarily visible to other threads.

Here, `synchronized` ensures visibility.



## Defects

- **Inefficient** -- Less lock release cases, attempting to obtain a lock cannot set a timeout and cannot interrupt a thread which is trying to obtain a lock.
-  **Inflexible** -- Single time to add and release locks.
- No way to know whether the lock was successfully acquired.



## Common Interview Questions

1. Points to note for use -- The range of the lock shouldn't be too large.Avoid nesting of locks.

2. How you choose `Lock` and `Synchronized` -- Prefer `Synchronized`

3. Various specifics of multi-threaded access to synchronous methods.

   [Question](#questions)



## Summary

`Synchronized`

`JVM` automatically adds an releases locks by using the monitor, ensures that only one thread can execute the specified code at the same time, ensures thread safety and is reentrant and non-interruptible in nature.
