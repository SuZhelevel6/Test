package com.sum.stater.task

import android.os.Looper
import android.os.Process
import androidx.core.os.TraceCompat
import com.sum.stater.TaskStat
import com.sum.stater.dispatcher.TaskDispatcher
import com.sum.stater.utils.DispatcherLog

/**
 * 任务的实际执行类，负责执行任务的 `run` 方法，并在任务完成后触发回调。
 * 该类实现了 `Runnable` 接口，用于在指定的线程池或主线程中执行任务。
 */
class DispatchRunnable : Runnable {
    // 当前要执行的任务
    private var mTask: Task

    // 任务调度器，用于通知任务完成和依赖关系的处理
    private var mTaskDispatcher: TaskDispatcher? = null

    /**
     * 构造函数，用于创建一个 DispatchRunnable 实例。
     *
     * @param task 要执行的任务
     */
    constructor(task: Task) {
        mTask = task
    }

    /**
     * 构造函数，用于创建一个 DispatchRunnable 实例，并指定任务调度器。
     *
     * @param task 要执行的任务
     * @param dispatcher 任务调度器
     */
    constructor(task: Task, dispatcher: TaskDispatcher?) {
        mTask = task
        mTaskDispatcher = dispatcher
    }

    /**
     * 任务的实际执行逻辑。
     * 该方法会在任务执行时调用，负责执行任务的 `run` 方法，并在任务完成后触发回调。
     */
    override fun run() {
        // 使用 TraceCompat 记录任务执行的开始，方便性能分析
        TraceCompat.beginSection(mTask.javaClass.simpleName)
        // 打印任务开始执行的日志
        DispatcherLog.i("${mTask.javaClass.simpleName}开始执行 | Situation：${TaskStat.currentSituation}")

        // 设置任务的线程优先级
        Process.setThreadPriority(mTask.priority())

        // 记录任务开始等待的时间
        var startTime = System.currentTimeMillis()
        // 标记任务为等待状态
        mTask.isWaiting = true
        // 等待任务的前置依赖任务完成
        mTask.waitToSatisfy()
        // 计算任务等待的时间
        val waitTime = System.currentTimeMillis() - startTime

        // 记录任务开始执行的时间
        startTime = System.currentTimeMillis()

        // 标记任务为运行状态
        mTask.isRunning = true
        // 执行任务的 `run` 方法
        mTask.run()

        // 执行任务的尾部任务（如果有）
        val tailRunnable = mTask.tailRunnable
        tailRunnable?.run()

        // 如果任务不需要回调，或者任务不在主线程执行，则直接标记任务完成
        if (!mTask.needCall() || !mTask.runOnMainThread()) {
            // 打印任务执行的日志
            printTaskLog(startTime, waitTime)
            // 标记任务完成
            TaskStat.markTaskDone()
            // 标记任务为已完成状态
            mTask.isFinished = true
            // 通知任务调度器，当前任务已完成
            mTaskDispatcher?.let {
                // 通知依赖当前任务的其他任务
                it.satisfyChildren(mTask)
                // 标记任务为已完成
                it.markTaskDone(mTask)
            }
            // 打印任务完成的日志
            DispatcherLog.i("${mTask.javaClass.simpleName} finish")
        }

        // 结束 TraceCompat 的记录
        TraceCompat.endSection()
    }

    /**
     * 打印任务执行的日志，包括任务的等待时间、执行时间、是否在主线程执行等信息。
     *
     * @param startTime 任务开始执行的时间
     * @param waitTime 任务等待的时间
     */
    private fun printTaskLog(startTime: Long, waitTime: Long) {
        // 计算任务的执行时间
        val runTime = System.currentTimeMillis() - startTime
        // 如果处于调试模式，则打印详细的日志信息
        if (DispatcherLog.isDebug) {
            DispatcherLog.i(
                mTask.javaClass.simpleName + "| wait：" + waitTime + "| run："
                        + runTime + "| isMain：" + (Looper.getMainLooper() == Looper.myLooper())
                        + "| needWait：" + (mTask.needWait() || Looper.getMainLooper() == Looper.myLooper())
                        + "| ThreadId：" + Thread.currentThread().id
                        + "| ThreadName：" + Thread.currentThread().name
                        + "| Situation：" + TaskStat.currentSituation
            )
        }
    }
}