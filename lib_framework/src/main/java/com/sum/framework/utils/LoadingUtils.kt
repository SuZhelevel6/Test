package com.sum.framework.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import com.sum.framework.loading.CenterLoadingView
import com.sum.framework.R

/**
 * 等待提示框
 */
class LoadingUtils(private val mContext: Context) {
    private val loadView  by lazy {
        CenterLoadingView(mContext, R.style.dialog)
    }

    /**
     * 统一耗时操作Dialog
     */
    fun showLoading(txt: String?) {
        if (loadView.isShowing) {
            loadView.dismiss()
        }
        if (!TextUtils.isEmpty(txt)) {
            loadView.setTitle(txt as CharSequence)
        }
        if (mContext is Activity && mContext.isFinishing) {
            return
        }
        loadView.show()
    }

    /**
     * 关闭Dialog
     */
    fun dismissLoading() {
        if (mContext is Activity && mContext.isFinishing) {
            return
        }

        loadView.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}
