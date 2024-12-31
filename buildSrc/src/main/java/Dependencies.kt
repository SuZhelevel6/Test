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
}

// 定义依赖库
object DependenciesLibraries {
    const val coreKtx = "androidx.core:core-ktx:${DependenciesVersions.coreKtx}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.lifecycleRuntimeKtx}"
    const val activityCompose = "androidx.activity:activity-compose:${DependenciesVersions.activityCompose}"
    const val composeBom = "androidx.compose:compose-bom:${DependenciesVersions.composeBom}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial3 = "androidx.compose.material3:material3"
    const val junit = "junit:junit:${DependenciesVersions.junit}"
    const val extJunit = "androidx.test.ext:junit:${DependenciesVersions.extJunit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${DependenciesVersions.espressoCore}"
    const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest"
}