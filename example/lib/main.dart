import 'package:flutter/material.dart';
import 'package:nquire_scanner_helper/nquire_scanner_helper.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'test',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String code = "";

  activateScan() async {
    String? result = await NquireScannerHelper.scan();

    if (result?.isNotEmpty ?? false) {
      code = result!;

      setState(() {});
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (code.isNotEmpty) Text("CODE : $code"),
            ElevatedButton(
              onPressed: activateScan,
              child: const Padding(
                padding: EdgeInsets.all(20),
                child: Text(
                  "SCAN",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
