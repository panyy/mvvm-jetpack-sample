package com.common.login.app

import android.app.Application
import android.content.Context
import com.common.arms.base.delegate.AppLifecycles
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import com.common.login.repository.LoginRepository
import com.common.login.vm.LoginViewModel

class LoginLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        var modules = module {
            single { LoginRepository() }
            viewModel { LoginViewModel(get()) }
        }
        loadKoinModules(modules)
    }

    override fun onTerminate(application: Application) {}

}