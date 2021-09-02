
import 'dart:async';

import 'package:flutter/services.dart';

class Hchannel {
  static const MethodChannel _channel =
      const MethodChannel('hchannel');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
