package com.github.wizz.basechannel.channel

/**
 * Author: xuweiyu
 * Date: 2021/8/18
 * Email: wizz.xu@outlook.com
 * Description:
 */
interface XBaseChannelHandler {
    fun getChannelName(): String

    fun handlerMethodChannel(method:String, arguments:Map<String, *>?): Map<String, *>
}