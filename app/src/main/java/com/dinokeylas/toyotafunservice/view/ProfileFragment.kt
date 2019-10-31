package com.dinokeylas.toyotafunservice.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.contract.ProfileContract
import com.dinokeylas.toyotafunservice.model.User
import com.dinokeylas.toyotafunservice.presenter.ProfilePresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment(), ProfileContract.View {

    private lateinit var profilePresenter: ProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePresenter = ProfilePresenter(this)

        view.btn_logout.setOnClickListener {
            profilePresenter.logout()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment = ProfileFragment()
    }

    override fun fillDataToLayout(user: User?) {
        tv_user_name.text = user?.userName
        tv_email.text = user?.email
        tv_phone_number.text = user?.phoneNumber
        tv_location.text = user?.address
        if (user?.profileImageUrl != "default profile image url"){
            Glide.with(this).load(user?.profileImageUrl).into(civ_profile_image)
        }
    }

    override fun navigateToAccountDetail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToLogin() {
        startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
