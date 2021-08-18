#!/bin/sh
flutter pub run pigeon --input ./lib/pigeon/everything.dart \
  --dart_out lib/base_channel_host.dart \
  --objc_header_out ios/Classes/BaseChannelHost.h \
  --objc_source_out ios/Classes/BaseChannelHost.m \
  --java_out ./android/src/main/java/com/github/wizz/basechannel/BaseChannelHost.java \
  --java_package "com.github.wizz.basechannel"