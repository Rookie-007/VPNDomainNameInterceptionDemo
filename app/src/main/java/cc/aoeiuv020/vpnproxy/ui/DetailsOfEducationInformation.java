package cc.aoeiuv020.vpnproxy.ui;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.aoeiuv020.vpnproxy.R;
import cc.aoeiuv020.vpnproxy.utils.ConstantUtils;
import cc.aoeiuv020.vpnproxy.utils.Utils;

public class DetailsOfEducationInformation extends AppCompatActivity implements View.OnClickListener {
    private  WebView webview;
    private TextView tvLoadFial;
    LinearLayout llBackLeft;
    TextView tvTitle;
    ImageView ivAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_education_information);
        Utils.setColor(DetailsOfEducationInformation.this, getResources().getColor(R.color.color_ff7e33), true, R.drawable.fake_status_bar_bg);
        init();

        initData();
    }

    private void initData() {
        String url  = "";
        tvLoadFial = findViewById(R.id.tv_load_fial);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setSavePassword(false);
        initWevViewClient();

        url = getIntent().getStringExtra(ConstantUtils.DETAILS_URL);
//        url = "https://mbd.baidu.com/newspage/data/landingsuper?context=%7B%22nid%22%3A%22news_8718708835162655846%22%7D&n_type=0&p_from=1";

        webview.loadUrl(url);
    }

    private void init() {
        ivAdd = findViewById(R.id.iv_add);
        llBackLeft = findViewById(R.id.ll_back_left);
        llBackLeft.setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title);
        ivAdd.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.details_of_information));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_back_left:
                finish();
                break;
        }
    }


    /**
     * 加载是否成功
     */
    private boolean isLoadStatus = true;

    protected void initWevViewClient() {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isLoadStatus = false;
//                loadUrl("file:///android_asset/agreement_app.html");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //处理https请求,接受信任所有网站的证书
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) { //当页面加载完成
                super.onPageFinished(view, url);
                if (!isLoadStatus) {
                    view.setVisibility(View.GONE);
                    webview.setVisibility(View.GONE);
                    tvLoadFial.setVisibility(View.VISIBLE);
                } else {
                    tvLoadFial.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                }

            }

        });
    }

}
