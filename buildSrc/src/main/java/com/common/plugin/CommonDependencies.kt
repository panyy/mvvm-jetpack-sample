package com.common.plugin

import Deps
import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Project

/**
 * desc :公共依赖
 * author：panyy
 * date：2021/04/22
 */
internal fun Project.configureDependencies() = dependencies.apply {
    add("implementation", (fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))))
    add("testImplementation", Deps.junit)

    if (project.containsAndroidPlugin()) {
        add("androidTestImplementation", Deps.extJunit)
        add("androidTestImplementation", Deps.espressoCore)
    }

    add("kapt", Deps.arouterCompiler)
}

internal fun Project.containsAndroidPlugin(): Boolean {
    return project.plugins.toList().any { plugin -> plugin is AndroidBasePlugin }
}