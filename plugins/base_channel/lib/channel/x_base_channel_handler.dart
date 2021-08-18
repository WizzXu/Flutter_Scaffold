/// Author: xuweiyu
/// Date: 8/18/21
/// Email: wizz.xu@outlook.com
/// Description:
abstract class XBaseChannelHandler {
  String getChannelName();

  Future<dynamic> handlerMethodChannel(String method,
      {Map<String, dynamic>? arguments});
}
