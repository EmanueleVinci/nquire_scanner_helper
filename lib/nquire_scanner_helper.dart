import 'package:flutter/services.dart';

class NQuireScannerHelper {
  static const platform = MethodChannel('flutter.native/helper');

  /// Start the scanner, it lasts for 9 seconds(max).
  /// If successful returns a String [code], otherwise an empty string
  static Future<String?> scan() async {
    String code = "";

    try {
      code = await platform.invokeMethod("activateScan");
      await platform.invokeMethod("stopScan");
    } catch (e) {
      print(e);
    }

    return code;
  }
}
