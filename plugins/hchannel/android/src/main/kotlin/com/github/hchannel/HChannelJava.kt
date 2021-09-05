package com.github.hchannel

import android.util.Log

/**
 * Author: xuweiyu
 * Date: 2021/9/4
 * Email: wizz.xu@outlook.com
 * Description:
 */
object HChannelJava {
    @JvmStatic
    fun callNative(argument: ByteArray?): ByteArray {
        Log.d("HChannelJava 入参---->", argument?.toList().toString())
        return byteArrayOf(1, 2, 3, 4, 5, 9)
    }
}