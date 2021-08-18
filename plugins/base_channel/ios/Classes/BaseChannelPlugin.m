#import "BaseChannelPlugin.h"
#if __has_include(<base_channel/base_channel-Swift.h>)
#import <base_channel/base_channel-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "base_channel-Swift.h"
#endif

@implementation BaseChannelPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBaseChannelPlugin registerWithRegistrar:registrar];
}
@end
