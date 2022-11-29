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

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "nquire_scanner_helper")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {


        val scannerReceiverWrapper: BroadcastReceiverWrapper = BroadcastReceiverWrapper()

        channel.setMethodCallHandler { call, result ->
            when {
                call.method.equals("activateScan") -> {
                    activateScan(result, scannerReceiverWrapper)
                }

                call.method.equals("stopScan") -> {
                    stopScan(scannerReceiverWrapper.receiver)
                }
            }
        }
    }


    private fun activateScan(
        result: MethodChannel.Result,
        scannerReceiverWrapper: BroadcastReceiverWrapper
    ) {
        val intent = Intent("nlscan.action.SCANNER_TRIG")
        intent.putExtra("SCAN_TIMEOUT", 9) // SCAN_TIMEOUT value: int, 1-9; unit: second.
        sendBroadcast(intent)
        println("Scan started")

        try {
            scannerReceiverWrapper.receiver = object : BroadcastReceiver() {
                override fun onReceive(contxt: Context?, intent: Intent?) {
                    val code = intent?.getStringExtra("SCAN_BARCODE1")
                    result.success(code)
                }
            }

            val mFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
            registerReceiver(scannerReceiverWrapper.receiver, mFilter)
            println("Listener started, waiting for scan...")
        } catch (e: Exception) {
            println("LISTENER ERROR:")
            print(e)
        }
    }

    private fun stopScan(scannerReceiver: BroadcastReceiver) {
        val stopIntent = Intent("nlscan.action.STOP_SCAN")
        sendBroadcast(stopIntent)
        unregisterReceiver(scannerReceiver)

        println("Stop Scanning")
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}

/// Wrapper class. In Kotlin all variables are final, but not their props
private class BroadcastReceiverWrapper {
    lateinit var receiver: BroadcastReceiver
}