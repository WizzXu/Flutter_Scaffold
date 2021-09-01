package com.github.wizz.pchannel

import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.StandardMessageCodec
import java.nio.ByteBuffer

/** PchannelPlugin */
class ChannelPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "channel")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      call.arguments
      result.success("Android channel ${android.os.Build.VERSION.RELEASE}")
      f()
    } else {
      result.notImplemented()
    }
  }

  val map = mapOf<String, Any>("1" to 1, "2" to "2", "3" to "private lateinit var channel : MethodChannel", "4" to false)
  //val map = "adsadfasd"
  val standardMessageCodec1 = StandardMessageCodec1()

  private val standardMessageCodec = StandardMessageCodec()

  fun f(){
    val list = mutableListOf<Int>()
    var byteBuffer = standardMessageCodec1.encodeMessage(map)
    var ret  = byteBuffer?.limit()
    Log.e("----->", ret.toString())
    for (i in 0 until ret!!){
      byteBuffer?.get(i)?.let { list.add(it.toInt()) }
    }
    Log.e("----->", list.toString())
    val a = byteBuffer?.array()
    //Log.e("----->", (StandardMessageCodec1().decodeMessage(ByteBuffer.wrap(a)) as HashMap<*, *>).toString())

    list.clear()
    byteBuffer = standardMessageCodec.encodeMessage(map)
    ret = byteBuffer?.limit()
    Log.e("----->", ret.toString())
    for (i in 0 until ret!!){
      byteBuffer?.get(i)?.let { list.add(it.toInt()) }
    }
    Log.e("----->", list.toString())
    Log.e("----->", (StandardMessageCodec().decodeMessage(ByteBuffer.wrap(byteBuffer?.array())) as HashMap<*, *>).toString())

  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
