package com.dinokeylas.toyotafunservice.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.contract.RegisterContract
import com.dinokeylas.toyotafunservice.model.User
import com.dinokeylas.toyotafunservice.presenter.RegisterPresenter
import com.dinokeylas.toyotafunservice.util.MD5
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var registerPresenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerPresenter = RegisterPresenter(this)

        btn_register.setOnClickListener{
            val user = User(
                et_full_name.text.toString(),
                et_full_name.text.toString(),
                et_email.text.toString(),
                et_address.text.toString(),
                et_phone_number.text.toString(),
                "default profile image url",
                MD5.encript(et_password.text.toString())
            )
            registerPresenter.register(user)
        }

        tv_login.setOnClickListener {
            navigateToLogin()
        }

    }

    override fun validateInput(user: User): Boolean {
        //full name should not empty
        if (user.fullName.isEmpty()){
            et_full_name.error = "Nama Tidak Boleh Kosong"
            et_full_name.requestFocus()
            return false
        }

        //email should not empty
        if (user.email.isEmpty()){
            et_email.error = "Email Tidak Boleh Kosong"
            et_email.requestFocus()
            return false
        }

        //email should follow pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()){
            et_email.error = "Format email salah"
            et_email.requestFocus()
            return false
        }

        //address should not empty
        if (user.address.isEmpty()){
            et_address.error = "Alamat Tidak Boleh Kosong"
            et_address.requestFocus()
            return false
        }

        //phone number should not empty
        if (user.phoneNumber.isEmpty()){
            et_phone_number.error = "Nomor Telepon Tidak Boleh Kosong"
            et_phone_number.requestFocus()
            return false
        }

        //password should not empty
        if (user.password.isEmpty()){
            et_password.error = "Kata Sandi Tidak Boleh Kosong"
            et_password.requestFocus()
            return false
        }

        //password contains minimum 6 character
        if (user.password.length<6){
            et_password.error = "Kata Sandi minimal terdiri dari 6 karakter"
            et_password.requestFocus()
            return false
        }

        //password should not null
        if (et_password_validation.text.toString().isEmpty()){
            et_password_validation.error = "Kata Sandi Tidak Boleh Kosong"
            et_password_validation.requestFocus()
            return false
        }

        // password and password validation must be same
        if (user.password != MD5.encript(et_password_validation.text.toString())){
            et_password.error = "Kata Sandi Harus Sama"
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

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
