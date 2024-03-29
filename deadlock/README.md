# DeadLock

## What?

Occurs in concurrency.

Not giving way to each other - Deadlock occurs when multiple threads hold resources needed by each other and do not actively release them, causing the program to fall into endless blocking.

## Necessary conditions

1. Mutual exclusion condition
2. Request and hold conditions
3. No deprivation conditions
4. Cyclic waiting conditions

## Case

`MustDeadLock.java`

`TransferMoney.java`

`MultiTransferMoney.java`

## Check Deadlock

`ThreadMXBeanDetection.java`

## Restoration Strategy

1. Avoidance Strategies - Philosophers dining change hands program and transfer change order program.
2. Detection recovery strategy - Check for deadlocks once in a while, and if so, strip a resource to release the deadlock.
3. ostrich strategy - sticking one's head in the sand.

## How to avoid deadlocks?

1. Set timeout time
2. Use more concurrent classes
3. Minimize the granularity of the use of locks
4. Try to use synchronous code blocks and specify the lock object yourself
5. Give meaningful names to threads
6. Avoid nesting of locks
7. Try not to use the same lock for several functions - special lock for special use

# LiveLock

Although the threads are not blocking and are always running, the program is not progressing because the threads keep doing the same thing over and over again.

# Starvation

When a thread needs certain resources, but never gets it.
