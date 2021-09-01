package com.github.wizz.basechannel.channel

import android.util.Log
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result

/**
 * Author: xuweiyu
 * Date: 2021/8/18
 * Email: wizz.xu@outlook.com
 * Description:
 */
class XChannelService private constructor() {
    private val TAG = "XChannelService-Java"
    private lateinit var methodChannel: MethodChannel

    private val channelHandlers = mutableMapOf<String, XIChannelHandler>()

    companion object {
        fun getInstance() = Inner.instance
    }

    private object Inner {
        val instance = XChannelService()
    }

    fun init(methodChannel: MethodChannel) {
        getInstance().methodChannel = methodChannel
    }

    fun registerChannelHandler(channelHandler: XChannelHandler) {
        Log.i(TAG, "registerChannelHandler-->channelHandler:${channelHandler.getChannelName()}")
        channelHandlers[channelHandler.getChannelName()] = channelHandler
    }

    fun removeChannelHandler(channelHandler: XChannelHandler) {
        Log.i(TAG, "removeChannelHandler-->channelHandler:${channelHandler.getChannelName()}")
        channelHandlers.remove(channelHandler.getChannelName())
    }

    fun sendMethodChannel(
        module: String, method: String,
        arguments: Map<String, @JvmSuppressWildcards Any>?,
        callback: Result?
    ) {
        val mArguments = mutableMapOf<String, @JvmSuppressWildcards Any>()
        mArguments["method"] = method
        arguments?.let {
            mArguments.putAll(it)
        }
        Log.i(TAG, "sendMethodChannel-->method:${module} arguments:${arguments}")
        return methodChannel.invokeMethod(module, mArguments, callback)
    }

    fun handlerMethodChannel(module: String, arguments: Map<*, *>, result: XMessageResult<Any>) {
        val xChannelHandler = channelHandlers[module]
        if (xChannelHandler == null) {
            result.notImplemented(module, arguments)
        } else {
            arguments["method"]?.toString()?.let { it1 ->
                xChannelHandler.handlerMethodChannel(
                    method = it1,
                    arguments = arguments,
                    messageResult = result
                )
            }
        }
    }
}