### 相关内容
* 组件化、支持模块单独运行
* androidx
* mvvm
* kotlin
* koin
* jetpack(livedata、viewmodel、lifecycle、viewbinding、...)
* buildsrc
* coroutines
* liveeventbus
* ...

### APK下载体验
[https://www.pgyer.com/Ispr](https://www.pgyer.com/Ispr)

### 如何检查依赖库的版本更新
在项目的根目录下执行以下命令。
```
./gradlew dependencyUpdates
```
会在当前目录下生成 build/dependencyUpdates/report.txt 文件，内容如下所示：
```
The following dependencies have later release versions:
 - androidx.swiperefreshlayout:swiperefreshlayout [1.0.0 -> 1.1.0]
     https://developer.android.com/jetpack/androidx
 - com.squareup.okhttp3:logging-interceptor [3.9.0 -> 4.7.2]
     https://square.github.io/okhttp/
 - junit:junit [4.12 -> 4.13]
     http://junit.org
 - org.koin:koin-android [2.1.5 -> 2.1.6]
 - org.koin:koin-androidx-viewmodel [2.1.5 -> 2.1.6]
 - org.koin:koin-core [2.1.5 -> 2.1.6]
Gradle release-candidate updates:
 - Gradle: [6.1.1 -> 6.5.1]
```
会列出所有需要更新的依赖库的最新版本，并且 Gradle Versions Plugin 比 AndroidStudio 所支持的更加全面：
* 支持手动方式管理依赖库最新版本检查
* 支持 ext 的方式管理依赖库最新版本检查
* 支持 buildSrc 方式管理依赖库最新版本检查
* 支持 gradle-wrapper 最新版本检查
* 支持多模块的依赖库最新版本检查
* 支持多项目的依赖库最新版本检查
