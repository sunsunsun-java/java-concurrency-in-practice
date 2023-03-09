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

### Question

1. Two threads access the synchronized method of **an object** at the same time? **Safe**
2.  Two threads access the synchronized method of **two objects**? **Unsafe**
3. Two threads access **static methods** decorated with synchronized?**Safe**
4. Simultaneous access to synchronized and unsynchronized methods?**Unsafe**
5. Different common synchronization methods that access the **same object**?**Safe**
6. Simultaneous access to static synchronized and non-static synchronized methods?**Unsafe**
7. After the method throws an exception, the lock is released？**Yes**
