package com.sum.tea

import android.app.Application
import android.content.Context
import timber.log.Timber
import com.sum.framework.log.TimberFormatTree


class SumApplication  : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化 Timber
        Timber.plant(TimberFormatTree())


    }

}