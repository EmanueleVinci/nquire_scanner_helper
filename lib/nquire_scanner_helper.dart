import 'package:flutter/services.dart';

class NquireScannerHelper {
  static const platform = MethodChannel('nquire_scanner_helper');

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
