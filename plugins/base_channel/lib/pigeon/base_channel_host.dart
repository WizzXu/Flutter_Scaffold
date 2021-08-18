
import 'package:pigeon/pigeon.dart';

class BaseChannelPluginSendBean {
  String module;
  String method;
  Map<dynamic, dynamic>? arguments;

  factory BaseChannelPluginSendBean.create(module, method) =>
      BaseChannelPluginSendBean(module, method, null);

  BaseChannelPluginSendBean(this.module, this.method, this.arguments);

}

class BaseChannelPluginResultBean {
  Map<String?, String?> result;

  BaseChannelPluginResultBean(this.result);
}

@HostApi()
abstract class BaseChannelHost {
  BaseChannelPluginResultBean call(BaseChannelPluginSendBean bean);
}