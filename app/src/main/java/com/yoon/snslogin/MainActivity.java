package com.yoon.snslogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.kakao.sdk.user.UserApiClient;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Arrays;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    //kakao
    private LinearLayout mKakaoLoginBtn;
    private TextView mResultId;
    private TextView mResultEmail;
    private TextView mResultBirth;
    private Boolean mIsLogin = false;
    //FB
    private com.facebook.login.widget.LoginButton mFbLoginBtn;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());
        mKakaoLoginBtn = findViewById(R.id.btn_kakao_login);
        mResultId = findViewById(R.id.result_id);
        mResultEmail = findViewById(R.id.result_email);
        mResultBirth = findViewById(R.id.result_birth);

        //kakao
        // 로그인 공통 callback 구성
        mKakaoLoginBtn.setOnClickListener(v->{
            if(!mIsLogin){
                login();
            }else{
                logout();
            }
            mIsLogin = !mIsLogin;
        });



     /*   //facebook

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

//        mFbLoginBtn = (com.facebook.login.widget.LoginButton) findViewById(R.id.fb_login_btn);
//        mFbLoginBtn.setReadPermissions("email");

        // Callback registration
//        mFbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//
//                Timber.tag("checkCheck").d("isLoggedIn : %s", isLoggedIn);
//
//
////                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                Timber.tag("checkCheck").d("isLoggedIn : %s", isLoggedIn);
//
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//                Timber.tag("checkCheck").d("isLoggedIn : %s", isLoggedIn);
//
//            }
//        });*/
//
    }

    private void logout() {
        UserApiClient.getInstance().logout(error -> {
            if (error != null) {
                Timber.tag("checkCheck").d("로그아웃 실패");
            }else{
                Timber.tag("checkCheck").d("로그아웃 성공");
            }
            return null;
        });
    }

    private void login() {

        UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Timber.tag("checkCheck").d("로그인 실패 - error : %s", error);
            } else if (oAuthToken != null) {
                Timber.tag("checkCheck").d("로그인 성공");

                UserApiClient.getInstance().me((user, meError) -> {
                    if (meError != null) {
                        Timber.tag("checkCheck").d("사용자 요청 실패 - error : %s", meError);
                    } else {
                        Timber.tag("checkCheck").d("사용자 정보 요청 성공"
                                        + "\n회원 번호 : %s" + "\n회원 이름 : %s"
                                , user.getId()
                                , user.getKakaoAccount());

                        mResultId.setText("아이디 : "+user.getId()+"");
                        mResultEmail.setText("닉네임 : "+user.getKakaoAccount().getProfile().getNickname()+"");
                        mResultBirth.setText("생일 : "+user.getKakaoAccount().getBirthday()+"");
                    }
                    return null;
                });

            }
            return null;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}