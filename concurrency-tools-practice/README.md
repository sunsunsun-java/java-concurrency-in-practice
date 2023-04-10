# JUC

## 线程池 -- 治理线程的法宝

### 线程池的自我介绍

- 如果不使用线程池，每个任务都新开一个线程处理。这样开销太大，我们希望有固定数量的线程，来执行很多线程，这样就避免反复创建并销毁线程所带来的的开销问题。线程池就这样出现了
- 作用：线程池能够对线程进行统一分配，调优和监控。

	- 降低资源消耗(线程无限制地创建，然后使用完毕后销毁)
	- 提高响应速度(无须创建线程)
	- 提高线程的可管理性

### 创建和停止线程池

- 线程池构造函数的参数

	- (int)corePoolSize:核心线程数
	- (int)maxPoolSize:最大线程数
	- (long)keepAliveTime:保持存活时间：如果线程池当前的线程数多于corePoolSize，那么如果多余的线程空闲时间超过KeepAliveTime，它们就会被终止。
	- (BlockingQueue)workQueue:任务存储队列

		- 直接交接：SynchronousQueue
		- 无界队列：LinkedBlockingQueue
		- 有界的队列：ArrayBlockingQueue

	- (ThreadFactory)threadFactory:当线程池需要新的线程时，会使用threadFactory来生成新的线程；创建出来的线程都在同一个线程组，拥有同样的NORM_PRIOITY优先级，并且都不是守护线程。如果自己指定ThreadFactory,那么可以定制线程名、线程组、优先级、是否是守护线程等。通常用默认就够了。
	- (Handler)RejectedExecutionHandler:由于线程池无法接收你所提交的任务的拒绝策略
	- 添加线程规则

		- 1.如果线程数小于corePoolSize，即使其他工作线程处于空闲状态，也会创建一个新线程来运行新任务。
		- 2.如果线程等于（或大于）corePoolSize但少于maxPoolSize，则将任务放入队列。
		- 3.如果队列已满，并且线程数小于maxPoolSize，则创建一个新线程来运行任务。
		- 4.如果队列已满，并且线程大于等于maxPoolSize，则拒绝该任务。
		- 流程图说明

	- 增减线程的特点

		- 通过设置corePoolSize和maxPoolSize相同，就可以创建固定大小的线程池。
		- 线程池希望保持较少的线程数，并且只有在负载变得很大时才增加它。
		- 通过设置maxPoolSize为很高的值，比如Integer.MAX_VALUE，可以允许线程池容纳任务数量的并发任务。
		- 只有在队列填满时才创建多于corePoolSize的线程，所以你使用的无界队列（LinkedBlockingQueue），那么线程数就不会超过corePoolSize。

- 线程池应该手动创建还是自动创建

	- 手动创建更好，因为这样可以让我们更加明确线程池的运行规则，避免资源耗尽的风险。
	- 自动创建线程池（即直接调用JDK封装好的方法）可能带来的哪些问题

		- Executors.newFixedThreadPool(固定线程数)

		  传的是LinkedBlockingQueue是没有容量上限的，所以当请求数越来越多，并且无法及时处理完毕的时候，也就是请求堆积的时候，会容易造成占用大量的内存，可能会导致OOM。

		- Executors.newSingleThreadExecutor()

		  和newFixedThreadPool的原理基本一样，只不过是吧线程数直接设置成了1，所以这也出现跟上面一样的问题。

		- Executors.newCachedThreadPool(...)

		  public static ExecutorService newCachedThreadPool() {
		  	return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		  }
		  
		  第二个参数为Integer.MAX_VALUE，可能会创建非常多的线程，甚至可能导致OOM。

			- 可缓存线程池。具有自动回收多余线程的能力

		- Executors.newScheduledThreadPool(..)

			- 支持定时及周期性任务执行的线程池

		- 四个创建线程池的参数说明图
		- 阻塞队列分析

			- FixedThreadPool和SingleThreadExecutor的Queue是LinkedBlockingQueue?因为corePoolSize、corePoolSize是固定的，选择无界队列能解决大量任务到来时的处理
			- CachedThreadPool使用的Queue是SynchronousQueue?因为corePoolSize是0，新任务来就直接创建，并不需要队列存储
			- ScheduledThreadPool来说，使用时延时队列DelayedWorkQueue.

- 线程池里的线程数量设定为多少比较合适？

	- CPU密集型（加密、计算hash等）：最佳线程数为CPU核心数的1-2倍左右。
	- 耗时IO型（读写数据库、文件、网络读写等）：最佳线程数一般会大于cpu核心数很多倍，以JVM线程监控显示繁忙情况为依据，保证线程空闲可以衔接上。
	- 理论核心线程数：CPU核心数 * (1 + 平均等待时间 / 平均工作时间)

- 停止线程池的正确方法

	- shutdown:：会将线程池的状态设置为SHUTDOWN,线程池进入这个状态后,就拒绝再接受任务,然后会将剩余的任务全部执行完
	- isShutdown：调用shutdown方法后，这个方法返回值就是true
	- isTerminated：调用shutdown/shutdownNow之后，如果任务全部执行完毕就返回true
	- awaitTermination：调用终止线程方法后，如果在指定时间内任务执行完，这返回true；否则，返回false
	- shutdownNow：将线程池状态设置为STOP，然后拒绝所有提交的任务。最后中断正在运行中的worker,然后清空任务队列。并返回任务队列中的所有任务，故使用该方法需注意处理返回值。

### 任务太多，怎么拒绝？

- AbortPolicy
- DiscardPolicy
- DiscardOldestPolicy
- CallerRunsPolicy

### 钩子方法，源码分析

### 线程池的五大状态

- RUNNING：接收新任务并处理排队任务
- SHUTDOWN：不接受新任务，但处理排队任务，并中断正在进行的任务
- STOP：不接受新任务，也不处理排队任务，并中断正在进行的任务
- TIDYING：所有任务都已终止，workerCount为零时，线程会转换到TIDYING状态，并将运行terminate()钩子方法。
- TERMINATED：terminate()运行完成

### 使用线程池的注意点

- 避免任务堆积
- 避免线程数过度增加
- 排查线程泄露

### 面试问题

- 为什么要有线程池？
- Java是实现和管理线程池有哪些方式？请简要举例说明如何使用。
- 为什么很多公司不允许使用Executors去创建线程池? 那么推荐怎么使用呢?
- ThreadPoolExecutor有哪些核心的配置参数? 请简要说明。
- ThreadPoolExecutor可以创建哪三种线程池呢?
- 当队列满了并且worker的数量达到maxSize的时候，会怎么样?
- 说说ThreadPoolExecutor有哪些RejectedExecutionHandler策略? 默认是什么策略?
- 简要说下线程池的任务执行机制? execute –> addWorker –>runworker (getTask)
- 线程池中任务是如何提交的?
- 线程池中任务是如何关闭的?
- 在配置线程池的时候需要考虑哪些配置因素?
- 如何监控线程池的状态?

## ThreadLocal

### 两大使用场景

- 典型场景1：每个线程需要一个独享的对象（通常是工具类，典型需要使用的类有SimpleDateFormat和Random）
- 典型场景2：每个线程内需要保存全局变量（例如在拦截器中获取用户信息），可以让不同方法直接使用，避免参数传递的麻烦。

### 第二使用场景

- 图例
- 用ThreadLocal保存一些业务内容（用户权限信息、从用户系统获取到的用户名、user ID等）
- 这些信息在同一个线程内相同，但是不同的线程使用业务内容是不相同的
- 在线程周期内，都通过这个静态ThreadLocal实例的get()方法取得自己set过的对象，避免了将这个对象（例如用户对象）作为参数传递的麻烦
- 强调的是同一请求内（同一个线程内）不同方法间的共享

### ThreadLocal的两个作用

- 1.让某个需要用到的对象在线程间隔离（每个线程都有自己的独立的对象）
- 2.在任何方法中都可以轻松获取到该对象

### ThreadLocal带来的好处

- 达到线程安全
- 不需要加锁，提高执行效率
- 更高效地利用内存、节省开销：相比于每个任务都新建一个SimpleDateFormat，显然用ThreadLocal可以节省内存和开销
- 免去传参的繁琐：无论是场景一的工具类，还是场景二的用户名，都可以在任何地方直接通过ThreadLocal拿到，再也不需要每次都传同样的参数。ThreadLocal使得代码耦合度更低，更优雅。

### ThreadLocal源码分析

- Thread、ThreadLocal、ThreadLocalMap三者的关系
- 主要方法介绍

	- T initialValue()：初始化

		- 1.该方法会返回当前线程对应的“初始值”，这是一个延迟加载的方法，只有在调用get的时候，才会触发；
		- 2.当线程第一次使用get方法访问变量时，将调用此方法，除非线程先前调用了set方法，在这种情况下，不会为线程调用本initialValue方法
		- 3.通常，每个线程只需要调用一次此方法，但如果已经调用了remove()后，再调用get()，则可以再次调用此方法
		- 如果不重写本方法，这个方法会返回null。一般使用匿名内部类/拉姆拉表达式来重写initialValue()方法，以便在后续使用中可以初始化副本对象。

	- void set(T t)：为这个线程设置一个新值
	- T get()：得到这个线程对应的value。如果首次调用get()，则会调用initialize来得到这个值
	- void remove()：删除对应这个线程的值

- 方法的分析

	- get方法

		- get方法是先取出当前线程的ThreadLocalMap，然后调用map.getEntry方法，把本ThreadLocal的引用作为参数传入，取出map中属于本ThreadLocal的value
		- 注意：这个map以及map中的key和value都是保存在线程中的，而不是保存在ThreadLocal中

	- initialValue方法

		- 没有默认实现的，如果我们要用到initialValue方法，需要自己实现，通常是匿名内部类的方式

	- remove方法

	  public void remove() {
	      ThreadLocalMap m = getMap(Thread.currentThread());
	      if (m != null)
	          m.remove(this);
	  }

		- 删除当前线程的ThreadLocalMap中的当前ThreadLocal

	- ThreadLocalMap 类

		- 在Thread.threadLocals中
		- ThreadLocalMap 类是每个线程Thread类里面的变量，里面最重要的是一个键值对数组Entry[] table，可以认为是一个map，键值对：键是这个ThreadLocal；值：实际需要的成员变量，比如user或者SimpleDateFormat对象
		- 解决哈希冲突的方式不同

			- Java8 HashMap结构
			- ThreadLocalMap采用的是线性探测法，也就是如果发生冲突，就继续找下一个空位置，而不是用链表拉链

### 两种使用场景殊途同归

- 通过源码分析可以看出，setInitialValue和直接set最后都是利用map.set()方法来设置值
- 也就是说，最后都会对应到ThreadLocalMap的一个Entry，只不过是起点和入口不一样。

### 内存泄露：收不回的value

- 是指某个对象不再有用，但是占用的内存却不能被回收
- Key的泄漏：ThreadLocalMap中的Entry继承自WeakReference，是弱引用

  弱引用的特点：如果这个对象只被弱引用关联（没有任何强引用关联），那么这个对象就可以被回收。
  所以弱引用不会阻止GC。

- Value的泄漏

	- ThreadLocalMap 的每个 Entry 都是一个对key的弱引用，同时，每个 Entry 都包含了一个对 value 的强引用。
	- 正常情况下，当线程终止，保存在 ThreadLocal 里的 value 会被垃圾回收，因为没有任何强引用了。
	- 但是，如果线程不终止（比如线程持续的时间很久），那么 key 对应的 value 就不能被回收，因为存在一个调用链：Thread -> ThreadLocalMap -> Entry(key 为 null) -> Value

- 因为 value 和 Thread 之间还存在这个强引用链路，所以导致 value 无法回收，就可能会出现 OOM
- JDK 考虑到这个问题，所在在set、remove、rehash方法中会判断 key == null 的 Entry，就会把对应的 value 设置为 null，这样 value 对象就可以被回收
- 但是如果一个 ThreadLocal 不被使用，那么我们很难主动调用 set、remove、rehash方法。如果同时线程又不停止，那么调用链就一致存在，那么就导致了 value 的内存泄漏

### 如何避免内存泄漏

- 主动调用remove方法，就会删除对应的 Entry 对象，可以避免内存泄漏。

### 注意点

- 空指针异常：在进行get之前，必须先set，否则可能会报空指针异常？
- 共享对象：如果每个线程中ThreadLocal.set()进去的东西本来就是多线程共享的同一个对象，比如static对象，那么多个线程的ThreadLocal.get()取得的还是共享对象本身，还是存在并发访问问题。
- 如果可以不使用ThreadLocal就解决问题，那么不要强行使用。比如在任务数很少的时候，在局部变量中可以新建对象就可以解决问题，就不需要使用到 ThreadLocal
- 优先使用框架的支持，而不是自己创建

### 面试问题

- 什么是ThreadLocal？用来解决什么问题的？

  ThreadLocal是一个将在多线程中为每个线程创建单独的变量副本的类；当使用ThreadLocal来维护变量时，ThreadLocal会为每个线程创建单独的变量副本，避免因多线程操作共享变量而导致的数据不一致的情况。

- 说说你对ThreadLocal的理解
- ThreadLocal是如何实现线程隔离的？
- 为什么ThreadLocal会造成内存泄漏？如何解决？
- 还有哪些使用ThreadLocal的应用场景？

## Java 中锁的分类

### 线程要不要锁住同步资源

- 为什么会诞生非互斥同步锁。即互斥同步锁的劣势

	- 阻塞和唤醒带来的性能劣势
	- 永久阻塞：如果持有锁的线程被永久阻塞，比如遇到了无限循环、死锁等活跃性问题，那么等待该线程释放锁的那几个线程，将永远也得不到执行
	- 优先级反转

- 锁住：悲观锁

  如果我不锁住这个资源，别人就回来争抢，就会造成数据结果错误，所以每次悲观锁为了确保结果的正确性，会在每次获取并修改数据时，把数据锁住，让别人无法访问该数据，这样可以确保数据内容万无一失。
  
  代表：synchronized和lock接口

- 不锁住：乐观锁

  认为自己在处理操作的时候不会有其他线程来干扰，所以并不会锁住被操作的对象。
  在更新的时候，去对比在我修改的期间数据有没有被其他人改变过，如果没被改变过，就说明真的是只有我自己在操作，那我就正常修改数据；如果数据和我一开始拿到的不一样了，说明其他人在这段时间内改过数据，那我们就不能继续刚才的更新数据过程了，可以选择放弃、报错、重试等策略。
  
  乐观锁的实现一般都是利用CAS算法来实现的。
  典型例子：原子类、并发容器等。

- 两者的对比

	- 典型例子

		- Git版本
		- 数据库

			- select for update就是悲观锁
			- version版本号来实现乐观锁

	- 开销对比

		- 悲观锁的原始开销要高于乐观锁，但特点是一劳永逸，临界区持锁时间就算越来越差，也不会对互斥锁的开销造成影响
		- 相反，虽然乐观锁一开始的开销比悲观锁小，但是如果自旋时间很长或者不停重试，那么消耗的资源也会越来越多

	- 各自使用场景

		- 悲观锁：适合并发写入多的情况，适用于临界区持锁时间比较长的情况，悲观锁可以避免大量的无用自旋等消耗，典型情况：

			- 1.临界区有IO操作
			- 2.临界区代码复杂或者循环量大
			- 3.临界区竞争非常激烈

		- 乐观锁：适合并发写入少，大部分是读取的场景，不加锁的能让读取性大幅提高

### 多线程能否共享一把锁

- 可以：共享锁

  又称读锁，获得共享锁之后，可以查看但无法修改和删除数据，其他线程此时也可以获取到共享锁，也可以查看但无法修改和删除数据。
  
  共享锁和排它锁的典型是读写锁ReentrantReadWriteLock，其中读锁是共享锁，写锁是独享锁。

- 不可以：独占锁

  又称排它锁，独享锁

- 读写锁

	- 作用

	  在没有读写锁之前，假设使用ReentrantLock，虽然保证了线程安全，但是也浪费了一定的资源：多个读操作同时进行，并没有线程安全问题。
	  在读的地方使用读锁，在写的地方使用写锁，灵活控制，如果没有写锁的情况下，读是无阻塞的，提高了程序的执行效率。

	- 规则

		- 1.多个线程只申请读锁，都可以申请到
		- 2.如果有一个线程已经占用了读锁，此时其他线程如果要申请写锁，则申请写锁的线程会一直等待释放读锁
		- 3.如果有一个线程已经占用了写锁，此时其他线程如果要申请写锁或读锁，则申请的线程会一直等待释放写锁。
		- 总结：要么多读，要么一写

	- 案例

	  public class CinemaReadWrite {
	      
	      private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
	      private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
	      private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
	      
	      private static void read() {
	          readLock.lock();
	          try {
	              System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
	              Thread.sleep(1000);
	          } catch (InterruptedException e) {
	              e.printStackTrace();
	          } finally {
	              System.out.println(Thread.currentThread().getName() + "释放锁");
	              readLock.unlock();
	          }
	      }
	      
	      private static void write() {
	          writeLock.lock();
	          try {
	              System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
	              Thread.sleep(1000);
	          } catch (InterruptedException e) {
	              e.printStackTrace();
	          } finally {
	              System.out.println(Thread.currentThread().getName() + "释放写锁");
	              writeLock.unlock();
	          }
	      }
	      
	      public static void main(String[] args) {
	          new Thread(CinemaReadWrite::read, "Thread1").start();
	          new Thread(CinemaReadWrite::read, "Thread2").start();
	          new Thread(CinemaReadWrite::write, "Thread3").start();
	          new Thread(CinemaReadWrite::write, "Thread4").start();
	      }
	  }

	- 实现方式

		- 读锁插队策略

			- 非公平：假设线程2和线程4正在同时读取，线程3想要写入，拿不到锁，于是进入等待队列，线程5不再队列里，现在过来想要读取
			- 上面情况有两种策略

				- 策略一：容易造成写锁饥饿
				- 策略二：降低了读锁的一点效率，但确保程序的正确性

					- 图一
					- 图二

			- 总结

				- 公平锁：不允许插队
				- 非公平锁

					- 写锁可以随时插队
					- 读锁仅在等待队列头结点不是想要获取写锁的线程的时候可以插队

				- 代码演示：1.读不插队；2.读实际上可以插队

				  /**
				   * @Author: 孙宏浩
				   * @CreateTime: 2022-05-01 15:27
				   * @Description: 演示非公平的ReentrantReadWriteLock的策略
				   */
				  public class NonfairBargeDemo {
				      
				      private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
				      private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
				      private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
				      
				      private static void read() {
				          System.out.println(Thread.currentThread().getName() + "开始尝试获取读锁");
				          readLock.lock();
				          try {
				              System.out.println(Thread.currentThread().getName() + "得到读锁，正在读取");
				              try {
				                  Thread.sleep(20);
				              } catch (InterruptedException e) {
				                  e.printStackTrace();
				              }
				          } finally {
				              System.out.println(Thread.currentThread().getName() + "释放读锁");
				              readLock.unlock();
				          }
				      }
				      
				      private static void write() {
				          System.out.println(Thread.currentThread().getName() + "开始尝试获取写锁");
				          writeLock.lock();
				          try {
				              System.out.println(Thread.currentThread().getName() + "得到写锁，正在写入");
				              try {
				                  Thread.sleep(40);
				              } catch (InterruptedException e) {
				                  e.printStackTrace();
				              }
				          } finally {
				              System.out.println(Thread.currentThread().getName() + "释放写锁");
				              writeLock.unlock();
				          }
				      }
				      
				      public static void main(String[] args) {
				          new Thread(NonfairBargeDemo::write,"Thread1").start();
				          new Thread(NonfairBargeDemo::read,"Thread2").start();
				          new Thread(NonfairBargeDemo::read,"Thread3").start();
				          new Thread(NonfairBargeDemo::write,"Thread4").start();
				          new Thread(NonfairBargeDemo::read,"Thread5").start();
				          new Thread(() -> {
				              Thread[] thread = new Thread[1000];
				              for (int i = 0; i < 1000; i++) {
				                  thread[i] = new Thread(NonfairBargeDemo::read, "子线程创建的Thread" + i);
				              }
				              for (int i = 0; i < 1000; i++) {
				                  thread[i].start();
				              }
				          }).start();
				      }
				  }

		- 升降级

			- 为什么需要升降级
			- 支持锁的降级，不支持升级：代码演示

			  /**
			   * @Author: 孙宏浩
			   * @CreateTime: 2022-05-01 16:10
			   * @Description: 演示ReentrantReadWriteLock可以降级，不能升级
			   */
			  public class Upgrading {
			      private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(
			          false);
			      private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
			      private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
			      
			      private static void readUpgrading() {
			          readLock.lock();
			          try {
			              System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
			              Thread.sleep(1000);
			              System.out.println("升级会带来阻塞");
			              writeLock.lock();
			              System.out.println(Thread.currentThread().getName() + "获取到了写锁，升级成功");
			          } catch (InterruptedException e) {
			              e.printStackTrace();
			          } finally {
			              System.out.println(Thread.currentThread().getName() + "释放读锁");
			              readLock.unlock();
			          }
			      }
			      
			      private static void writeDowngrading() {
			          writeLock.lock();
			          try {
			              System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
			              Thread.sleep(1000);
			              readLock.lock();
			              System.out.println("在不释放写锁的情况下，直接获取读锁，成功降级");
			          } catch (InterruptedException e) {
			              e.printStackTrace();
			          } finally {
			              readLock.unlock();
			              System.out.println(Thread.currentThread().getName() + "释放写锁");
			              writeLock.unlock();
			          }
			      }
			      
			      public static void main(String[] args) throws InterruptedException {
			          System.out.println("先演示降级是可以的");
			          Thread thread1 = new Thread(() -> writeDowngrading(), "Thread1");
			          thread1.start();
			          thread1.join();
			          System.out.println("------------------");
			          System.out.println("演示升级是不行的");
			          Thread thread2 = new Thread(() -> readUpgrading(), "Thread2");
			          thread2.start();
			      }
			  }

			- 为什么不支持锁的升级？死锁
			- 实际锁降级案例

			  在锁降级成功后，也就是持有写锁的时候同时申请并获得了读锁后，此时直接释放写锁，但是不释放读锁，这样就可以提高锁的利用效率，下面这段代码演示了在更新缓存的时候，如何利用锁的降级功能。
			  /**
			   * @Author: 孙宏浩
			   * @CreateTime: 2022-05-01 16:17
			   * @Description: 实际锁降级的例子
			   */
			  public class CachedData {
			      
			      Object data;
			      volatile boolean cacheValid;
			      final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
			      
			      void processCachedData() {
			          rwl.readLock().lock();
			          if (!cacheValid) {
			              // 发现缓存失效，那么就需要写入了，所以现在需要获取写锁，由于锁不支持升级，所以在获取写锁之前，必须首先释放读锁。
			              rwl.readLock().unlock();
			              // 获取到写锁
			              rwl.writeLock().lock();
			              try {
			                  // 这里需要再次判断数据的有效性,因为在我们释放读锁和获取写锁的空隙之内，可能有其他线程修改了数据。
			                  if (!cacheValid) {
			                      data = new Object();
			                      cacheValid = true;
			                  }
			                  // 在不释放写锁的情况下，直接获取读锁，这就是读写锁的降级。
			                  rwl.readLock().lock();
			              } finally {
			                  // 释放了写锁，但是依然持有读锁，这样一来，就可以多个线程同时读取了，提高了整体效率。
			                  rwl.writeLock().unlock();
			              }
			          }
			      }
			  }
			  在这段代码中有一个读写锁，最重要的就是中间的 processCachedData 方法，在这个方法中，会首先获取到读锁，也就是 rwl.readLock().lock()，它去判断当前的缓存是否有效，如果有效那么就直接跳过整个 if 语句，如果已经失效，代表我们需要更新这个缓存了。由于我们需要更新缓存，所以之前获取到的读锁是不够用的，我们需要获取写锁。
			  
			  在获取写锁之前，我们首先释放读锁，然后利用 rwl.writeLock().lock() 来获取到写锁，然后是经典的 try finally 语句，在 try 语句中我们首先判断缓存是否有效，因为在刚才释放读锁和获取写锁的过程中，可能有其他线程抢先修改了数据，所以在此我们需要进行二次判断。
			  
			  如果我们发现缓存是无效的，就用 new Object() 这样的方式来示意，获取到了新的数据内容，并把缓存的标记位设置为 ture，让缓存变得有效。由于我们后续希望打印出 data 的值，所以不能在此处释放掉所有的锁。我们的选择是在不释放写锁的情况下直接获取读锁，也就是 rwl.readLock().lock() 这行语句所做的事情，然后，在持有读锁的情况下释放写锁，最后，在最下面的 try 中把 data 的值打印出来。
			  这就是一个非常典型的利用锁的降级功能的代码。
			  
			  你可能会想，我为什么要这么麻烦进行降级呢？我一直持有最高等级的写锁不就可以了吗？这样谁都没办法来影响到我自己的工作，永远是线程安全的。
			  
			  为什么需要锁的降级？
			  
			  如果我们在刚才的方法中，一直使用写锁，最后才释放写锁的话，虽然确实是线程安全的，但是也是没有必要的，因为我们只有一处修改数据的代码：
			  
			  data = new Object();
			  后面我们对于 data 仅仅是读取。如果还一直使用写锁的话，就不能让多个线程同时来读取了，持有写锁是浪费资源的，降低了整体的效率，所以这个时候利用锁的降级是很好的办法，可以提高整体性能。

- 两种锁的总结

	- 1.ReentrantReadWriteLock实现了ReadWriteLock接口，最主要的有两个方法：readLock()和writeLock()用来获取读锁和写锁
	- 2.锁申请和释放策略
	- 3.插队策略：为了防止饥饿，读锁不能插队
	- 4.升降级策略：只能降级，不能升级
	- 5.适用场合：相比于ReentrantLock适用于一般场合，ReentrantReadWriteLock适用于读多写少的情况，合理使用可以进一步提高并发效率。

### 多线程竞争时，是否排队

- 排队：公平锁
- 先尝试插队，插队失败再排队：非公平锁

### 同一个线程是否可以重复获取同一把锁

- 可以：可重入锁
- 不可以：不可重入锁

### 是否可中断

- 可以：可中断锁

	- 在 Java 中，synchronized就是不可中断锁，而 Lock 是可中断锁，因为 tryLock(time) 和 lockInterruptibly 都能响应中断。

- 不可以：非可中断锁

### 等锁的过程

阻塞或唤醒一个 Java 线程需要操作系统切换CPU状态来完成，这种状态转换需要耗费处理器时间；
如果同步代码块中的内容过于简单，状态转换消耗的时间有可能比用户代码执行的时间还要长；
在许多场景中，同步资源的锁定时间很短，为了这一小段时间去切换线程，线程挂起和恢复现场的花费可能会让系统得不偿失。

所以就出现了自旋锁

- 自旋：自旋锁

  如果物理机器有多个处理器，能够让两个或以上的线程同时并行执行，就可以让后面那个请求锁的线程不放弃 CPU 的执行时间，看看持有锁的线程是否很快就会释放锁；
  而为了让当前线程“稍等一下”，我们需让当前线程进行自旋，如果在自旋完成后前面锁定同步资源的线程已经释放了锁，那么当前线程就可以不必阻塞而是直接获取同步资源，从而避免切换线程的开销。这就是自旋锁。

	- 自旋锁的缺点

	  如果锁被占用的时间很长，那么自旋的线程只会白浪费处理器资源。
	  在自旋的过程中，一直消耗CPU，虽然自旋锁的起始开销低于悲观锁，但是随着自旋时间增长，开销也是线性增长的。

	- 原理

		- Java1.5以上的并发框架 java.util.concurrent 的 atmoic 包下的类基本都是自旋锁的实现
		- AtomicInteger的实现：自旋锁的实现原理是CAS，其中调用 unsafe 进行自增操作的源码中 do-whiile 没修改成功，就在while里死循环，直至修改成功

- 阻塞：非自旋锁

### Java 虚拟机对锁的优化

- 自旋锁和自适应
- 锁消除
- 锁粗化

## atomic包

### 原子类作用

- 原子类的作用和锁类似，是为了保证并发情况下线程安全。不过原子类相比于锁，有一定优势：

	- 粒度更细：可以把竞争范围缩小到变量级别
	- 效率更高：通常原子类的效率会高于使用锁的效率，除了高度竞争的情况

### 原子类纵览

### 以AtomicInteger为例

- public final int get() // 获取当前的值
- public final int getAndSet(int new Value) // 获取当前的值，并设置新的值
- public final int getAndIncrement() // 获取当前的值，并自增
- public final int getAddAdd(int delta) // 获取当前的值，并加上预期的值
- public final int getAndDecrement() // 获取当前的值，并自减
- boolean compareAndSet(int expect, int update) // 如果当前的数值等于预期值，则以原子方式将该值设置为输入值（update）

### Adder 累加器

- 高并发下 LongAdder 比 AtomicLong 效率高，不过本质是空间换时间
- 竞争激烈的时候，LongAdder把不同线程对应到不同的Cell上进行修改，降低了冲突的概率，是多段锁的思想，提高了并发性
- 演示多线程情况下AtomicLong的性能，有16个线程对一个AtomicLong累加。由于竞争激烈，每次加法，都要flush和refresh，导致很耗费资源。
- 原理分析

	- AtomicLong的弊端
	- LongAdder的改进

		- 图
		- 原理

		  AtomicLong的实现原理是，每次加法都需要做同步，所以在高并发的时候会导致冲突比较多，自然效率就降低了。
		  而LongAdder，每个线程会有自己的一个计数器，仅用来在自己线程内计数，这样就不会和其他线程的计数器有干扰。
		  由于他们之间不存在竞争关系，所以在加和的过程中，根本不需要同步机制，也就不需要flush和reflush。
		  
		  LongAdder引入了分段累加的概念，内部有一个base变量和一个cell[]数组共同参与计数：
		  
		  base变量：竞争不激烈，直接累加到该变量上；
		  
		  Cell[]数组：竞争激励，各个线程分散累加到自己对应索引Cell[i]中；
		  
		  最后再 sum一下。

	- 两者使用场景

		- 在低争用下，AtomicLong和LongAdder这两个类具有相似的特征。但是在竞争激烈下，LongAdder的预期吞吐量要高很多，但要消耗很多的空间。
		- LongAdder适合的场景是统计求和和计数的场景，而且LongAdder基本值提供了add方法，而AtomicLong还具有 CAS 方法

## CAS 原理

### 什么是 CAS

- 首先应用在并发中
- 我认为 V 的值应该是 A，如果是的话那就改成 B，如果不是 A（说明被别人修改过了），那我就不修改了，避免多人同时修改导致出错。
- CAS 有三个操作数：内存值 V、预期值 A、要修改的值 B，当且仅当预期值 A和内存值 V 相同时，才将内存值修改成为 B，否则什么都不做。最后返回现在的 V 值。

### 应用场景

- 乐观锁
- 并发容器
- 原子类

### 以AtomicInteger为例，看 Java 是如何利用 CAS 实现原子操作的？

- Unsafe是 CAS 的核心类。Java无法直接访问底层操作系统，而是通过本地（native）方法来访问。尽管如此，JVM中提供了一个类Unsafe，它提供了硬件级别的原子操作。
- valueOffset 表示的是变量值在内存中的偏移地址，因为Unsafe是根据内存偏移地址获取数据的原值的，这样就通过 Unsafe 来实现 CAS 了。
- Unsafe类中的compareAndSwapInt方法：方法中先拿到变量 value 在内存中的地址。通过native方法实现原子性的比较和替换，最终完成 CAS 的全过程。

## final 关键字和不变性

### 什么是不变性

- 如果对象在被创建后，状态就不能被修改，那么它就是不可变的
- 例子：Person 对象，age 和 name 被 fianl修饰
- 具有不变性的对象一定是线程安全的，我们不需要对其采取任何额外的安全措施，也能保证线程安全

### final的作用

- 类防止被继承、方法防止被重写、变量防止被修改
- 天生是线程安全的，而不需要额外的同步开销

### 3种用法：修饰变量、方法、类

- final修饰变量：被 final 修饰的变量，意味着值不能被修改。如果变量是对象，那么对象的引用不能变，但是对象自身的内容任然是可以变的

	- 类中的 final 属性

		- 1.在声明变量的等号右边直接赋值
		- 2.构造函数中赋值
		- 3.在类的初始化代码块中赋值（不常用）

	- 类中的 static final 属性

		- 1.在声明变量的等号右边直接赋值
		- 2.用 static 初始化代码块赋值（不常用）

	- 方法中的 final 变量

		- 不规定赋值时机，只要求在使用前必须赋值，这和方法中的非 final 变量的要求是一样的

- final修饰方法

	- 构造方法不允许final修饰
	- 不可被重写，也就是不能被@Override
	- static 方法不能被重写。static是随着类加载而加载，虽然父子类可以写一样的静态方法，但他们实质上是不一样的

- final修饰类：不可被继承

### 注意点

- final修饰对象时候，只是对象的引用不可变，而对象本身的属性是可以变化的
- final 是一种良好的编程习惯

### 不变性和 final 的关系

- 不变性并不意味着，简单地用 final 修饰就是不可变

	- 对于基本类型，用final修饰后确实具备不变性
	- 但对于对象类型，需要该对象保证自身被创建后，状态不会变才可以算。

- 如何利用 final 实现对象不可变，需满足三点

	- 1.对象创建后，其状态就不能修改
	- 2.所有属性都是 final 修饰的
	- 3.对象创建过程中没有发生逸出

- 把变量写到线程内部 -- 栈封闭

  在方法里新建的局部变量，实际上是存储在每个线程私有的栈空间，而每个栈的栈空间是不能被其他线程所访问到的，所以不会有线程安全问题。这就是“栈封闭”技术，是“线程封闭”技术的一种情况。

### 面试例子

1.
String a = "1112";
final String b = "111";
String d = "111";
String c = b + 2;
String e = d + 2;
sout(a == c);
sout(a == e);

true;
false;


方法1 {
String a = "1112";
final String b = 方法2();
String c = b + 2;
sout(a == b);
}

方法2 {
return "111";
}

false

## 获取子线程的执行结果

### Runnable 的缺陷

- 不能返回一个返回值
- 也不能抛出 checked Exception

### Callable 接口

- 类似于 Runnable，被其他线程执行的任务
- 实现 call 方法
- 有返回值
- 源码

  @FunctionalInterface
  public interface Callable<V> {
      /**
       * Computes a result, or throws an exception if unable to do so.
       *
       * @return computed result
       * @throws Exception if unable to compute a result
       */
      V call() throws Exception;
  }

### Future 和 Callable 的关系

首先可以用 Future.get 来获取 Callable 接口返回的执行结果，还可以通过 Future.isDone() 来判断任务是否已经执行完了，以及取消这个任务，限时获取任务的结果等

在 call() 未执行完毕之前，调用 get() 的线程（假设此时是主线程）会被阻塞，直到 call() 方法返回了结果后，此时 future.get() 才会得到该结果，然后主线程才会切换到 runnable 状态

所以 Future 是一个存储器，它存储了 call() 这个任务的结果，而这个任务的执行时间是无法提前确定的，因为这完全取决于 call() 方法执行的情况

### Future 的主要方法

- get() 方法：获取结果（行为取决于 Callable 任务的状态）

	- 1.任务正常完成：get方法会立刻返回结果
	- 2.任务尚未完成（任务还没开始或者进行中）：get将阻塞并只直到任务完成。
	- 3.任务执行过程中抛出Exception：get方法会抛出ExecutionException：这里的抛出异常，是 call() 执行时产生的那个异常。无论 call() 执行时抛出的异常类型是什么，最后 get() 方法抛出的异常类型都是 ExecutionException。
	- 4.任务被取消：get方法会抛出 CancellationException
	- 5.任务超时：get方法有个重载方法，是传入一个延迟时间，如果时间到了还没返回结果，get方法就会抛出TimeoutExcetpion。

- get(long timeout, TimeUnit unit)：有超时时间的获取

	- 用此方法时，如果 call() 在规定时间内完成了任务，那么就会正常获取到返回值；而超时就会抛出TimeoutException
	- 超时不获取，任务需取消

- cancel方法

	- 1.如果任务还没开始执行，调用该方法，任务会被正常的取消，未来也不会被执行，方法返回true
	- 2.如果任务已完成，或者取消：那么cancel()方法会执行失败，方法返回 false。
	- 3.如果任务开始执行了，方法将不会取消该任务，而是根据填写的入参来判断。
	- 针对任务开始执行中的情况（情况3）

		- Future.cancel(ture)适用于：任务能够处理 interrupt异常
		- Future.cancel(false) 仅用于避免启动尚未启动的任务，适用于：

			- 1.未能处理interrupt的任务
			- 2.不清楚任务是否支持取消
			- 3.需要等待已经开始的任务执行完成

- isDone()方法：判断线程是否执行完毕
- isCancelled()方法：判断是否被取消

### 用法

- 1.线程池的submit方法返回Future对象

  首先，我们要给线程池提交我们的任务，提交时线程池会立刻返回给我们一个空的Future容器。当线程的任务一旦执行完毕，也就是当我们可以获取到结果时候，线程池就会把结果填入到之前生成的Future对象中，之后可以从Future中获取到任务执行的结果

- 2.get超时方法，调用future.cancel() ，传入 true和 false的区别：代表是否中断正在执行的任务

  public class Timeout {
      
      private static final Ad DEFAULT_AD = new Ad("无网络时候的默认广告");
      private static final ExecutorService exec = Executors.newFixedThreadPool(10);
      
      static class Ad {
          
          String name;
          
          public Ad(String name) {
              this.name = name;
          }
          
          @Override
          public String toString() {
              return "Ad{" +
                  "name='" + name + '\'' +
                  '}';
          }
      }
      
      static class FetchAdTask implements Callable<Ad> {
          
          @Override
          public Ad call() {
              try {
                  Thread.sleep(3000);
              } catch (InterruptedException e) {
                  System.out.println("sleep期间被中断了");
                  return new Ad("被中断时候的默认广告");
              }
              return new Ad("旅游订票哪家强？找某程");
          }
      }
      
      public void printAd() {
          Future<Ad> f = exec.submit(new FetchAdTask());
          Ad ad;
          try {
              ad = f.get(2000, TimeUnit.MILLISECONDS);
          } catch (InterruptedException e) {
              ad = new Ad("被中断时候的默认广告");
          } catch (ExecutionException e) {
              ad = new Ad("异常时候的默认广告");
          } catch (TimeoutException e) {
              ad = new Ad("超时时候的默认广告");
              System.out.println("超时，未获取到广告");
              boolean cancel = f.cancel(false);
              System.out.println("cancel的结果：" + cancel);
          }
          exec.shutdown();
          System.out.println(ad);
      }
      
      public static void main(String[] args) {
          Timeout timeout = new Timeout();
          timeout.printAd();
      }
  }
  当 f.cancel(false) 时，返回内容：
  
  超时，未获取到广告
  cancel的结果：true
  Ad{name='超时时候的默认广告'}
  
  当 f.cancel(true) 时，返回内容：
  
  超时，未获取到广告
  sleep期间被中断了
  cancel的结果：true
  Ad{name='超时时候的默认广告'}

- 3.用FutureTask来创建Future

  把 Callable 实例作为参数，生成 FutureTask对象，然后把对象作为Runnable对象，用线程池或者子线程执行该对象，最后通过 FutureTask作为返回值的接收。

	- FutureTask来获取Future和任务的结果
	- FutureTask是一种包装器，可以把 Callable 转化成 Future 和 Runnable，它同时实现两者的接口
	- 所以FutureTask既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值

### Future的注意点

- 当 for循环批量获取future的结果时，容易发生一部分线程很慢的情况，get方法调用时应使用timeout限制
- Future生命周期不能后退。一旦完成了任务，就永远是已完成

## AQS

### 为什么需要AQS?

- 锁和协作类有共同点：闸门

  ReentrantLock、Semaphore、CountDownLatch、ReentrantReadWriteLock都有这样类似的“协作”功能，其实，它们底层都用一个共同的基类，就是AQS。
  
  因为上面那些协作类，它们有很多工作都是类似的，所以如果能够提取出一个工具类，那么就可以直接用。比如对于ReentrantLock和Semaphore而言就可以屏蔽很多细节，只关注它们自己“业务逻辑”就可以了。

### AQS的作用

AQS是一个用于构建锁、同步器、协作工具类的工具类（框架）。有了AQS以后，更多的协作工具类都可以很方便的被写出来。

### AQS的重要性、地位

### AQS内部原理解析

- AQS最核心的三部分

	- state

	  private volatile int state;
	  
	  这里state的具体含义，会根据具体实现类的不同而不同，比如在Semaphore里，它表示“剩余的许可证的数量”，而在CountDownLatch里，它表示“还需要倒数的数量”。
	  
	  state被volatile修饰的，会被并发的修改，所以所有修改state的方法都需要保证线程安全，比如getState，setState以及compareAndSetState操作来读取和更新这个状态。
	  
	  在ReentrantLock中，state用来表示“锁”的占有情况，包括可重入计数；当state的值为0的时候，标识改Lock不被任何线程所占有

	- 控制线程抢锁和配合的FIFO队列

	  这个队列用来存放“等待的线程”，AQS就是“排队管理器”，当多个线程争用同一把锁时，必须有排队机制将那些没能拿到锁的线程串在一起。当锁释放时，锁管理器就会挑选一个合适的线程来占有这个刚刚释放的锁
	  
	  AQS会维护一个等待的线程队列，把线程都放到这个队里里。

	- 期望协作工具类去实现的获取/释放等重要方法

	  这里的获取和释放方法，是利用AQS的协作工具类里最重要的方法，是由协作类自己去实现的，并且含有各不相同
	  
	  获取方法：
	  获取操作会依赖state变量，经常会阻塞（比如获取不到锁的时候）；
	  在Semaphore中，获取就是acquire方法，作用是获取一个许可证；
	  在CountDownLatch里面，获取就是await方法，作用是“等待，直到倒数结束”
	  
	  释放方法：
	  释放操作不会阻塞；
	  在Semaphore中，释放就是release方法，作用是释放一个许可证；
	  在CountDownLatch里面，释放就是countDown方法，作用是“倒数1个数”

### 应用实例、源码解析

- AQS用法

	- 1.写一个类，想好协作的逻辑，实现获取/释放方法。
	- 2.内部写一个Sync类继承AbstractQueuedSynchronizer
	- 3.根据是否独占来重写tryAcquire/tryRelease或tryAcquireShared(int acquires)和tryReleaseShared(int releases)等方法，在之前写的获取/释放方法中调用AQS的acquire/release或者Shared方法

- ASQ在CountDownLatch的应用

  调用CountDownLatch的await方法时，便会尝试获取“共享锁”，不过一开始是获取不到该锁的，于是线程被阻塞；
  
  而“共享锁”可获取到的条件，就是“锁计数器”的值为0；
  
  而”锁计数器“的初始值为count，每当一个线程调用该CountDownLatch对象的countDown()方法时，才将”锁计数器“-1；
  
  count个线程调用countDown()之后，“锁计数器”才为0，而前面提到的等待获取共享锁的线程才能继续运行。

- AQS在Semaphore的应用

  在Semaphore中，state表示许可证的剩余数量；
  
  看tryAcquire方法，判断nonfairTryAcquireShared大于等于0的话，代表成功；
  
  先检查剩余许可证数量够不够这次需要的，减法算，如果不够，那就返回负数，表示失败；如果够了，用自旋加compareAndSetState来改变state状态，直到改成功就返回正数；或者是期间如果被其他人修改了导致剩余数量不够了，那也返回负数代表获取失败。

## 控制并发流程

### 概念：该工具类，作用就是帮助我们程序员更容易得让线程之间合作。让线程之间相互配合，来满足业务逻辑

### 工具类概图

### CountDownLatch

- 主要方法介绍

	- CountDownLatch(int count):仅有一个构造函数，参数count为需要倒数的数值
	- await():调用await()方法的线程会被挂起，它会等待知道count值为0才继续执行。
	- countDown():将count值减1，直到为0时，等待的线程会被唤起。

- 用法

	- 用法一：一个线程等待多个线程都执行完毕，再继续自己的工作

	  /**
	   * @Author: 孙宏浩
	   * @CreateTime: 2022-05-07 16:57
	   * @Description: 工厂中，质检，5个工人检查，所有人都认为通过，才通过
	   */
	  public class CountDownLatchDemo1 {
	      
	      public static void main(String[] args) throws InterruptedException {
	          CountDownLatch latch = new CountDownLatch(5);
	          ExecutorService service = Executors.newFixedThreadPool(5);
	          for (int i = 0; i < 5; i++) {
	              final int no = i + 1;
	              Runnable runnable = new Runnable() {
	                  @Override
	                  public void run() {
	                      try {
	                          Thread.sleep((long) (Math.random() * 1000));
	                          System.out.println("No." + no + "完成了检查。");
	                      } catch (InterruptedException e) {
	                          e.printStackTrace();
	                      } finally {
	                          latch.countDown();
	                      }
	                  }
	              };
	              
	              service.submit(runnable);
	          }
	          System.out.println("等待5个人检查完......");
	          latch.await();
	          System.out.println("所有人都完成了工作，进入下一个环节。");
	          service.shutdown();
	      }
	  }

	- 用法二：多个线程等待某个线程的信号，同时开始执行

	  /**
	   * @Author: 孙宏浩
	   * @CreateTime: 2022-05-07 17:14
	   * @Description: 模拟100米跑步，5名选手都准备好了，只等裁判员一声令下，所有人同时开始跑步
	   */
	  public class CountDownLatchDemo2 {
	      
	      public static void main(String[] args) throws InterruptedException {
	          CountDownLatch begin = new CountDownLatch(1);
	          ExecutorService service = Executors.newFixedThreadPool(5);
	          for (int i = 0; i < 5; i++) {
	              final int no = i + 1;
	              Runnable runnable = () -> {
	                  System.out.println("No." + no + "准备完毕，等待发令枪");
	                  try {
	                      begin.await();
	                      System.out.println("No." + no + "开始跑步了");
	                  } catch (InterruptedException e) {
	                      e.printStackTrace();
	                  }
	              };
	              
	              service.submit(runnable);
	          }
	      
	          Thread.sleep(5000);
	          System.out.println("发令枪响，比赛开始！");
	          begin.countDown();
	          service.shutdown();
	      }
	  }

	- 以上两种结合起来使用

	  /**
	   * @Author: 孙宏浩
	   * @CreateTime: 2022-05-07 17:14
	   * @Description: 模拟100米跑步，5名选手都准备好了，只等裁判员一声令下，所有人同时开始跑步;当所有人都到终点后，比赛结束
	   */
	  public class CountDownLatchDemo3 {
	      
	      public static void main(String[] args) throws InterruptedException {
	          CountDownLatch begin = new CountDownLatch(1);
	          CountDownLatch end = new CountDownLatch(5);
	          ExecutorService service = Executors.newFixedThreadPool(5);
	          for (int i = 0; i < 5; i++) {
	              final int no = i + 1;
	              Runnable runnable = () -> {
	                  System.out.println("No." + no + "准备完毕，等待发令枪");
	                  try {
	                      begin.await();
	                      System.out.println("No." + no + "开始跑步了");
	                      Thread.sleep((long) (Math.random() * 10000));
	                      System.out.println("No." + no + "跑到终点了");
	                  } catch (InterruptedException e) {
	                      e.printStackTrace();
	                  } finally {
	                      end.countDown();
	                  }
	              };
	              
	              service.submit(runnable);
	          }
	      
	          Thread.sleep(5000);
	          System.out.println("发令枪响，比赛开始！");
	          begin.countDown();
	          
	          end.await();
	          System.out.println("所有人到达终点，比赛结束");
	          
	          service.shutdown();
	      }
	  }

- 注意点

	- 扩展用法：多个线程等多个线程完成执行后，再同时执行
	- CountDownLatch是不能够重用的，如果需要重新计数，可以考虑使用CyclicBarrier或者创建新的CountDownLatch实例。

- 总结

	- 经典用法：一等多和多等一
	- CountDownLatch类在创建实例的时候，需要传递倒数次数。倒数到0的时候，之前等待的线程会继续执行
	- CountDownLatch不能回滚重置

### Semaphore

- Semaphore可以用来限制或管理数量有限的资源的使用情况
- 信号量用法

	- 使用流程

		- 1.初始化Semaphore并指定许可证的数量
		- 2.在需要被现在的代码前加acquire()或者acquireUninterruptibly()方法
		- 3.在任务执行结束后，调用release()来释放许可证

	- 主要方法介绍

		- new Semaphore(int permits, boolean fair):可以设置是否要使用公平策略，如果传入true，那么会把之前等待的线程放到FIFO的队列中。
		- tryAcquire():看看现在有没有空闲的许可证，如果有的话就获取，没有就去做其他事情。
		- tryAcquire(timeout):和tryAcquire()一样，多了个超时时间。
		- release():用完归还

- 案例
- 注意点

	- 1.获取和释放的许可证数量必须一致，否则比如每次都获取2个但是值释放一个甚至不释放，随着时间推移，到最后许可证数量不够用，会导致程序卡死。
	- 2.注意在初始化Semaphore的时候设置公平性，一般设置为true会更合理
	- 3.并不是必须由获取许可证的线程释放那个许可证，事实上，获取和释放许可证对线程并无要求，也许是A获取了，然后由B释放，只要逻辑合理即可。
	- 4.信号量的作用，除了控制临界区最多同时有N个线程访问外，另一个作用是可以实现“条件等待”，例如线程1需要在线程2完成准备工作后才能开始工作，那么就线程1 acquire()，而线程2完成任务后 release()，这就相当于轻量级的CountDownLatch。

### Condition

- 作用

	- 当线程1需要等待某个条件的时候，它就去执行condition.await()方法，一旦执行了await()方法，线程就会进入阻塞状态
	- 然后通常会有另一个线程，去执行对应的条件，直到条件满足，该线程就会执行condition.signal()方法，这时 JVM 就会从被阻塞的线程中找到那些等待该 condition 的线程，但线程1收到可执行信号的时候，它的线程状态就会变成 Runnable 可执行状态

- signalAll() 和 signal() 区别

	- signalAll() 会唤起所有的正在等待的线程
	- 但是 signal() 是公平的，只会唤起那个等待时间最长的线程

- 案例

  /**
   * @Author: 孙宏浩
   * @CreateTime: 2022-05-08 09:56
   * @Description: 演示用 Condition 实现生产者消费者模式
   */
  public class ConditionDemo2 {
      
      private int queueSize = 10;
      private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
      private Lock lock = new ReentrantLock();
      private Condition notFull = lock.newCondition();
      private Condition notEmpty = lock.newCondition();
      
      public static void main(String[] args) {
          ConditionDemo2 conditionDemo2 = new ConditionDemo2();
          Producer producer = conditionDemo2.new Producer();
          Consumer consumer = conditionDemo2.new Consumer();
          producer.start();
          consumer.start();
      }
      
      class Consumer extends Thread {
          
          @Override
          public void run() {
              consume();
          }
      
          private void consume() {
              while (true) {
                  lock.lock();
                  try {
                      while (queue.size() == 0) {
                          System.out.println("队列空，等待数据");
                          try {
                              notEmpty.await();
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                      queue.poll();
                      notFull.signalAll();
                      System.out.println("从队列里取走一个数据，队列剩余" + queue.size() + "个元素");
                  } finally {
                      lock.unlock();
                  }
              }
          }
      }
      
      class Producer extends Thread {
          
          @Override
          public void run() {
              produce();
          }
          
          private void produce() {
              while (true) {
                  lock.lock();
                  try {
                      while (queue.size() == queueSize) {
                          System.out.println("队列满，等待有空余");
                          try {
                              notFull.await();
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                      queue.offer(1);
                      notEmpty.signalAll();
                      System.out.println("向队列插入了一个元素，队列剩余空间" + (queueSize - queue.size()));
                  } finally {
                      lock.unlock();
                  }
              }
          }
      }
  }

- 注意点

	- 实际上，如果说Lock用来代替synchronized，那么Condition就是用来代替相对应的Object.wait/notify的，所以在用法和性质上，几乎一样。
	- await方法会自动释放持有的Lock锁，和Object.wait一样，不需要自己手动先释放锁
	- 调用await的时候，必须持有锁，否则会抛出异常，和Object.wait一样。

### CyclicBarrier

- 与CountDownLatch的区别

	- 作用不同：CyclicBarrier要等固定数量的线程都达到了栅栏位置才能继续执行，而CountDownLatch只需等待数字到0，也就是说，CountDownLatch用于事件，但是CyclicBarrier是用于线程的。
	- 可重用性不同：CountDownLatch在倒数到0并触发门闩打开后，就不能再次使用了，触发新建新的实例；而CyclicBarrier可重复使用。

- 案例

  /**
   * @Author: 孙宏浩
   * @CreateTime: 2022-05-08 10:10
   * @Description:
   */
  public class CyclicBarrierDemo {
      
      public static void main(String[] args) {
          CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
              System.out.println("所有人都到场了，大家统一出发！");
          });
          for (int i = 0; i < 10; i++)
              new Thread(new Task(i, cyclicBarrier)).start();
      }
      
      
      static class Task implements Runnable {
          private int id;
          private CyclicBarrier cyclicBarrier;
          
          public Task(int id, CyclicBarrier cyclicBarrier) {
              this.id = id;
              this.cyclicBarrier = cyclicBarrier;
          }
          
          @Override
          public void run() {
              System.out.println("线程" + id + "现在前往集合地点");
              try {
                  Thread.sleep((long) (Math.random() * 10000));
                  System.out.println("线程" + id + "到了集合地点，开始等待其他人到达");
                  cyclicBarrier.await();
                  System.out.println("线程" + id + "出发了");
              } catch (InterruptedException | BrokenBarrierException e) {
                  e.printStackTrace();
              }
          }
      }
  }

## ConcurrentHashMap等并发集合工具类

### 为什么要把 1.7 的结构改成 1.8 的结构？

- 数据结构

  在1.7中采用是 Segment + ReetrantLock + 拉链法实现；1.8中是 Node + CAS + synchronized + 链表和红黑树结构

- Hash碰撞

  1.7中遇到碰撞时会直接使用拉链法，用链表存储。而在1.8中会先使用拉链法，如果数量达到8以后，会转成红黑树形式存储

- 保证并发安全

  1.7中采用的是分段锁，用Segment来保证线程安全，用 ReentrantLock实现。1.8中通过 CAS + synchronized来实现

- 查询复杂度

  1.7中是O(n)；1.8中如果变成红黑树，就成O(logn)

- 为什么超过 8 要转为红黑树呢？

  1.默认是链表的形式存储冲突时的数据，这是因为所占用的内存更少；
  2.想要达到hash冲突到8时，是很难的，官方给出冲突为8时的概率已经是千万分之几了。那如果真的发生这种情况的话，为了保证HashMap查询效率，会转为红黑树

### CopyOnWriteArrayList

- Vector和SynchronizedList的锁粒度太大，并发效率相对比较低，并且迭代时无法编辑
- 适用场景

	- 读操作可以尽可能地快，而写即使慢一些也没有太大关系
	- 读多写少：黑名单，每日更新；监听器：迭代操作远没有多于修改操作

- 读写规则

	- 之前读写锁：读读共享、其他都互斥（写写互斥、读写互斥、写读互斥）
	- 读写锁规则的升级：读取完全是不加锁的，并且写入也不会阻塞读取操作。只有写写操作会互斥

- 实现原理

	- CopyOnWrite的含义
	- 创建新副本、读写分离
	- “不可变”原理
	- 迭代时操作，也不会报错

- 缺点

	- 数据一致性问题

	  CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果希望写入的数据，马上能读到，就不要用CopyOnWrite容器。

	- 内存占用问题

	  因为CopyOnWrite的写是复制机制，所以在进行写操作的时候，内存里会同时驻扎两个对象的内存。

