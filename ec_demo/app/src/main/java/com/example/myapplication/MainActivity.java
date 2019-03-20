package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fifthera.ecwebview.ECWebView;
import com.fifthera.ecwebview.HomePageInterceptListener;
import com.fifthera.ecwebview.JSApi;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private ECWebView mWebView;
    private JSApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mWebView = findViewById(R.id.ec_webview);
        mApi = new JSApi(this);

        mWebView.addJavascriptObject(mApi);

        mWebView.shouldInterceptHomePageUrl(new HomePageInterceptListener() {
            @Override
            public boolean interceptUrl(String s) {
                //点击商品列表页的商品，打开新的页面展示商品详情
                ECWebviewActivity.invoke(mContext, s);
                return true;
            }
        });

        //授权打开商品列表页，分为后端授权和客户端授权两种方案，推荐使用后端授权
        authoInServerloadUrl();
        //authoInClientLoadUrl();
    }

    //授权方式1：推荐使用此种方式，更为安全
    private void authoInServerloadUrl() {
        //TODO 请求流量主后端接口，进行授权并打开商品列表页
    }

    //授权方式2：客户端执行授权步骤，方便客户端快速接入
    private void authoInClientLoadUrl() {
        String clientId = "11111";   //使用平台申请的clientId
        String clientSecret = "222222";  //使用平台申请的clientSecret
        String uid = "123456789";  // 用户id
        long currnetTime = System.currentTimeMillis()/1000;  //时间戳
        String sign = getSign(clientId, clientSecret, currnetTime, uid);
        StringBuilder str = new StringBuilder();
        str.append("https://ec-api.thefifthera.com/h5/v1/auth/redirect?client_id=")
                .append(clientId)
                .append("&sign=")
                .append(sign)
                .append("&timestamp=")
                .append(currnetTime)
                .append("&uid=")
                .append(uid)
                .append("&type=page.goods");

        String url = str.toString();
        mWebView.loadUrl(url);
    }

    private String getSign(String clientId, String clientSecret, long currentTime, String uid) {
        StringBuilder str = new StringBuilder();
        str.append(clientSecret);
        str.append("client_id").append(clientId)
                .append("timestamp").append(currentTime)
                .append("type").append("page.goods")
                .append("uid").append(uid);
        str.append(clientSecret);

        String s = new String(Hex.encodeHex(DigestUtils.md5(str.toString())));
        return s.toUpperCase();
    }

}
