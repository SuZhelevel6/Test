import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.sum.main"
    compileSdk = AndroidConfig.compileSdk

    //ARouter
    kapt {
        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
    }

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
    buildFeatures{
        viewBinding = true
        dataBinding = true
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
    implementation(project(":lib_framework"))
    implementation(project(":lib_common"))
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly(DependenciesLibraries.coreKtx)
    compileOnly(DependenciesLibraries.appcompat)
    compileOnly(DependenciesLibraries.tbssdk)
    compileOnly(DependenciesLibraries.timber)
    compileOnly(DependenciesLibraries.gson)
    compileOnly(DependenciesLibraries.refreshLayout)
    compileOnly(DependenciesLibraries.viewpager2)
    testImplementation(DependenciesLibraries.junit)
    testImplementation(DependenciesLibraries.extJunit)
    testImplementation(DependenciesLibraries.espressoCore)

}