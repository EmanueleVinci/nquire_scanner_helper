import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:nquire_scanner_helper/nquire_scanner_helper_method_channel.dart';

void main() {
  MethodChannelNquireScannerHelper platform =
      MethodChannelNquireScannerHelper();
  const MethodChannel channel = MethodChannel('nquire_scanner_helper');

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
    expect(await platform.getPlatformVersion(), '42');
  });
}
