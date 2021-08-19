package com.github.wizz.basechannel

import android.util.Log
import androidx.annotation.LongDef
import androidx.annotation.NonNull
import com.github.wizz.basechannel.channel.XChannelHandler
import com.github.wizz.basechannel.channel.XChannelService
import com.github.wizz.basechannel.channel.XMessageResult

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** BaseChannelPlugin */
class BaseChannelPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "XWY_XMethodChannel")
        channel.setMethodCallHandler(this)
        XChannelService.getInstance().init(channel)
        XChannelService.getInstance().registerChannelHandler(XChannelHandler())
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        Log.e("--->BaseChannelPlugin", call.method)
        Log.e("--->BaseChannelPlugin", call.arguments.toString())

        if (call.arguments is Map<*, *>) {
            XChannelService.getInstance().handlerMethodChannel(call.method, call.arguments as Map<*, *>, XMessageResult(result))
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
