package com.sum.framework.manager

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.sum.framework.log.LogUtil
import com.sum.framework.utils.DeviceInfoUtils
import kotlin.math.max
import kotlin.math.min

/**
 * AppManager 是一个用于管理应用全局信息的工具类。
 * 它提供了以下功能：
 * 1. 屏幕信息管理：获取屏幕的宽度、高度、密度、状态栏高度等信息，并提供 dp 和 px 之间的转换。
 * 2. 设备信息管理：获取设备的品牌、型号、系统版本号等信息，并判断是否为魅族设备和是否有 SmartBar。
 * 3. 应用版本管理：获取应用的版本名称和版本号。
 * 4. 工具方法：提供了一些常用的工具方法，如 `dip2px` 用于将 dp 值转换为 px 值。
 *
 * 使用场景：
 * - 屏幕适配：通过获取屏幕的宽度、高度和密度信息，帮助开发者进行屏幕适配。
 * - 设备信息获取：获取设备的品牌、型号和系统版本号，用于设备兼容性处理。
 * - 应用版本管理：获取应用的版本信息，用于版本更新和兼容性处理。
 *
 * 示例用法：
 * ```kotlin
 * // 初始化 AppManager
 * AppManager.init(application)
 *
 * // 获取屏幕宽度（像素）
 * val screenWidthPx = AppManager.getScreenWidthPx()
 *
 * // 获取屏幕高度（像素）
 * val screenHeightPx = AppManager.getScreenHeightPx()
 *
 * // 获取状态栏高度
 * val statusBarHeight = AppManager.getStatusBarHeight()
 *
 * // 判断是否为大屏幕设备
 * val isBiggerScreen = AppManager.isBiggerScreen()
 *
 * // 获取应用版本名称
 * val versionName = AppManager.getAppVersionName(context)
 * ```
 */
object AppManager {

    // 日志标签
    private val TAG = AppManager::class.java.simpleName

    // 应用的上下文
    private lateinit var mContext: Application

    // 屏幕宽度（像素）
    private var mScreenWidthPx = 0

    // 屏幕高度（像素）
    private var mScreenHeightPx = 0

    // 屏幕宽度（dp）
    private var mScreenWidthDp = 0

    // 屏幕高度（dp）
    private var mScreenHeightDp = 0

    // 屏幕密度（dpi）
    private var mDensityDpi = 0

    // 屏幕密度比例（density）
    private var mDensity = 0f

    // 状态栏高度（像素）
    private var mStatusBarHeight = 0

    // 设备型号（处理后的字符串）
    private var mProductType: String? = null

    // 是否为大屏幕设备（屏幕宽高比大于 16:9）
    private var mIsBiggerScreen = false

    /**
     * 初始化 AppManager，获取屏幕信息和设备信息。
     *
     * @param application 应用的上下文
     */
    fun init(application: Application) {
        mContext = application
        // 获取 WindowManager 服务
        val windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        // 获取屏幕的显示信息
        windowManager.defaultDisplay.getMetrics(metrics)

        // 获取屏幕的宽度和高度（像素），确保高度大于宽度
        mScreenHeightPx = max(metrics.heightPixels, metrics.widthPixels)
        mScreenWidthPx = min(metrics.heightPixels, metrics.widthPixels)

        // 判断是否为大屏幕设备（屏幕宽高比大于 16:9）
        mIsBiggerScreen = mScreenHeightPx * 1.0 / mScreenWidthPx > 16.0 / 9

        // 获取屏幕的密度信息
        mDensityDpi = metrics.densityDpi
        mDensity = metrics.density

        // 计算屏幕的宽度和高度（dp）
        mScreenHeightDp = (mScreenHeightPx / mDensity).toInt()
        mScreenWidthDp = (mScreenWidthPx / mDensity).toInt()

        // 获取状态栏高度
        val resourceId = application.resources.getIdentifier("status_bar_height", "dimen", "android")
        mStatusBarHeight = application.resources.getDimensionPixelSize(resourceId)

        // 生成设备型号（处理后的字符串）
        mProductType = genProductType()
    }

    /**
     * 获取屏幕宽度（像素）。
     *
     * @return 屏幕宽度（像素）
     */
    fun getScreenWidthPx(): Int {
        return mScreenWidthPx
    }

    /**
     * 获取屏幕高度（像素）。
     *
     * @return 屏幕高度（像素）
     */
    fun getScreenHeightPx(): Int {
        return mScreenHeightPx
    }

    /**
     * 获取屏幕内容高度（像素），即屏幕高度减去状态栏高度。
     *
     * @return 屏幕内容高度（像素）
     */
    fun getScreenContentHeightPx(): Int {
        return mScreenHeightPx - getStatusBarHeight()
    }

    /**
     * 获取屏幕宽度（dp）。
     *
     * @return 屏幕宽度（dp）
     */
    fun getScreenWidthDp(): Int {
        return mScreenWidthDp
    }

    /**
     * 获取屏幕高度（dp）。
     *
     * @return 屏幕高度（dp）
     */
    fun getScreenHeightDp(): Int {
        return mScreenHeightDp
    }

    /**
     * 获取屏幕密度（dpi）。
     *
     * @return 屏幕密度（dpi）
     */
    fun getDensityDpi(): Int {
        return mDensityDpi
    }

    /**
     * 获取屏幕密度比例（density）。
     *
     * @return 屏幕密度比例（density）
     */
    fun getDensity(): Float {
        return mDensity
    }

    /**
     * 获取状态栏高度（像素）。
     *
     * @return 状态栏高度（像素）
     */
    fun getStatusBarHeight(): Int {
        return mStatusBarHeight
    }

    /**
     * 获取设备型号（处理后的字符串）。
     *
     * @return 设备型号
     */
    fun getProductType(): String? {
        return mProductType
    }

    /**
     * 生成设备型号（处理后的字符串）。
     *
     * @return 设备型号
     */
    private fun genProductType(): String? {
        val model = DeviceInfoUtils.phoneModel
        // 去除设备型号中的特殊字符
        return model.replace("[:{} \\[\\]\"']*".toRegex(), "")
    }

    /**
     * 获取魅族 SmartBar 的高度（像素）。
     *
     * @return SmartBar 的高度（像素），如果不是魅族设备或没有 SmartBar，则返回 0
     */
    fun getSmartBarHeight(): Int {
        // 如果是魅族设备并且有 SmartBar
        if (isMeizu() && hasSmartBar()) {
            // 判断 SmartBar 是否自动隐藏
            val autoHideSmartBar = Settings.System.getInt(
                mContext.contentResolver,
                "mz_smartbar_auto_hide", 0
            ) == 1
            // 如果 SmartBar 自动隐藏，则返回 0，否则返回正常导航栏高度
            return if (autoHideSmartBar) {
                0
            } else {
                getNormalNavigationBarHeight()
            }
        }
        return 0
    }

    /**
     * 获取正常导航栏的高度（像素）。
     *
     * @return 导航栏高度（像素），如果没有导航栏，则返回 0
     */
    private fun getNormalNavigationBarHeight(): Int {
        try {
            val res: Resources = mContext.resources
            // 获取系统是否显示导航栏的配置
            val rid = res.getIdentifier("config_showNavigationBar", "bool", "android")
            if (rid > 0) {
                val flag = res.getBoolean(rid)
                if (flag) {
                    // 获取导航栏高度的资源 ID
                    val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                    if (resourceId > 0) {
                        return res.getDimensionPixelSize(resourceId)
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 判断当前设备是否为魅族手机。
     *
     * @return 如果是魅族手机，则返回 true，否则返回 false
     */
    fun isMeizu(): Boolean {
        return Build.MANUFACTURER.equals("Meizu", ignoreCase = true)
    }

    /**
     * 判断当前设备是否有 SmartBar。
     *
     * @return 如果有 SmartBar，则返回 true，否则返回 false
     */
    fun hasSmartBar(): Boolean {
        try {
            // 通过反射调用 Build.hasSmartBar() 方法
            val method = Class.forName("android.os.Build").getMethod("hasSmartBar")
            return method.invoke(null) as Boolean
        } catch (e: Exception) {
            Log.e(TAG, "hasSmartBar", e)
        }
        // 如果反射失败，则通过 Build.DEVICE 判断
        if (Build.DEVICE == "mx2") {
            return true
        } else if (Build.DEVICE == "mx" || Build.DEVICE == "m9") {
            return false
        }
        return false
    }

    /**
     * 判断当前设备是否为大屏幕设备（屏幕宽高比大于 16:9）。
     *
     * @return 如果是大屏幕设备，则返回 true，否则返回 false
     */
    fun isBiggerScreen(): Boolean {
        return mIsBiggerScreen
    }

    /**
     * 获取手机品牌。
     *
     * @return 手机品牌
     */
    fun getDeviceBuildBrand(): String {
        return Build.BRAND ?: ""
    }

    /**
     * 获取手机型号。
     *
     * @return 手机型号
     */
    fun getDeviceBuildModel(): String {
        return DeviceInfoUtils.phoneModel
    }

    /**
     * 获取手机系统版本号。
     *
     * @return 系统版本号
     */
    fun getDeviceBuildRelease(): String {
        return Build.VERSION.RELEASE ?: ""
    }

    /**
     * 将 dp 值转换为 px 值。
     *
     * @param dipValue dp 值
     * @return px 值
     */
    fun dip2px(dipValue: Float): Int {
        return (dipValue * mDensity + 0.5f).toInt()
    }

    /**
     * 获取应用的版本名称。
     *
     * @param context 上下文
     * @return 版本名称
     */
    fun getAppVersionName(context: Context): String {
        var versionName = ""
        try {
            val pm = context.packageManager
            val packageName = context.packageName ?: "com.sum.tea"
            val pi = pm.getPackageInfo(packageName, 0)
            versionName = pi.versionName
            if (versionName.isNullOrEmpty()) {
                return ""
            }
        } catch (e: Exception) {
            LogUtil.e("VersionInfo", e)
        }
        return versionName
    }

    /**
     * 获取应用的版本号。
     *
     * @param context 上下文
     * @return 版本号
     */
    fun getAppVersionCode(context: Context): Long {
        var appVersionCode: Long = 0
        try {
            val packageName = context.packageName ?: "com.sum.tea"
            val packageInfo = context.applicationContext
                .packageManager
                .getPackageInfo(packageName, 0)
            appVersionCode = packageInfo.versionCode.toLong()
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtil.e("getAppVersionCode-${e.message}")
        }
        return appVersionCode
    }

    /**
     * 返回 AppManager 的字符串表示，包含屏幕信息和设备信息。
     *
     * @return 字符串表示
     */
    override fun toString(): String {
        return ("PhoneInfoManager{"
                + "mScreenWidthPx="
                + mScreenWidthPx
                + ", mScreenHeightPx="
                + mScreenHeightPx
                + ", mScreenWidthDp="
                + mScreenWidthDp
                + ", mScreenHeightDp="
                + mScreenHeightDp
                + ", mDensityDpi="
                + mDensityDpi
                + ", mDensity="
                + mDensity
                + ", mStatusBarHeight="
                + mStatusBarHeight
                + '}')
    }
}