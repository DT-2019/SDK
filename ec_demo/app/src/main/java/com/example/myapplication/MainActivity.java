package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fifthera.ecwebview.ECWebView;
import com.fifthera.ecwebview.HomePageInterceptListener;
import com.fifthera.ecwebview.JSApi;

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
        loadUrl();
    }

    private void loadUrl() {
        //TODO 请求流量主后端接口，进行授权并打开商品列表页
    }
}
