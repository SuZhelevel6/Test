package com.sum.framework.log

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * 这是一个自定义的Timber日志打印类，可以在日志中添加时间戳
 */
class TimberFormatTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val customMessage = String.format("[%s] %s: %s", currentTimestamp, tag, message)
        super.log(priority, tag, customMessage, t)
    }

    private val currentTimestamp: String
        get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
}