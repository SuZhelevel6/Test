pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.aliyun.com/repository/google") } // 阿里云的 Google 镜像
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") } // 阿里云的 Gradle 插件镜像
    }
}

rootProject.name = "tea"
include(":app")
include(":lib_framework")
include(":lib_common")
include(":mod_main")

include(":lib_stater")
