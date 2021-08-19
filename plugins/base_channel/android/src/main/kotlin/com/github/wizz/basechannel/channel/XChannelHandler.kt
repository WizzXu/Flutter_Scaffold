package com.github.wizz.basechannel.channel

/**
 * Author: xuweiyu
 * Date: 2021/8/18
 * Email: wizz.xu@outlook.com
 * Description:
 */
class XChannelHandler : XIChannelHandler {
    companion object {
        const val CHANNEL_NAME = "XChannelHandler"
    }

    override fun getChannelName(): String {
        return CHANNEL_NAME
    }

    override fun handlerMethodChannel(method: String, arguments: Map<*, *>?,
                                      messageResult: XMessageResult<Any>) {
        when (method) {
            "getPlatformVersion" -> {
                messageResult.success(
                        mapOf("result" to "Android ${android.os.Build.VERSION.RELEASE}"))
            }
            else -> messageResult.notImplemented()
        }
    }
}