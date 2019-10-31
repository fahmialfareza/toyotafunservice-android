package com.dinokeylas.toyotafunservice.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.dinokeylas.toyotafunservice.HomeActivity
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.contract.LoginContract
import com.dinokeylas.toyotafunservice.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginPresenter = LoginPresenter(this)

        btn_login.setOnClickListener {
            loginPresenter.login(et_email.text.toString(), et_password.text.toString())
        }

        tv_register.setOnClickListener {
            navigateToRegister()
        }
    }

    override fun validateInput(email: String, password: String): Boolean {
        if(email.isEmpty()){
            et_email.error = "Email tidak boleh kosong"
            et_email.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.error = "Pastikan format penulisan email benar"
            et_email.requestFocus()
            return false
        }

        if (password.isEmpty()){
            et_password.error = "Kata sandi tidak boleh kosong"
            et_password.requestFocus()
            return false
        }

        return true
    }

    override fun showToastMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

}
