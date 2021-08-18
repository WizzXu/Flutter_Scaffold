
import 'dart:async';

import 'package:flutter/services.dart';

class BaseChannel {
  static const MethodChannel _channel =
      const MethodChannel('base_channel');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
