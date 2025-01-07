package com.sum.stater.utils

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * 线程池管理类，提供 CPU 密集型任务和 IO 密集型任务的线程池。
 * 通过合理的线程池配置，确保任务能够在合适的线程中执行，避免资源浪费和线程竞争。
 */
object DispatcherExecutor {

    // 获取当前设备的 CPU 核心数
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    // 核心线程池大小，至少为 2，最大为 CPU 核心数减 1 和 5 的较小值
    private val CORE_POOL_SIZE = 2.coerceAtLeast((CPU_COUNT - 1).coerceAtMost(5))

    // 最大线程池大小，与核心线程池大小相同
    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE

    // 线程空闲时的存活时间（秒）
    private const val KEEP_ALIVE_SECONDS = 5

    // 任务队列，用于存放待执行的任务
    private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()

    // 线程工厂，用于创建线程
    private val sThreadFactory = DefaultThreadFactory()

    // 拒绝策略，当任务无法执行时，使用该策略处理
    private val sHandler = RejectedExecutionHandler { r, executor ->
        // 如果线程池无法执行任务，则使用一个新的缓存线程池来执行任务
        Executors.newCachedThreadPool().execute(r)
    }

    /**
     * CPU 密集型任务的线程池。
     * 适用于计算密集型任务，线程池大小根据 CPU 核心数动态调整。
     */
    var cPUExecutor: ThreadPoolExecutor? = null
        private set

    /**
     * IO 密集型任务的线程池。
     * 适用于 IO 密集型任务，线程池大小不受限制，适合处理大量短时任务。
     */
    var iOExecutor: ExecutorService? = null
        private set

    /**
     * 默认的线程工厂类，用于创建线程。
     */
    private class DefaultThreadFactory : ThreadFactory {
        // 线程组
        private val group: ThreadGroup?

        // 线程编号
        private val threadNumber = AtomicInteger(1)

        // 线程名前缀
        private val namePrefix: String

        override fun newThread(r: Runnable): Thread {
            // 创建新线程
            val t = Thread(
                group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0
            )
            // 确保线程不是守护线程
            if (t.isDaemon) {
                t.isDaemon = false
            }
            // 设置线程优先级为普通优先级
            if (t.priority != Thread.NORM_PRIORITY) {
                t.priority = Thread.NORM_PRIORITY
            }
            return t
        }

        companion object {
            // 线程池编号
            private val poolNumber = AtomicInteger(1)
        }

        init {
            // 获取安全管理器（如果有）
            val s = System.getSecurityManager()
            // 设置线程组
            group = s?.threadGroup ?: Thread.currentThread().threadGroup ?: null
            // 设置线程名前缀
            namePrefix = "TaskDispatcherPool-${poolNumber.getAndIncrement()}-Thread-"
        }
    }

    // 初始化线程池
    init {
        // 创建 CPU 密集型任务的线程池
        cPUExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory, sHandler
        )
        // 允许核心线程在空闲时超时销毁
        cPUExecutor?.allowCoreThreadTimeOut(true)

        // 创建 IO 密集型任务的线程池
        iOExecutor = Executors.newCachedThreadPool(sThreadFactory)
    }
}