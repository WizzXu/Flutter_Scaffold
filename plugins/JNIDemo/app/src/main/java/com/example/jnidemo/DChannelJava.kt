package com.example.jnidemo

import android.util.Log

object DChannelJava {

    @JvmStatic
    fun callNative(argument: ByteArray): ByteArray {
        //Log.e("获取byteArray，入参为：", argument.toList().toString())
        return argument
    }

}