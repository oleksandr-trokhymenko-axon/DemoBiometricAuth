package com.axon.demobiometricauth.ui.auth

import com.axon.demobiometricauth.base.ui.BaseViewModel
import com.axon.demobiometricauth.domain.usecases.auth.GetSessionWithLoginUseCase
import com.axon.demobiometricauth.domain.usecases.auth.IsUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    /** Use case for get session with login and password input. */
    private val getSessionWithLoginUseCase: GetSessionWithLoginUseCase,
    /** Use case for check shared prefs session isNotEmpty() */
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
) : BaseViewModel() {

    private val _userLogged = MutableStateFlow(value = false)
    val userLogged: StateFlow<Boolean> get() = _userLogged

    val isUserLogged: Flow<Boolean>
        get() = flow { emit(isUserLoggedUseCase.invoke()) }

    fun loginWithUsername(username: String, password: String) = runCoroutine(withProgress = true) {
        val params = GetSessionWithLoginUseCase.Params(username, password)
        _userLogged.value = getSessionWithLoginUseCase.invoke(params)
    }
}