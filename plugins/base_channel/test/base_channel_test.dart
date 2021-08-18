import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:base_channel/base_channel.dart';

void main() {
  const MethodChannel channel = MethodChannel('base_channel');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await BaseChannel.platformVersion, '42');
  });
}
