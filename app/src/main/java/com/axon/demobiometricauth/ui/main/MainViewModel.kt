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
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val isUserLoggedUseCase: IsUserLoggedUseCase
) : BaseViewModel() {

    private val _userLogged = MutableStateFlow(value = true)
    val userLogged: StateFlow<Boolean> get() = _userLogged

    fun logout() = runCoroutine(withProgress = true) {
        deleteSessionUseCase.invoke()
        _userLogged.value = isUserLoggedUseCase.invoke()
    }
}