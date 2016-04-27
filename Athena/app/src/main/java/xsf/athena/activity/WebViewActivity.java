package xsf.athena.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import xsf.athena.R;
import xsf.athena.activity.base.BaseActvity;

/**
 * Author: xsf
 * Time: created at 2016/4/26.
 * Email: xsf_uestc_ncl@163.com
 */
public class WebViewActivity extends BaseActvity {
    public static String URL = "webViewUrl";
    protected WebView mWebView;
    protected ProgressBar mProgressBar;
    private String mUrl;

    @Override
    protected void init() {
        initToolBar();
        mUrl = getIntent().getExtras().getString(URL);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        mProgressBar = IfindViewById(R.id.progressbar);
        mWebView = IfindViewById(R.id.webView);
        initWebViewSetting();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mProgressBar.setMax(100);
        mWebView.loadUrl(mUrl);


        setToobarTitle("加载中……");
        //监听toolbar左上角后退按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void initWebViewSetting() {
        WebSettings webSettings = mWebView.getSettings();
        mWebView.requestFocusFromTouch();//支持获取手势焦点，输入用户名、密码或其他
        webSettings.setJavaScriptEnabled(true);//支持js
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小


        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。

        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.supportMultipleWindows();  //多窗口
        // webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

    }

    @Override
    public void onBackPressed() {
        if (canGoBanck()) goBack();
        else
            finish();
    }

    private void goBack() {
        if (mWebView != null) {
            mWebView.goBack();
        }
    }

    private boolean canGoBanck() {
        return mWebView != null && mWebView.canGoBack();
    }

    //WebViewClient就是帮助WebView处理各种通知、请求事件的。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
    }

    //WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        //获取Web页中的title用来设置自己界面中的title
        //当加载出错的时候，比如无网络，这时onReceiveTitle中获取的标题为 找不到该网页,
        //因此建议当触发onReceiveError时，不要使用获取到的title
        @Override
        public void onReceivedTitle(WebView view, String title) {
            //super.onReceivedTitle(view, title);
            mToolbar.setTitle(title);
        }
    }
}
