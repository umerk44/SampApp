package com.test.istestprojectapplication.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.istestprojectapplication.data.repository.ISLoginRepository
import com.test.istestprojectapplication.viewmodel.LoginViewModel

class LoginViewModelFactory(private val isLoginRepository: ISLoginRepository,
                            private val sessionManager: SessionManager) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(isLoginRepository, sessionManager) as T
    }
}