import 'dart:collection';

import 'package:base_channel/channel/x_base_channel_handler.dart';
import 'package:base_channel/channel/x_channel_handler.dart';
import 'package:base_channel/channel/x_service_log.dart';
import 'package:flutter/services.dart';

/// Author: xuweiyu1
/// Date: 8/18/21
/// Email: wizz.xu@outlook.com
/// Description:
class XChannelService {
  static XChannelService? _instance;
  late MethodChannel _methodChannel;

  static final Map<String, XBaseChannelHandler?> _channelHandlers = new Map();

  XChannelService._(String methodChannel) {
    MethodChannel channel = MethodChannel(methodChannel);
    channel.setMethodCallHandler((MethodCall? call) async {
      XBaseChannelHandler? handler = _channelHandlers[call?.method];
      XServiceLog.i("-->XChannelService:${call?.method} ");
      XServiceLog.i("-->XChannelService:${call?.arguments} ");
      if (handler == null) {
        XServiceLog.i("channel插件不存在:${call?.method} ");
      } else {
        if (call?.arguments == null ||
            call?.arguments is LinkedHashMap<dynamic, dynamic>) {
          return await handler.handlerMethodChannel(call?.arguments["method"] ?? "",
              arguments: Map.from(call?.arguments));
        } else {
          XServiceLog.i("channel参数格式错误:${call?.arguments}:${call?.arguments.runtimeType.toString()}");
        }
      }
      return Future.value(null);
    });
    this._methodChannel = channel;
  }

  static XChannelService getInstance() {
    return _instance ??= XChannelService._("XWY_XMethodChannel");
  }

  static void registerChannelHandler(XChannelHandler channelHandler) {
    XServiceLog.i("-->XChannelService:registerChannelHandler:${channelHandler.getChannelName()} ");
    _channelHandlers[channelHandler.getChannelName()] = channelHandler;
  }

  static void removeChannelHandler(XChannelHandler channelHandler) {
    _channelHandlers.remove(channelHandler.getChannelName());
  }

  Future<dynamic> sendMethodChannel(String module, String method,
      {Map<String, dynamic>? arguments}) async {
    Map<String, dynamic> mArguments = Map();
    mArguments["method"] = method;
    if (arguments != null) {
      mArguments.addAll(arguments..removeWhere((key, value) => value == null));
    }
    return await _methodChannel.invokeMethod(module, mArguments);
  }
}
