package com.example.nquire_scanner_helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** NquireScannerHelperPlugin */
class NquireScannerHelperPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "nquire_scanner_helper")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        var wrapper = BroadcastReceiverWrapper(object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                val code = intent?.getStringExtra("SCAN_BARCODE1")
                result.success(code)
            }
        })

        when {
            call.method.equals("activateScan") -> {
                activateScan(wrapper)
            }

            call.method.equals("stopScan") -> {
                stopScan(wrapper)
            }
        }
    }


    private fun activateScan(
        receiverWrapper: BroadcastReceiverWrapper
    ) {
        val intent = Intent("nlscan.action.SCANNER_TRIG")
        intent.putExtra("SCAN_TIMEOUT", 9) // SCAN_TIMEOUT value: int, 1-9; unit: second.
        context.sendBroadcast(intent)

        try {
            val mFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
            context.registerReceiver(receiverWrapper.receiver, mFilter)
            println("Listener started, waiting for scan...")
        } catch (e: Exception) {
            println("LISTENER ERROR:")
            print(e)
        }
    }

    private fun stopScan(receiverWrapper: BroadcastReceiverWrapper) {
        val stopIntent = Intent("nlscan.action.STOP_SCAN")
        context.sendBroadcast(stopIntent)
        context.unregisterReceiver(receiverWrapper.receiver)

        println("Stop Scanning")
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}

private class BroadcastReceiverWrapper constructor(val receiver: BroadcastReceiver) {

}
