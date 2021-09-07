package com.github.hchannel

import android.util.Log
import io.flutter.plugin.common.StandardMessageCodec
import java.nio.ByteBuffer

/**
 * Author: xuweiyu
 * Date: 2021/9/4
 * Email: wizz.xu@outlook.com
 * Description:
 */
object HChannelJava {
    @JvmStatic
    fun callNative(argument: ByteArray?): ByteArray? {
        //Log.d("HChannelJava 入参---->", argument?.toList().toString())
        //Log.d("HChannelJava 入参---->", argument?.toList()?.size.toString())
        argument?.let {
            var s: String =
                StandardMessageCodec.INSTANCE.decodeMessage(ByteBuffer.wrap(argument)) as String
        }
        //Log.d("HChannelJava 入参---->", s)

        //Log.d("HChannelJava执行线程：", "${Thread.currentThread().name} ")
        return argument  //byteArrayOf(1, 2, 3, 4, 5, 9)
    }
}