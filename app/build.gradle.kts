plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt") // 应用 kotlin-kapt 插件, Kotlin 注解处理工具
}

android {
    namespace = AndroidConfig.namespace
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        applicationId = AndroidConfig.namespace
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk
        versionCode = AndroidConfig.versionCode
        versionName = AndroidConfig.versionName

        testInstrumentationRunner = AndroidConfig.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(SigningConfigs.debugStoreFile)
            storePassword = SigningConfigs.debugStorePassword
            keyAlias = SigningConfigs.debugKeyAlias
            keyPassword = SigningConfigs.debugKeyPassword
        }
        create("release") {
            storeFile = file(SigningConfigs.releaseStoreFile)
            storePassword = SigningConfigs.releaseStorePassword
            keyAlias = SigningConfigs.releaseKeyAlias
            keyPassword = SigningConfigs.releaseKeyPassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = AndroidConfig.jvmTarget
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Android 核心库
    implementation(DependenciesLibraries.coreKtx)
    implementation(DependenciesLibraries.lifecycleRuntimeKtx)
    implementation(DependenciesLibraries.appcompat)
    implementation(DependenciesLibraries.multidex)
    implementation(DependenciesLibraries.material)

    // Compose 相关
    implementation(DependenciesLibraries.activityCompose)
    implementation(platform(DependenciesLibraries.composeBom))
    implementation(DependenciesLibraries.composeUi)
    implementation(DependenciesLibraries.composeUiGraphics)
    implementation(DependenciesLibraries.composeUiToolingPreview)
    implementation(DependenciesLibraries.composeMaterial3)
    androidTestImplementation(platform(DependenciesLibraries.composeBom))
    androidTestImplementation(DependenciesLibraries.composeUiTestJunit4)
    debugImplementation(DependenciesLibraries.composeUiTooling)
    debugImplementation(DependenciesLibraries.composeUiTestManifest)

    // UI 组件
    implementation(DependenciesLibraries.viewpager2)
    implementation(DependenciesLibraries.flexbox)

    // 日志
    implementation(DependenciesLibraries.timber)

    // 测试
    testImplementation(DependenciesLibraries.junit)
    androidTestImplementation(DependenciesLibraries.extJunit)
    androidTestImplementation(DependenciesLibraries.espressoCore)

    // 网络请求
    implementation(DependenciesLibraries.retrofit)
    implementation(DependenciesLibraries.retrofitGson)
    implementation(DependenciesLibraries.loggingInterceptor)

    // JSON 解析
    implementation(DependenciesLibraries.gson)

    // 数据存储
    implementation(DependenciesLibraries.mmkv)
    implementation(DependenciesLibraries.roomKtx)
    kapt(DependenciesLibraries.roomCompiler)

    // 下拉刷新
    implementation(DependenciesLibraries.refreshLayout)
    implementation(DependenciesLibraries.refreshHeader)
    implementation(DependenciesLibraries.refreshFooter)

    // 图片加载
    implementation(DependenciesLibraries.glide)
    kapt(DependenciesLibraries.glideCompiler)

    // 路由
    implementation(DependenciesLibraries.arouterApi)
    kapt(DependenciesLibraries.arouterCompiler)

    // 腾讯 X5
    implementation(DependenciesLibraries.tbssdk)

    // 媒体播放
    implementation(DependenciesLibraries.exoPlayer)

    // CameraX
    implementation(DependenciesLibraries.cameraX)

    // 权限
    implementation(DependenciesLibraries.rxPermission)
    implementation(DependenciesLibraries.rxjava)
    implementation(DependenciesLibraries.rxandroid)

    implementation(project(":lib_framework"))
}