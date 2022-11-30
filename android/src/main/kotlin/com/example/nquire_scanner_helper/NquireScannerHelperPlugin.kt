package com.example.nquire_scanner_helper

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** NquireScannerHelperPlugin */
class NquireScannerHelperPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "nquire_scanner_helper")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        val scannerReceiverWrapper: BroadcastReceiverWrapper = BroadcastReceiverWrapper()

        when {
            call.method.equals("activateScan") -> {
                activateScan(result, scannerReceiverWrapper)
            }

            call.method.equals("stopScan") -> {
                stopScan(scannerReceiverWrapper.receiver)
            }
        }
    }


    private fun activateScan(
        result: MethodChannel.Result,
        scannerReceiverWrapper: BroadcastReceiverWrapper
    ) {
        val intent = Intent("nlscan.action.SCANNER_TRIG")
        intent.putExtra("SCAN_TIMEOUT", 9) // SCAN_TIMEOUT value: int, 1-9; unit: second.
        context.sendBroadcast(intent)

        try {
            scannerReceiverWrapper.receiver = object : BroadcastReceiver() {
                override fun onReceive(contxt: Context?, intent: Intent?) {
                    val code = intent?.getStringExtra("SCAN_BARCODE1")
                    result.success(code)
                }
            }

            val mFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
            context.registerReceiver(scannerReceiverWrapper.receiver, mFilter)
            println("Listener started, waiting for scan...")
        } catch (e: Exception) {
            println("LISTENER ERROR:")
            print(e)
        }
    }

    private fun stopScan(scannerReceiver: BroadcastReceiver) {
        val stopIntent = Intent("nlscan.action.STOP_SCAN")
        context.sendBroadcast(stopIntent)
        context.unregisterReceiver(scannerReceiver)

        println("Stop Scanning")
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity;
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }
}

/// Wrapper class. In Kotlin all variables are final, but not their props
private class BroadcastReceiverWrapper {
    lateinit var receiver: BroadcastReceiver
}