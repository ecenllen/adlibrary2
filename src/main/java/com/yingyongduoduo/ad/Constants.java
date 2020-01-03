package com.yingyongduoduo.ad;

import java.util.HashMap;

public class Constants {
  public static final String APPID = "1101152570";
  public static final String SplashPosID = "8863364436303842593";
  public static final String BannerPosID = "9079537218417626401";
  public static final String InterteristalPosID = "8575134060152130849";
  public static final String NativePosID = "5010320697302671";
  public static final String NativeVideoPosID = "5090421627704602";
  public static final String NativeExpressPosID = "7030020348049331"; //如果选择支持视频的模版样式，请使用NativeExpressSupportVideoPosID测试广告位拉取
  public static final String NativeExpressSupportVideoPosID = "2000629911207832"; //支持视频模版样式的广告位
  public static final String CONTENT_AD_POS_ID = "6030826684185381";

  public static HashMap<String, String> paramsMap = new HashMap<>();

  static {
    paramsMap = new HashMap<>();
    paramsMap.put("APPID", APPID);
    paramsMap.put("SplashPosID", SplashPosID);
    paramsMap.put("BannerPosID", BannerPosID);
    paramsMap.put("InterteristalPosID", InterteristalPosID);
    paramsMap.put("NativePosID", NativePosID);
    paramsMap.put("NativeVideoPosID", NativeVideoPosID);
    paramsMap.put("NativeExpressSupportVideoPosID", NativeExpressSupportVideoPosID);
    paramsMap.put("CONTENT_AD_POS_ID", CONTENT_AD_POS_ID);
  }

}