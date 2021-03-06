package com.axon.demobiometricauth.ui.main

import com.axon.demobiometricauth.base.ui.BaseViewModel
import com.axon.demobiometricauth.domain.usecases.auth.DeleteSessionUseCase
import com.axon.demobiometricauth.domain.usecases.auth.IsUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    /** Use case for delete session http request and clear up shared prefs with sessionId. */
    private val deleteSessionUseCase: DeleteSessionUseCase,
    /** Use case for check shared prefs session isNotEmpty() */
    private val isUserLoggedUseCase: IsUserLoggedUseCase
) : BaseViewModel() {

    private val _userLogged = MutableStateFlow(value = true)
    val userLogged: StateFlow<Boolean> get() = _userLogged

    fun logout() = runCoroutine(withProgress = true) {
        deleteSessionUseCase.invoke()
        _userLogged.value = isUserLoggedUseCase.invoke()
    }
}