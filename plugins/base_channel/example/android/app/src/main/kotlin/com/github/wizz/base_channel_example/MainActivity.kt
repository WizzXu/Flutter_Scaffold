package com.github.wizz.base_channel_example

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.wizz.basechannel.channel.XChannelService
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {

    override fun onResume() {
        super.onResume()
        val handler = Handler(Looper.getMainLooper())
        handler.postAtTime(Runnable {
            XChannelService.getInstance().sendMethodChannel("XChannelHandler",
            "getInfo", null, object : MethodChannel.Result{
                override fun notImplemented() {
                    Log.e("xwy---->", "notImplemented")
                }

                override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {
                    Log.e("xwy---->", "error")
                }

                override fun success(result: Any?) {
                    Log.e("xwy---->", result.toString())
                }
            })
        }, 1000)
    }
}
