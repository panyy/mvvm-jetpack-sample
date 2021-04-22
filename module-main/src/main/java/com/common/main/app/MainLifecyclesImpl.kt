package com.common.main.app

import android.app.Application
import android.content.Context
import com.common.arms.base.delegate.AppLifecycles
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import com.common.main.repository.MainRepository
import com.common.main.vm.MainViewModel

class MainLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        var modules = module {
            single { MainRepository() }
            viewModel { MainViewModel(get(), get()) }
        }
        loadKoinModules(modules)
    }

    override fun onTerminate(application: Application) {}

}