package com.dinokeylas.toyotafunservice.contract

import com.dinokeylas.toyotafunservice.model.User

interface ProfileContract {

    interface View{
        fun fillDataToLayout(user: User?)
        fun navigateToAccountDetail()
        fun navigateToLogin()
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun loadUserData()
        fun fillUserDataToLayout(user: User?)
        fun logout()
    }

}