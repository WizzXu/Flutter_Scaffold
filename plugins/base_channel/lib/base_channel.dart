
import 'package:base_channel/channel/x_channel_handler.dart';
import 'package:base_channel/channel/x_channel_service.dart';


export 'channel/x_channel_service.dart';
export 'channel/x_channel_handler.dart';
export 'common_channel.dart';

class BaseChannel {
  static init(){
    //XChannelService.getInstance().registerChannelHandler(XChannelHandler());
  }
}
