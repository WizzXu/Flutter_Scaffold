package com.github.wizz.basechannel.channel

import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result

/**
 * Author: xuweiyu
 * Date: 2021/8/18
 * Email: wizz.xu@outlook.com
 * Description:
 */
class XChannelService private constructor() {
    private lateinit var methodChannel: MethodChannel

    private val channelHandlers = mutableMapOf<String, XBaseChannelHandler>()

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
        channelHandlers[channelHandler.getChannelName()] = channelHandler;
    }

    fun removeChannelHandler(channelHandler: XChannelHandler) {
        channelHandlers.remove(channelHandler.getChannelName());
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
        return methodChannel.invokeMethod(module, mArguments, callback)
    }

    // TODO: 2021/8/19 xwy
    /*fun handlerMethodChannel(module:String, arguments:Map<String, *>): Map<String, *>{
        return channelHandlers[module]
            ?.handlerMethodChannel(method = arguments["method"]?.toString(), arguments = arguments)
    }*/
}