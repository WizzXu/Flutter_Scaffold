package com.github.hchannel

import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.StandardMessageCodec

/** HchannelPlugin */
class HchannelPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "hchannel")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        /*val byteBuffer = StandardMessageCodec.INSTANCE.encodeMessage("Android")
        byteBuffer?.flip()
        val size = byteBuffer?.let { it.limit() - it.position() }
        val byteArray = size?.let { ByteArray(it) }
        for ( i in 0 until size!!){
            byteArray?.set(i, byteBuffer[i])
        }
        Log.e("---->", byteArray?.toList().toString())
        byteArray?.let { getBytesFromJNI(it) }*/
        if (call.method == "getPlatformVersion") {
            result.success(call.arguments)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    external fun getBytesFromJNI(byteArray: ByteArray): ByteArray

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
