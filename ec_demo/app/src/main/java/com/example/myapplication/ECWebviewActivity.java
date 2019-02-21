package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.fifthera.ecwebview.ECWebChromeClient;
import com.fifthera.ecwebview.ECWebView;
import com.fifthera.ecwebview.ErrorCode;
import com.fifthera.ecwebview.JSApi;
import com.fifthera.ecwebview.OnApiResponseListener;

import static com.fifthera.ecwebview.BitmapShareType.TYPE_WECHAT;
import static com.fifthera.ecwebview.BitmapShareType.TYPE_WECHAT_MOMENT;

public class ECWebviewActivity extends AppCompatActivity {
    private ECWebView mWebView;
    private JSApi mApi;
    private Context mContext;

    public static void invoke(Context context, String url) {
        Intent intent = new Intent(context, ECWebviewActivity.class);
        intent.putExtra("web_url", url);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ec_webview_activity);
        final String webUrl = getIntent().getStringExtra("web_url");
        mContext = this;
        mWebView = findViewById(R.id.new_ec_webview);
        mApi = new JSApi(this);
        mWebView.addJavascriptObject(mApi);

        mApi.setOnApiResponseListener(new OnApiResponseListener() {

            @Override
            public void fail(int i) {
                if (i == ErrorCode.TOKEN_FAIL) {
                    Toast.makeText(ECWebviewActivity.this, "token失效了", Toast.LENGTH_SHORT).show();
                }
                if (i == ErrorCode.COMPOSITE_FAIL) {
                    Toast.makeText(ECWebviewActivity.this, "图片合成失败,请重新分享", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * 分享图片到相应的渠道
             * @param bitmap  需要分享出去的图片
             * @param s  需要复制的文案
             * @param i   分享渠道： 微信好友 - 0x01 ,  微信朋友圈 - 0x02
             */
            @Override
            public void getShareImageBitmap(Bitmap bitmap, String s, int i) {
                Toast.makeText(mContext, "别忘了发送复制内容哦~ ，在输入文字地方点击，粘贴文字即可", Toast.LENGTH_SHORT).show();
                //TODO copy s

                if (i == TYPE_WECHAT) {
                    //TODO 分享到微信好友
                } else if (i == TYPE_WECHAT_MOMENT) {
                    //TODO 分享到微信朋友圈
                }
            }

            /**
             * H5的返回事件
             */
            @Override
            public void goBack() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
            }
        });

        mWebView.setOnWebChromeClientListener(new ECWebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                //Load Progress
            }
        });

        mWebView.loadUrl(webUrl);

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView = null;
        }
        mApi = null;
    }
}
