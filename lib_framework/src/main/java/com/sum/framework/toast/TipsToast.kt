@file:JvmName("TipToast")

package com.sum.framework.toast

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sum.framework.R
import com.sum.framework.databinding.WidgetTipsToastBinding
import timber.log.Timber

/**
 * TipsToast 是一个单例对象，用于显示自定义的 Toast 提示。
 * 支持普通文本提示、成功提示和警告提示，并可以设置提示的显示时长。
 * 使用 ViewBinding 绑定自定义布局，确保 Toast 的样式统一。
 */
object TipsToast {

    // 用于显示 Toast 的对象
    private var toast: Toast? = null

    // 上下文对象，必须是 Application 上下文
    private lateinit var mContext: Application

    // 用于在主线程中延迟执行 Toast 显示的 Handler
    private val mToastHandler = Looper.myLooper()?.let { Handler(it) }

    // 使用 ViewBinding 绑定自定义 Toast 布局
    private val mBinding by lazy {
        WidgetTipsToastBinding.inflate(LayoutInflater.from(mContext), null, false)
    }

    /**
     * 初始化 TipsToast，必须在使用前调用。
     *
     * @param context Application 上下文
     */
    fun init(context: Application) {
        mContext = context
    }

    /**
     * 显示普通文本提示，默认时长为 Toast.LENGTH_SHORT。
     *
     * @param stringId 字符串资源 ID
     */
    fun showTips(@StringRes stringId: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, Toast.LENGTH_SHORT)
    }

    /**
     * 显示普通文本提示，默认时长为 Toast.LENGTH_SHORT。
     *
     * @param msg 提示文本
     */
    fun showTips(msg: String?) {
        showTipsImpl(msg, Toast.LENGTH_SHORT)
    }

    /**
     * 显示普通文本提示，可自定义时长。
     *
     * @param msg 提示文本
     * @param duration 显示时长（Toast.LENGTH_SHORT 或 Toast.LENGTH_LONG）
     */
    fun showTips(msg: String?, duration: Int) {
        showTipsImpl(msg, duration)
    }

    /**
     * 显示普通文本提示，可自定义时长。
     *
     * @param stringId 字符串资源 ID
     * @param duration 显示时长（Toast.LENGTH_SHORT 或 Toast.LENGTH_LONG）
     */
    fun showTips(@StringRes stringId: Int, duration: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, duration)
    }

    /**
     * 显示成功提示，默认时长为 Toast.LENGTH_SHORT。
     *
     * @param msg 提示文本
     */
    fun showSuccessTips(msg: String?) {
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_success)
    }

    /**
     * 显示成功提示，默认时长为 Toast.LENGTH_SHORT。
     *
     * @param stringId 字符串资源 ID
     */
    fun showSuccessTips(@StringRes stringId: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_success)
    }

    /**
     * 显示警告提示，默认时长为 Toast.LENGTH_SHORT。
     *
     * @param msg 提示文本
     */
    fun showWarningTips(msg: String?) {
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_warning)
    }

    /**
     * 显示警告提示，默认时长为 Toast.LENGTH_SHORT。
     *
     * @param stringId 字符串资源 ID
     */
    fun showWarningTips(@StringRes stringId: Int) {
        val msg = mContext.getString(stringId)
        showTipsImpl(msg, Toast.LENGTH_SHORT, R.mipmap.widget_toast_warning)
    }

    /**
     * 显示 Toast 的核心实现方法。
     *
     * @param msg 提示文本
     * @param duration 显示时长（Toast.LENGTH_SHORT 或 Toast.LENGTH_LONG）
     * @param drawableId 左侧图标资源 ID（可选，默认为 0）
     */
    private fun showTipsImpl(
        msg: String?,
        duration: Int,
        @DrawableRes drawableId: Int = 0,
    ) {
        // 如果提示文本为空，直接返回
        if (msg.isNullOrEmpty()) {
            return
        }

        // 如果当前有 Toast 正在显示，先取消
        toast?.let {
            cancel()
            toast = null
        }

        // 使用 Handler 延迟 50ms 显示 Toast，避免频繁调用时 Toast 重叠
        mToastHandler?.postDelayed({
            try {
                // 创建 Toast 对象
                toast = Toast(mContext)
                // 设置自定义布局
                toast?.view = mBinding.root
                // 设置提示文本
                mBinding.tipToastTxt.text = msg
                // 设置左侧图标（如果有）
                mBinding.tipToastTxt.setCompoundDrawablesWithIntrinsicBounds(
                    drawableId,
                    0,
                    0,
                    0
                )
                // 设置 Toast 居中显示
                toast?.setGravity(Gravity.CENTER, 0, 0)
                // 设置显示时长
                toast?.duration = duration
                // 显示 Toast
                toast?.show()
            } catch (e: Exception) {
                // 捕获并打印异常
                e.printStackTrace()
                Timber.tag("show tips error").e(e)
            }
        }, 50)
    }

    /**
     * 取消当前显示的 Toast。
     */
    fun cancel() {
        toast?.cancel()
        mToastHandler?.removeCallbacksAndMessages(null)
    }
}