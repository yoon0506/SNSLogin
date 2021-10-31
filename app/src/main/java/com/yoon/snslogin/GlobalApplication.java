package com.yoon.snslogin;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    private static String KAKAO_NATIVE_APP_KEY = "812a7afb4bcb6bb0e36336d9cf2eb493";
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this, KAKAO_NATIVE_APP_KEY);
    }
}