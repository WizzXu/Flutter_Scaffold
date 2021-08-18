import 'package:flutter/cupertino.dart';

/// Author: xuweiyu
/// Date: 8/18/21
/// Email: wizz.xu@outlook.com
/// Description:
class XServiceLog {
  static d (dynamic msg){
    debugPrint(msg.toString());
  }

  static i (dynamic msg){
    print(msg.toString());
  }

  static e (dynamic msg){
    print(msg.toString());
  }
}