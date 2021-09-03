import 'dart:async';
import 'dart:ffi';

import 'package:ffi/ffi.dart';
import 'package:flutter/services.dart';
import 'package:hchannel/h_channel_dart_api.dart';

import 'h_channel.dart';

class Hchannel {
  static const MethodChannel _channel = const MethodChannel('hchannel');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');

    HChannelDart.init();
    NativeLibrary library = NativeLibrary(DynamicLibrary.open("native-lib.so"));

    ByteData byteData = new ByteData(10);
    for(int i = 0; i < byteData.lengthInBytes; i++){
      byteData.setInt8(i, i);
    }

    Pointer<Int8> byte = malloc.allocate(byteData.lengthInBytes);

    Pointer<Int8> ret = library.callNative(byte);
    //ret.asTypedList();
    return version;
  }

}
