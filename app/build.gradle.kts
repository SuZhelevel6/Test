plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
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
    implementation(DependenciesLibraries.coreKtx)
    implementation(DependenciesLibraries.lifecycleRuntimeKtx)
    implementation(DependenciesLibraries.activityCompose)
    implementation(platform(DependenciesLibraries.composeBom))
    implementation(DependenciesLibraries.composeUi)
    implementation(DependenciesLibraries.composeUiGraphics)
    implementation(DependenciesLibraries.composeUiToolingPreview)
    implementation(DependenciesLibraries.composeMaterial3)
    testImplementation(DependenciesLibraries.junit)
    androidTestImplementation(DependenciesLibraries.extJunit)
    androidTestImplementation(DependenciesLibraries.espressoCore)
    androidTestImplementation(platform(DependenciesLibraries.composeBom))
    androidTestImplementation(DependenciesLibraries.composeUiTestJunit4)
    debugImplementation(DependenciesLibraries.composeUiTooling)
    debugImplementation(DependenciesLibraries.composeUiTestManifest)
}