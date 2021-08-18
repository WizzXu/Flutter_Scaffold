import 'package:base_channel/base_channel.dart';

class CommonChannel {

  static get platformVersion {
    return XChannelService.getInstance()
        .sendMethodChannel(XChannelHandler.CHANNEL_NAME, "getPlatformVersion");
  }
}
