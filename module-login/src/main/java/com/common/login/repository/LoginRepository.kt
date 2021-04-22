package com.common.login.repository

import androidx.lifecycle.MutableLiveData
import com.common.arms.base.mvvm.BaseRepository
import com.common.arms.base.net.apiCallWithState
import com.common.arms.base.state.State
import com.common.login.api.LoginService
import com.common.login.entity.LoginEntity
import retrofit2.http.Field

class LoginRepository : BaseRepository() {

    suspend fun userLogin(
            username: String?,
            password: String?,
            loadState: MutableLiveData<State>? = null) =
            apiCallWithState({
                apiService<LoginService>().userLogin(username, password)
            }, loadState)
}