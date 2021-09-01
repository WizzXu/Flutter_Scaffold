package com.github.wizz.basechannel.channel

import android.util.Log
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec

/**
 * Author: xuweiyu
 * Date: 8/19/21
 * Email: wizz.xu@outlook.com
 * Description:
 */
class XMessageResult<T> constructor(private val result: MethodChannel.Result) {

    private val TAG = "XMessageResult-Java"
    fun success(result: T?) {
        this.result.success(result)
    }

    /**
     * Handles an error result.
     *
     * @param errorCode An error code String.
     * @param errorMessage A human-readable error message String, possibly null.
     * @param errorDetails Error details, possibly null. The details must be an Object type
     * supported by the codec. For instance, if you are using [StandardMessageCodec]
     * (default), please see its documentation on what types are supported.
     */
    fun error(
        module: String?,
        arguments: Map<*, *>?,
        errorCode: String?,
        errorMessage: String?,
        errorDetails: Any?
    ) {
        Log.e(TAG, "error-->method:${module} arguments:${arguments}")
        this.result.error(errorCode, errorMessage, errorDetails)
    }

    /** Handles a call to an unimplemented method.  */
    fun notImplemented(module: String, arguments: Map<*, *>?) {
        Log.i(TAG, "notImplemented-->method:${module} arguments:${arguments}")
        this.result.notImplemented()
    }
}