package com.sum.tea

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import timber.log.Timber
import com.sum.framework.log.TimberFormatTree
import com.sum.framework.manager.ActivityManager
import com.sum.framework.manager.AppFrontBack
import com.sum.framework.manager.AppFrontBackListener
import com.sum.framework.toast.TipsToast
import com.sum.tea.task.*


class SumApplication  : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化 Timber
        Timber.plant(TimberFormatTree())
        // 注册APP前后台切换监听
        appFrontBackRegister()
        // 注册Activity生命周期监听
        registerActivityLifecycle()
        // 初始化Toast
        TipsToast.init(this)

//        //1.启动器：TaskDispatcher初始化
//        TaskDispatcher.init(this)
//        //2.创建dispatcher实例
//        val dispatcher: TaskDispatcher = TaskDispatcher.createInstance()
//
//        //3.添加任务并且启动任务
//        dispatcher.addTask(InitSumHelperTask(this))
//            .addTask(InitMmkvTask())
//            .addTask(InitAppManagerTask())
//            .addTask(InitRefreshLayoutTask())
//            .addTask(InitArouterTask())
//            .start()
//
//        //4.等待，需要等待的方法执行完才可以往下执行
//        dispatcher.await()
    }

    /**
     * 注册APP前后台切换监听
     */
    private fun appFrontBackRegister() {
        AppFrontBack.register(this, object : AppFrontBackListener {
            override fun onBack(activity: Activity?) {
                Timber.d("${activity?.applicationInfo?.name} onBack")
            }

            override fun onFront(activity: Activity?) {
                Timber.d("${activity?.applicationInfo?.name} onFront")
            }
        })
    }

    /**
     * 注册Activity生命周期监听
     */
    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.pop(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                ActivityManager.push(activity)
            }

            override fun onActivityResumed(activity: Activity) {
            }
        })
    }

}