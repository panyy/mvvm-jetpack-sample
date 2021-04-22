package com.common.plugin

import Deps
import Versions
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.SigningConfig
import isRunAlone
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

/**
 * desc :android{} 配置
 * author：panyy
 * date：2021/04/22
 */
internal fun Project.configureAndroid(isAppModule: Boolean) {
    var extension =
            if (isAppModule || isRunAlone)
                extensions.getByType<BaseAppModuleExtension>()
            else
                extensions.getByType<LibraryExtension>()

    extension.run {

        buildToolsVersion(Versions.buildTool)
        compileSdkVersion(Versions.compileSdk)

        defaultConfig {
            minSdkVersion(Versions.minSdk)
            targetSdkVersion(Versions.targetSdk)
            testInstrumentationRunner = Deps.androidJUnitRunner
            multiDexEnabled = true
            flavorDimensions("default")
            ndk {
                // 设置支持的SO库架构
                abiFilters.add("armeabi-v7a")  //'armeabi', 'x86', 'armeabi-v7a', 'x86_64', 'arm64-v8a'
            }
            javaCompileOptions {
                annotationProcessorOptions {
                    argument("AROUTER_MODULE_NAME", project.name)
                }
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }

        if (isAppModule || isRunAlone) {
            extensions.getByType<BaseAppModuleExtension>().buildFeatures {
                dataBinding = true
                viewBinding = true
            }
        } else {
            extensions.getByType<LibraryExtension>().buildFeatures {
                dataBinding = true
                viewBinding = true
            }
        }

        sourceSets {
            getByName("main") {
                if (isRunAlone) {
                    @Suppress("MISSING_DEPENDENCY_CLASS")
                    var debugManifest = File("${project.projectDir}/src/main/debug/AndroidManifest.xml")
                    if (debugManifest.exists()) {
                        manifest.srcFile("src/main/debug/AndroidManifest.xml")
                    } else {
                        manifest.srcFile("src/main/AndroidManifest.xml")
                    }
                } else {
                    manifest.srcFile("src/main/AndroidManifest.xml")
                }
            }
        }

        buildTypes {
            @Suppress("MISSING_DEPENDENCY_CLASS")
            var signingConfig = SigningConfig("sample").apply {
                setStoreFile(File("${project.rootDir}/buildSrc/sample.jks"))
                setStorePassword("sample")
                setKeyAlias("sample")
                setKeyPassword("sample")
            }
            getByName("debug") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                setSigningConfig(signingConfig)
            }
            getByName("release") {
                isMinifyEnabled = false
                isZipAlignEnabled = false
                if (isAppModule || isRunAlone) {
                    isShrinkResources = false
                }
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                setSigningConfig(signingConfig)
            }
        }

        packagingOptions {
            exclude("META-INF/NOTICE.txt")
            // ...
        }
    }
}