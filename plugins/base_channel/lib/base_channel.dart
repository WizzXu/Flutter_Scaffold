
import 'dart:async';

import 'package:base_channel/channel/x_channel_handler.dart';
import 'package:base_channel/channel/x_channel_service.dart';
import 'package:flutter/services.dart';

class BaseChannel {
  static init(){
    XChannelService.getInstance().registerChannelHandler(XChannelHandler());
  }
}
