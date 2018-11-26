package com.xyxg.android.unittestexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GmailActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail);
        initWeb();
    }

    private void initWeb() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie("accounts.google.com", "LSID=");
        cookieManager.setCookie("accounts.google.com", "GAPS=");
        cookieManager.setCookie("accounts.google.com", "ACCOUNT_CHOOSER=");
        webView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/59.0.3071.82");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Gmail", "shouldOverrideUrlLoading: " + url);
                if (url.contains("approvalCode")) {
                    int i = url.indexOf("approvalCode");
                    Log.e("Gmail", "shouldOverrideUrlLoading: " + url.substring(i + 13));
                    finish();
                    /*
                    获取token：
                    https://www.googleapis.com/oauth2/v4/token
                    client_id: 1078214547638-rmtl4r2p05ojhs4v328khm143ilg5fag.apps.googleusercontent.com
                    code: 4/JWb53TwE3E6_jwAUs3f__LxHBGgsuakrAaEfmhBBprE
                    grant_type: authorization_code
                    redirect_uri: urn:ietf:wg:oauth:2.0:oob
                    */
                    /*ss_token=ya29.GltiBCAoNlCrEthSVHDmjdEL7fOdYkwK_N8GlCwXqUuZvMU2qwYCBC840r1TdKooAo5EoSiq3gTx8n-l_WD2PlOrnE9-onl90CwMsGju1AbJFja423OC4_Q0q0T9
                    返回：
                    {
                        "sub": "104491757208201508119",
                        "name": "Limin Yao",
                        "given_name": "Limin",
                        "family_name": "Yao",
                        "picture": "https://lh3.googleusercontent.com/-XdUIqdM
                    获取用户信息：https://www.googleapis.com/oauth2/v3/userinfo?accekCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg",
                        "email": "xyxg.yao@gmail.com",
                        "email_verified": true,
                        "locale": "zh-CN"
                    }
                    */
                } else if (url.contains("errot=")) {
                    // TODO: 2017/6/7 拒绝
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        String url = "https://accounts.google.com/o/oauth2/v2/auth?"
                + "scope=openid%20email%20profile%20https://mail.google.com/%20https://www.googleapis.com/auth/contacts&"
                + "redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&"
                + "client_id=1078214547638-rmtl4r2p05ojhs4v328khm143ilg5fag.apps.googleusercontent.com";
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
