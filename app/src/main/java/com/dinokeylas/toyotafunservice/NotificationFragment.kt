package com.dinokeylas.toyotafunservice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NotificationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_notification, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment{
            return NotificationFragment()
        }
    }
}
