package com.sum.stater.dispatcher

import android.app.Application
import android.os.Looper
import android.util.Log
import androidx.annotation.UiThread
import com.sum.stater.TaskStat
import com.sum.stater.sort.TaskSortUtil
import com.sum.stater.task.DispatchRunnable
import com.sum.stater.task.Task
import com.sum.stater.task.TaskCallBack
import com.sum.stater.utils.DispatcherLog
import com.sum.stater.utils.StaterUtils
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 任务调度器的核心类，负责管理和调度任务的执行。
 * 支持任务的依赖管理、优先级调度、线程池分配等功能。
 */
class TaskDispatcher private constructor() {

    // 任务调度的开始时间
    private var mStartTime: Long = 0

    // 保存所有异步任务的 Future 对象，用于取消任务
    private val mFutures: MutableList<Future<*>> = ArrayList()

    // 所有待执行的任务列表
    private var mAllTasks: MutableList<Task> = ArrayList()

    // 所有任务的 Class 类型列表，用于依赖管理
    private val mClsAllTasks: MutableList<Class<out Task>> = ArrayList()

    // 需要在主线程执行的任务列表
    @Volatile
    private var mMainThreadTasks: MutableList<Task> = ArrayList()

    // 用于等待异步任务完成的 CountDownLatch
    private var mCountDownLatch: CountDownLatch? = null

    // 需要等待的任务数量
    private val mNeedWaitCount = AtomicInteger()

    // 需要等待的任务列表
    private val mNeedWaitTasks: MutableList<Task> = ArrayList()

    // 已经完成的任务列表
    @Volatile
    private var mFinishedTasks: MutableList<Class<out Task>> = ArrayList(100)

    // 任务依赖关系表，记录每个任务被哪些任务依赖
    private val mDependedHashMap = HashMap<Class<out Task>, ArrayList<Task>?>()

    // 任务分析的次数，用于统计任务分析的耗时
    private val mAnalyseCount = AtomicInteger()

    /**
     * 添加任务到调度器中。
     *
     * @param task 要添加的任务
     * @return 返回当前 TaskDispatcher 实例，支持链式调用
     */
    fun addTask(task: Task?): TaskDispatcher {
        task?.let {
            // 收集任务的依赖关系
            collectDepends(it)
            // 将任务添加到所有任务列表中
            mAllTasks.add(it)
            // 将任务的 Class 类型添加到 Class 列表中
            mClsAllTasks.add(it.javaClass)
            // 如果任务需要等待（非主线程且 needWait 为 true），则将其添加到需要等待的任务列表中
            if (ifNeedWait(it)) {
                mNeedWaitTasks.add(it)
                mNeedWaitCount.getAndIncrement()
            }
        }
        return this
    }

    /**
     * 收集任务的依赖关系，并将其添加到依赖关系表中。
     *
     * @param task 当前任务
     */
    private fun collectDepends(task: Task) {
        task.dependsOn()?.let { list ->
            for (cls in list) {
                cls?.let { cls ->
                    // 如果依赖的任务还没有被记录，则初始化一个空列表
                    if (mDependedHashMap[cls] == null) {
                        mDependedHashMap[cls] = ArrayList()
                    }
                    // 将当前任务添加到依赖任务的依赖列表中
                    mDependedHashMap[cls]?.add(task)
                    // 如果依赖的任务已经完成，则直接满足当前任务
                    if (mFinishedTasks.contains(cls)) {
                        task.satisfy()
                    }
                }
            }
        }
    }

    /**
     * 判断任务是否需要等待。
     *
     * @param task 当前任务
     * @return 如果任务需要等待（非主线程且 needWait 为 true），则返回 true，否则返回 false
     */
    private fun ifNeedWait(task: Task): Boolean {
        return !task.runOnMainThread() && task.needWait()
    }

    /**
     * 启动任务调度器，开始执行任务。
     * 必须在主线程调用。
     */
    @UiThread
    fun start() {
        // 记录任务调度的开始时间
        mStartTime = System.currentTimeMillis()
        // 确保当前线程是主线程
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw RuntimeException("must be called from UiThread")
        }

        // 如果有任务需要执行
        if (!mAllTasks.isNullOrEmpty()) {
            // 增加任务分析的次数
            mAnalyseCount.getAndIncrement()
            // 打印任务的依赖关系信息
            printDependedMsg(false)
            // 对任务进行拓扑排序，确保依赖关系得到满足
            mAllTasks = TaskSortUtil.getSortResult(mAllTasks, mClsAllTasks).toMutableList()
            // 初始化 CountDownLatch，用于等待需要等待的任务完成
            mCountDownLatch = CountDownLatch(mNeedWaitCount.get())
            // 发送并执行异步任务
            sendAndExecuteAsyncTasks()
            // 打印任务分析的耗时
            DispatcherLog.i("task analyse cost ${(System.currentTimeMillis() - mStartTime)} begin main ")
            // 执行主线程任务
            executeTaskMain()
        }
        // 打印任务调度的总耗时
        DispatcherLog.i("task analyse cost startTime cost ${(System.currentTimeMillis() - mStartTime)}")
    }

    /**
     * 取消所有任务的执行。
     */
    fun cancel() {
        for (future in mFutures) {
            future.cancel(true)
        }
    }

    /**
     * 执行主线程任务。
     */
    private fun executeTaskMain() {
        // 记录主线程任务的开始时间
        mStartTime = System.currentTimeMillis()
        for (task in mMainThreadTasks) {
            val time = System.currentTimeMillis()
            // 执行任务
            DispatchRunnable(task, this).run()
            // 打印任务的执行耗时
            DispatcherLog.i(
                "real main ${task.javaClass.simpleName} cost ${(System.currentTimeMillis() - time)}"
            )
        }
        // 打印主线程任务的总耗时
        DispatcherLog.i("mainTask cost ${(System.currentTimeMillis() - mStartTime)}")
    }

    /**
     * 发送并执行异步任务。
     */
    private fun sendAndExecuteAsyncTasks() {
        for (task in mAllTasks) {
            // 如果任务只在主进程执行且当前不是主进程，则标记任务为已完成
            if (task.onlyInMainProcess() && !isMainProcess) {
                markTaskDone(task)
            } else {
                // 否则发送任务
                sendTaskReal(task)
            }
            // 标记任务已发送
            task.isSend = true
        }
    }

    /**
     * 打印任务的依赖关系信息。
     *
     * @param isPrintAllTask 是否打印所有任务的依赖信息
     */
    private fun printDependedMsg(isPrintAllTask: Boolean) {
        DispatcherLog.i("needWait size : ${mNeedWaitCount.get()}")
        if (isPrintAllTask) {
            for (cls in mDependedHashMap.keys) {
                DispatcherLog.i("cls: ${cls.simpleName} ${mDependedHashMap[cls]?.size}")
                mDependedHashMap[cls]?.let {
                    for (task in it) {
                        DispatcherLog.i("cls:${task.javaClass.simpleName}")
                    }
                }
            }
        }
    }

    /**
     * 通知依赖当前任务的其他任务，当前任务已完成。
     *
     * @param launchTask 已完成的任务
     */
    fun satisfyChildren(launchTask: Task) {
        val arrayList = mDependedHashMap[launchTask.javaClass]
        if (!arrayList.isNullOrEmpty()) {
            for (task in arrayList) {
                // 满足依赖当前任务的其他任务
                task.satisfy()
            }
        }
    }

    /**
     * 标记任务为已完成。
     *
     * @param task 已完成的任务
     */
    fun markTaskDone(task: Task) {
        if (ifNeedWait(task)) {
            // 将任务添加到已完成任务列表中
            mFinishedTasks.add(task.javaClass)
            // 从需要等待的任务列表中移除
            mNeedWaitTasks.remove(task)
            // CountDownLatch 计数减一
            mCountDownLatch?.countDown()
            // 需要等待的任务数量减一
            mNeedWaitCount.getAndDecrement()
        }
    }

    /**
     * 发送任务并执行。
     *
     * @param task 要执行的任务
     */
    private fun sendTaskReal(task: Task) {
        if (task.runOnMainThread()) {
            // 如果任务需要在主线程执行，则将其添加到主线程任务列表中
            mMainThreadTasks.add(task)
            if (task.needCall()) {
                // 如果任务需要回调，则设置回调
                task.setTaskCallBack(object : TaskCallBack {
                    override fun call() {
                        // 标记任务完成
                        TaskStat.markTaskDone()
                        task.isFinished = true
                        // 通知依赖当前任务的其他任务
                        satisfyChildren(task)
                        // 标记任务为已完成
                        markTaskDone(task)
                        // 打印任务完成日志
                        DispatcherLog.i("${task.javaClass.simpleName} finish")
                        Log.i("testLog", "call")
                    }
                })
            }
        } else {
            // 如果任务在异步线程执行，则提交到线程池
            val future = task.runOn()?.submit(DispatchRunnable(task, this))
            future?.let {
                // 将 Future 对象添加到 Future 列表中
                mFutures.add(it)
            }
        }
    }

    /**
     * 执行任务。
     *
     * @param task 要执行的任务
     */
    fun executeTask(task: Task) {
        if (ifNeedWait(task)) {
            // 如果任务需要等待，则增加需要等待的任务数量
            mNeedWaitCount.getAndIncrement()
        }
        // 提交任务到线程池
        task.runOn()?.execute(DispatchRunnable(task, this))
    }

    /**
     * 等待所有需要等待的任务完成。
     * 必须在主线程调用。
     */
    @UiThread
    fun await() {
        try {
            if (DispatcherLog.isDebug) {
                // 打印需要等待的任务信息
                DispatcherLog.i("still has ${mNeedWaitCount.get()}")
                for (task in mNeedWaitTasks) {
                    DispatcherLog.i("needWait: ${task.javaClass.simpleName}")
                }
            }
            // 如果还有需要等待的任务，则等待
            if (mNeedWaitCount.get() > 0) {
                mCountDownLatch?.await(WAIT_TIME.toLong(), TimeUnit.MILLISECONDS)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        // 等待任务完成的最大时间（10秒）
        private const val WAIT_TIME = 10000

        // 应用的上下文
        var context: Application? = null
            private set

        // 当前是否为主进程
        var isMainProcess = false
            private set

        // 标记 TaskDispatcher 是否已经初始化
        @Volatile
        private var sHasInit = false

        /**
         * 初始化 TaskDispatcher。
         *
         * @param context 应用的上下文
         */
        fun init(context: Application?) {
            context?.let {
                Companion.context = it
                sHasInit = true
                // 判断当前是否为主进程
                isMainProcess = StaterUtils.isMainProcess(context)
            }
        }

        /**
         * 创建 TaskDispatcher 实例。
         * 注意：每次调用都会返回一个新的实例。
         *
         * @return TaskDispatcher 实例
         */
        fun createInstance(): TaskDispatcher {
            if (!sHasInit) {
                throw RuntimeException("must call TaskDispatcher.init first")
            }
            return TaskDispatcher()
        }
    }
}