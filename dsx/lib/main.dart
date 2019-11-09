import 'package:dsx/views/home_page.dart';
import 'package:dsx/views/login_page.dart';
import 'package:dsx/views/signup_page.dart';
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      routes: {
        '/': (context) => HomePage(),
        '/logIn': (context) => LoginPage(),
        '/signUp': (context) => SignUpPage(),
      },
    );
  }
}
