package com.dinokeylas.toyotafunservice.contract

interface LoginContract {

    interface View {
        fun validateInput(email: String, password: String): Boolean
        fun showToastMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun navigateToHome()
        fun navigateToRegister()
    }

    interface Presenter {
        fun isValidInput(email: String, password: String): Boolean
        fun login(email: String, password: String)
        fun onLoginSuccess()
        fun onLoginFailure()
    }

}