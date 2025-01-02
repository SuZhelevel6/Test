package com.sum.framework.loading

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.sum.framework.R
import com.sum.framework.databinding.DialogLoadingBinding

/**
 * 通用加载中弹框，显示一个旋转的加载动画。
 *
 * @param context 上下文对象，通常是 Activity 或 Application。
 * @param theme 对话框的主题样式。
 */
class CenterLoadingView(context: Context, theme: Int) : Dialog(context, R.style.loading_dialog) {

    // 使用 ViewBinding 绑定布局
    private var mBinding: DialogLoadingBinding

    // 旋转动画对象
    private var animation: Animation? = null

    /**
     * 初始化对话框。
     */
    init {
        // 设置对话框无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 使用 ViewBinding 加载布局
        mBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))

        // 设置对话框的内容视图
        setContentView(mBinding.root)

        // 初始化旋转动画
        initAnim()
    }

    /**
     * 初始化旋转动画。
     */
    private fun initAnim() {
        // 创建一个旋转动画
        animation = RotateAnimation(
            0f, // 起始角度
            360f, // 结束角度
            Animation.RELATIVE_TO_SELF, // 旋转中心点的 X 轴相对类型
            0.5f, // 旋转中心点的 X 轴相对值（0.5 表示中心）
            Animation.RELATIVE_TO_SELF, // 旋转中心点的 Y 轴相对类型
            0.5f // 旋转中心点的 Y 轴相对值（0.5 表示中心）
        )

        // 设置动画持续时间（2 秒）
        animation?.duration = 2000

        // 设置动画重复次数（40 次）
        animation?.repeatCount = 40

        // 设置动画结束后保持最后一帧的状态
        animation?.fillAfter = true
    }

    override fun show() {
        super.show()
        mBinding.ivImage.startAnimation(animation)
    }

    override fun dismiss() {
        super.dismiss()
        mBinding.ivImage.clearAnimation()
    }

    override fun setTitle(title: CharSequence?) {
        if (!title.isNullOrEmpty()) {
            mBinding.tvMsg.text = title
        }
    }
}
