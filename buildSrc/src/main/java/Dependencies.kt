// 定义 Android 配置
object AndroidConfig {
    const val namespace = "com.sum.tea"
    const val compileSdk = 34
    const val minSdk = 30
    const val targetSdk = 34
    const val versionCode = 1
    const val versionName = "1.0"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val jvmTarget = "1.8"
}

// 定义签名配置
object SigningConfigs {
    const val debugStoreFile = "D:\\Project\\keystore\\androiddebugkey.keystore"
    const val debugStorePassword = "android"
    const val debugKeyAlias = "androiddebugkey"
    const val debugKeyPassword = "android"

    const val releaseStoreFile = "D:\\Project\\keystore\\androiddebugkey.keystore"
    const val releaseStorePassword = "android"
    const val releaseKeyAlias = "androiddebugkey"
    const val releaseKeyPassword = "android"
}

// 定义依赖版本
object DependenciesVersions {
    const val coreKtx = "1.13.0"
    const val lifecycleRuntimeKtx = "2.8.7"
    const val activityCompose = "1.9.3"
    const val composeBom = "2023.08.00"
    const val junit = "4.13.2"
    const val extJunit = "1.2.1"
    const val espressoCore = "3.6.1"
    const val viewpager2 = "1.1.0"
    const val timber = "5.0.1"
    const val appcompat = "1.4.1"
    const val multidex = "2.0.1"
    const val retrofit = "2.9.0"
    const val retrofitGson = "2.4.0"
    const val okhttp3Log = "3.11.0"
    const val gson = "2.10.1"
    const val mmkv = "1.2.15"
    const val refresh = "2.0.5"
    const val glide = "4.15.0"
    const val flexbox = "3.0.0"
    const val arouterApi = "1.5.2"
    const val arouterCompiler = "1.5.2"
    const val tbssdk = "44132"
    const val exoPlayer = "2.18.5"
    const val permissions = "0.12"
    const val room = "2.6.1"
    const val rxjava = "3.1.6"
    const val rxandroid = "3.0.2"
    const val camerax = "1.3.0-alpha04"
    const val material = "1.4.0"
}

// 定义依赖库
object DependenciesLibraries {
    // Android 核心库
    const val coreKtx = "androidx.core:core-ktx:${DependenciesVersions.coreKtx}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.lifecycleRuntimeKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${DependenciesVersions.appcompat}"
    const val multidex = "androidx.multidex:multidex:${DependenciesVersions.multidex}"
    const val material = "com.google.android.material:material:${DependenciesVersions.material}"

    // Compose 相关
    const val activityCompose = "androidx.activity:activity-compose:${DependenciesVersions.activityCompose}"
    const val composeBom = "androidx.compose:compose-bom:${DependenciesVersions.composeBom}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial3 = "androidx.compose.material3:material3"
    const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest"

    // UI 组件
    const val viewpager2 = "androidx.viewpager2:viewpager2:${DependenciesVersions.viewpager2}"
    const val flexbox = "com.google.android.flexbox:flexbox:${DependenciesVersions.flexbox}"

    // 日志
    const val timber = "com.jakewharton.timber:timber:${DependenciesVersions.timber}"

    // 测试
    const val junit = "junit:junit:${DependenciesVersions.junit}"
    const val extJunit = "androidx.test.ext:junit:${DependenciesVersions.extJunit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${DependenciesVersions.espressoCore}"

    // 网络请求
    const val retrofit = "com.squareup.retrofit2:retrofit:${DependenciesVersions.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${DependenciesVersions.retrofitGson}"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${DependenciesVersions.okhttp3Log}"

    // JSON 解析
    const val gson = "com.google.code.gson:gson:${DependenciesVersions.gson}"

    // 数据存储
    const val mmkv = "com.tencent:mmkv:${DependenciesVersions.mmkv}"
    const val roomKtx = "androidx.room:room-ktx:${DependenciesVersions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${DependenciesVersions.room}"

    // 下拉刷新
    const val refreshLayout = "io.github.scwang90:refresh-layout-kernel:${DependenciesVersions.refresh}"
    const val refreshHeader = "io.github.scwang90:refresh-header-classics:${DependenciesVersions.refresh}"
    const val refreshFooter = "io.github.scwang90:refresh-footer-classics:${DependenciesVersions.refresh}"

    // 图片加载
    const val glide = "com.github.bumptech.glide:glide:${DependenciesVersions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${DependenciesVersions.glide}"

    // 路由
    const val arouterApi = "com.alibaba:arouter-api:${DependenciesVersions.arouterApi}"
    const val arouterCompiler = "com.alibaba:arouter-compiler:${DependenciesVersions.arouterCompiler}"

    // 腾讯 X5
    const val tbssdk = "com.tencent.tbs:tbssdk:${DependenciesVersions.tbssdk}"

    // 媒体播放
    const val exoPlayer = "com.google.android.exoplayer:exoplayer:${DependenciesVersions.exoPlayer}"

    // CameraX
    const val cameraX = "androidx.camera:camera-camera2:${DependenciesVersions.camerax}"

    // 权限
    const val rxPermission = "com.github.tbruyelle:rxpermissions:${DependenciesVersions.permissions}"
    const val rxjava = "io.reactivex.rxjava3:rxjava:${DependenciesVersions.rxjava}"
    const val rxandroid = "io.reactivex.rxjava3:rxandroid:${DependenciesVersions.rxandroid}"
}