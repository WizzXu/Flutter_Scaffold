import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:hchannel/hchannel.dart';

void main() {
  const MethodChannel channel = MethodChannel('hchannel');

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
    expect(await Hchannel.platformVersion, '42');
  });
}
