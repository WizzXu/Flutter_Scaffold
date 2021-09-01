package com.github.wizz.basechannel.channel

/**
 * Author: xuweiyu
 * Date: 2021/8/18
 * Email: wizz.xu@outlook.com
 * Description:
 */
class XChannelHandler : XIChannelHandler {

    override fun getChannelName(): String {
        return "XChannelHandler"
    }

    override fun handlerMethodChannel(
        method: String, arguments: Map<*, *>?,
        messageResult: XMessageResult<Any>
    ) {
        when (method) {
            "getPlatformVersion" -> {
                messageResult.success(
                    mapOf("result" to "Android ${android.os.Build.VERSION.RELEASE}")
                )
            }
            else -> messageResult.notImplemented(getChannelName(), arguments)
        }
    }
}