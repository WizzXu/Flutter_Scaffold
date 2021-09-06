import 'dart:async';
import 'dart:convert';
import 'dart:ffi';
import 'dart:typed_data';

import 'package:ffi/ffi.dart';
import 'package:flutter/services.dart';
import 'package:hchannel/h_channel_dart_api.dart';

class Hchannel {
  static String testString =
      'This is a long string: sdlfdksjflksndhiofuu2893873(*（%￥#@）*&……￥撒肥料开发时傅雷家书那份会计师东方丽景三等奖';
  static const MethodChannel _channel = const MethodChannel('hchannel');
  static NativeLibrary library =
      NativeLibrary(DynamicLibrary.open("libnative-lib.so"));

  static Future<String?> get platformVersion async {
    String? version = "";
    StandardMessageCodec standardMessageCodec = new StandardMessageCodec();

    int loopsize = 10000 * 1;

    int time = DateTime.now().millisecondsSinceEpoch;
    for (int i = 0; i < loopsize; i++) {
      testHChannel(standardMessageCodec, testString);
    }
    print("HChannel花费时间：" + (DateTime.now().millisecondsSinceEpoch - time).toString());

    /*time = DateTime.now().millisecondsSinceEpoch;
    for (int i = 0; i < loopsize; i++) {
      //version = await _channel.invokeMethod('getPlatformVersion', testString);
    }
    print("Flutter Channel花费时间：" + (DateTime.now().millisecondsSinceEpoch - time).toString());
*/
    version = "结束";
    return "HChannel花费时间：" + (DateTime.now().millisecondsSinceEpoch - time).toString();
  }

  static testHChannel(StandardMessageCodec standardMessageCodec, Object object) {

    //final units = utf8.encode(testString);

    //Pointer<Uint8> byte = malloc.allocate(units.length);
    /*for(int i = 0; i < units.length; i++){
      byte[i] = units.indexOf(i);
    }*/

    ByteData? byteData = standardMessageCodec.encodeMessage(object);
    //print("----------------");
    Int8List int8list = byteData?.buffer.asInt8List() ?? new Int8List(0);
    //print(int8list);
    //print(int8list.length.toString());

    Pointer<Int8> byte = malloc.allocate(int8list.length);
    byte.asTypedList(int8list.length).setAll(0, int8list);

    /*for(int i = 0; i < int8list.length; i++){
      byte[i] = int8list.indexOf(i);
      print("byte[$i]=" + byte[i].toString());
    }*/

    Pointer<ByteArray> ret = library.callNative(byte, int8list.length);
    //print("object" + ret.ref.length.toString());
    //print(ret.ref.data.asTypedList(ret.ref.length));
    //释放内存
    malloc.free(byte);
    malloc.free(ret);
  }
}
