import 'dart:async';
import 'dart:convert';
import 'dart:ffi';
import 'dart:typed_data';

import 'package:ffi/ffi.dart';
import 'package:flutter/services.dart';
import 'package:hchannel/h_channel_dart_api.dart';

class Hchannel {
  static const MethodChannel _channel = const MethodChannel('hchannel');
  static NativeLibrary library =
      NativeLibrary(DynamicLibrary.open("libnative-lib.so"));

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');

    int time = DateTime.now().millisecondsSinceEpoch;
    for (int i = 0; i < 10000; i++) {
      testHChannel();
    }
    print("花费时间：" + (DateTime.now().millisecondsSinceEpoch - time).toString());

    return version;
  }

  static testHChannel() {
    String testString =
    'This is a long string: sdlfdksjflksndhiofuu2893873(*（%￥#@）*&……￥撒肥料开发时傅雷家书那份会计师东方丽景三等奖';

    final units = utf8.encode(testString);

    Pointer<Int8> byte = malloc.allocate(units.length);
    for(int i = 0; i < units.length; i++){
      byte[i] = units.indexOf(i);
    }

    StandardMethodCodec en = new StandardMethodCodec();
    en.en

    Pointer<ByteArray> ret = library.callNative(byte, units.length);
    print("object" + ret.ref.length.toString());
    print(ret.ref.data.asTypedList(ret.ref.length));
    //释放内存
    malloc.free(byte);
    malloc.free(ret);
  }
}
