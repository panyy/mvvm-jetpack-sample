package com.common.plugin

import isRunAlone
import org.gradle.api.Project

/**
 * desc :公共插件
 * author：panyy
 * date：2021/04/22
 */
internal fun Project.configurePlugins(isAppModule: Boolean) {
    if (isAppModule || isRunAlone) {
        plugins.apply("com.android.application")
        var options = mutableMapOf<String, String>().apply {
            @Suppress("MISSING_DEPENDENCY_CLASS")
            put("from", "${project.rootDir}/build_app.gradle")
        }
        apply(options)
    } else {
        plugins.apply("com.android.library")
    }
    plugins.apply("kotlin-android")
    plugins.apply("kotlin-kapt")
    plugins.apply("kotlin-android-extensions")
}