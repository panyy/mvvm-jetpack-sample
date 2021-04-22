package com.common.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * desc :插件配置
 * author：panyy
 * date：2021/04/22
 */
class PluginApp : Plugin<Project> {
    override fun apply(project: Project) {
        project.configurePlugins(true)
        project.configureAndroid(true)
        project.configureDependencies()
    }
}