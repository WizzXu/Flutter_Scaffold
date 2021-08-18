import 'package:base_channel/channel/x_base_channel_handler.dart';

/// Author: xuweiyu
/// Date: 8/18/21
/// Email: wizz.xu@outlook.com
/// Description:
class XChannelHandler extends XBaseChannelHandler {
  static const String CHANNEL_NAME = "XChannelHandler";
  @override
  String getChannelName() {
    return CHANNEL_NAME;
  }

  @override
  Future handlerMethodChannel(String method, {Map<String, dynamic>? arguments}) {
    return Future.value(null);
  }

}
