# Synchronized

## Introduction

### role

It's guaranted that at most one thread executes the code at the same moment to ensure concurrency safety.



## Usage

### object lock

- method lock -- The default object lock is `this`, the currence object.
- synchronized code block lock -- specify the lock object yourself.

### class lock

- a static method decorated with `synchronized`.
- `Class object` is acted as a lock.

