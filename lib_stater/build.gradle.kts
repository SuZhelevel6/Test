plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.sum.stater"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = AndroidConfig.jvmTarget
    }
}

dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly(DependenciesLibraries.coreKtx)
    compileOnly(DependenciesLibraries.appcompat)
    compileOnly(DependenciesLibraries.tbssdk)
    compileOnly(DependenciesLibraries.timber)
    compileOnly(DependenciesLibraries.gson)
    compileOnly(DependenciesLibraries.refreshLayout)
    compileOnly(DependenciesLibraries.viewpager2)
    compileOnly(DependenciesLibraries.material)
    testImplementation(DependenciesLibraries.junit)
    testImplementation(DependenciesLibraries.extJunit)
    testImplementation(DependenciesLibraries.espressoCore)

    compileOnly(project(":lib_framework"))
}