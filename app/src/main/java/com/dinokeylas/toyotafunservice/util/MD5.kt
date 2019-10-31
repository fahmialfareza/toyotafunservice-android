package com.dinokeylas.toyotafunservice.util

import com.google.common.io.ByteStreams.toByteArray
import java.math.BigInteger
import java.security.MessageDigest

class MD5 {

    companion object{
        @JvmStatic
        fun encript(string: String): String{
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(toByteArray(string.byteInputStream()))).toString(16).padStart(30,'0')
        }
    }

}