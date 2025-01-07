package com.sum.stater.dispatcher

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import com.sum.stater.task.DispatchRunnable
import com.sum.stater.task.Task
import java.util.LinkedList
import java.util.Queue

/**
 * 延迟初始化调度器，利用 Android 的 IdleHandler 机制，在主线程空闲时执行任务。
 * 适用于那些不需要立即执行、但需要在主线程空闲时执行的任务。
 */
class DelayInitDispatcher {

    // 延迟任务队列，用于存储需要延迟执行的任务
    private val mDelayTasks: Queue<Task> = LinkedList()

    // IdleHandler，用于在主线程空闲时执行任务
    private val mIdleHandler = IdleHandler {
        // 如果任务队列中有任务
        if (mDelayTasks.size > 0) {
            // 从队列中取出一个任务
            val task = mDelayTasks.poll()
            task?.let {
                // 执行任务
                DispatchRunnable(it).run()
            }
        }
        // 如果任务队列不为空，则继续等待下一次空闲时执行
        !mDelayTasks.isEmpty()
    }

    /**
     * 添加任务到延迟任务队列中。
     *
     * @param task 需要延迟执行的任务
     * @return 返回当前 DelayInitDispatcher 实例，支持链式调用
     */
    fun addTask(task: Task): DelayInitDispatcher {
        // 将任务添加到延迟任务队列中
        mDelayTasks.add(task)
        return this
    }

    /**
     * 开启延迟初始化调度器，将 IdleHandler 添加到主线程的消息队列中。
     * 当主线程空闲时，IdleHandler 会被触发，执行延迟任务。
     */
    fun start() {
        // 将 IdleHandler 添加到主线程的消息队列中
        Looper.myQueue().addIdleHandler(mIdleHandler)
    }
}