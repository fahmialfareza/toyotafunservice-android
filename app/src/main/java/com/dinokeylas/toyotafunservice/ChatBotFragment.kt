package com.dinokeylas.toyotafunservice

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ChatBotFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat_bot, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance():Fragment = ChatBotFragment()
    }
}
