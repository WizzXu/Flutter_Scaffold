import 'package:Flutter_Scaffold/model/chapters.dart';
import 'package:dio/dio.dart';
import 'package:retrofit/dio.dart';
import 'package:retrofit/http.dart';

part 'api.g.dart';
/// Author: 2021/3/5
/// Date: wizz
/// Description:
/// flutter pub run build_runner build

@RestApi(baseUrl: "https://wanandroid.com/")
abstract class APIClient{
  factory APIClient(Dio dio) = _APIClient;

  @GET("wxarticle/chapters/json")
  Future<HttpResponse<Chapters>> getChapters();
}
