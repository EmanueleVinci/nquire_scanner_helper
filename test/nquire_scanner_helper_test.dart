import 'package:flutter_test/flutter_test.dart';
import 'package:nquire_scanner_helper/nquire_scanner_helper.dart';
import 'package:nquire_scanner_helper/nquire_scanner_helper_method_channel.dart';
import 'package:nquire_scanner_helper/nquire_scanner_helper_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockNquireScannerHelperPlatform
    with MockPlatformInterfaceMixin
    implements NquireScannerHelperPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final NquireScannerHelperPlatform initialPlatform =
      NquireScannerHelperPlatform.instance;

  test('$MethodChannelNquireScannerHelper is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelNquireScannerHelper>());
  });

  test('getPlatformVersion', () async {
    NquireScannerHelper nquireScannerHelperPlugin = NquireScannerHelper();
    MockNquireScannerHelperPlatform fakePlatform =
        MockNquireScannerHelperPlatform();
    NquireScannerHelperPlatform.instance = fakePlatform;

    expect(await nquireScannerHelperPlugin.getPlatformVersion(), '42');
  });
}
