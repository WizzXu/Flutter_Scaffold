import 'package:pigeon/pigeon.dart';

class Everything {
  bool? aBool;
  int? anInt;
  double? aDouble;
  String? aString;
  Uint8List? aByteArray;
  Int32List? a4ByteArray;
  Int64List? a8ByteArray;
  Float64List? aFloatArray;
  // ignore: always_specify_types
  List? aList;
  // ignore: always_specify_types
  Map? aMap;
  List<List<bool?>?>? nestedList;
  Map<String?, String?>? mapWithAnnotations;
}

@HostApi()
abstract class HostEverything {
  Everything giveMeEverything();
  Everything echo(Everything everything);
}

@FlutterApi()
abstract class FlutterEverything {
  Everything giveMeEverything();
  Everything echo(Everything everything);
}