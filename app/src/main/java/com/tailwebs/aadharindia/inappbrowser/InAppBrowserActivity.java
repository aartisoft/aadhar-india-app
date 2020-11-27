package com.tailwebs.aadharindia.inappbrowser;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;

public class InAppBrowserActivity extends BaseActivity {


    // private String TAG = BrowserActivity.class.getSimpleName();
    private String url;
//    private WebView webView;
//    private ProgressBar progressBar;
//    private float m_downX;
//    CoordinatorLayout coordinatorLayout;



    WebView webview;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_browser);


        init();
        listener();

//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");


        String doc="<iframe src='http://docs.google.com/viewer?url=http://www.iasted.org/conferences/formatting/presentations-tips.ppt&embedded=true' width='100%' height='100%' style='border: none;'></iframe>";
//        url = getIntent().getExtras().getString("file");

//        url="https://www.google.co.in";
//        url="http://www.africau.edu/images/default/sample.pdf";

        String pdfURL = "http://www.africau.edu/images/default/sample.pdf";


//        url = "http://drive.google.com/viewerng/viewer?embedded=true&url=" + "http://www.africau.edu/images/default/sample.pdf";

        // if no url is passed, close the activity

        url = getIntent().getExtras().getString("file");
//        if (TextUtils.isEmpty(url)) {
//            finish();
//        }
//
//        webView = (WebView) findViewById(R.id.webView);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
//
//        initWebView();
//
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//
//
//        webView.loadData(doc, "text/html", "UTF-8");
//        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdfURL);

//        String pdf = "http://www.africau.edu/images/default/sample.pdf";
//        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);


//                webView.loadUrl("https://www.google.co.in");

//        webView.getSettings().setJavaScriptEnabled(true);
//        String pdf = "http://www.pdf995.com/samples/pdf.pdf ";
//        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//        initWebView();
//
//        webView.loadUrl(url);


    }


    private void init() {
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        pDialog = new ProgressDialog(InAppBrowserActivity.this);
        pDialog.setTitle("PDF");
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        String pdfURL = "https://www.adobe.com/support/products/enterprise/knowledgecenter/media/c4611_sample_explain.pdf";
        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);
//        webview.loadUrl("https://drive.google.com/file/d/0B534aayZ5j7Yc3RhcnRlcl9maWxl/view"+pdfURL);

    }

    private void listener() {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
    }


//    private void initWebView() {
//        webView.setWebChromeClient(new MyWebChromeClient(this));
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                progressBar.setVisibility(View.VISIBLE);
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                webView.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                progressBar.setVisibility(View.GONE);
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                progressBar.setVisibility(View.GONE);
//                invalidateOptionsMenu();
//            }
//        });
//        webView.clearCache(true);
//        webView.clearHistory();
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setHorizontalScrollBarEnabled(false);
////        String pdf = "http://www.africau.edu/images/default/sample.pdf";
////        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getPointerCount() > 1) {
//                    //Multi touch detected
//                    return true;
//                }
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        // save the x
//                        m_downX = event.getX();
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP: {
//                        // set x so that it doesn't move
//                        event.setLocation(m_downX, event.getY());
//                    }
//                    break;
//                }
//
//                return false;
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.browser, menu);
//
//        if (BrowserUtils.isBookmarked(this, webView.getUrl())) {
//            // change icon color
//            BrowserUtils.tintMenuIcon(getApplicationContext(), menu.getItem(0), R.color.colorAccent);
//        } else {
//            BrowserUtils.tintMenuIcon(getApplicationContext(), menu.getItem(0), android.R.color.white);
//        }
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // menu item 0-index is bookmark icon

        // enable - disable the toolbar navigation icons
//        if (!webView.canGoBack()) {
//            menu.getItem(1).setEnabled(false);
//            menu.getItem(1).getIcon().setAlpha(130);
//        } else {
//            menu.getItem(1).setEnabled(true);
//            menu.getItem(1).getIcon().setAlpha(255);
//        }
//
//        if (!webView.canGoForward()) {
//            menu.getItem(2).setEnabled(false);
//            menu.getItem(2).getIcon().setAlpha(130);
//        } else {
//            menu.getItem(2).setEnabled(true);
//            menu.getItem(2).getIcon().setAlpha(255);
//        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

//        if (item.getItemId() == R.id.action_bookmark) {
//            // bookmark / unbookmark the url
//            BrowserUtils.bookmarkUrl(this, webView.getUrl());
//
//            String msg = BrowserUtils.isBookmarked(this, webView.getUrl()) ?
//                    webView.getTitle() + "is Bookmarked!" :
//                    webView.getTitle() + " removed!";
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
//            snackbar.show();
//
//            // refresh the toolbar icons, so that bookmark icon color changes
//            // depending on bookmark status
//            invalidateOptionsMenu();
//        }
//
//        if (item.getItemId() == R.id.action_back) {
//            back();
//        }
//
//        if (item.getItemId() == R.id.action_forward) {
//            forward();
//        }

        return super.onOptionsItemSelected(item);
    }

    // backward the browser navigation
//    private void back() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        }
//    }
//
//    // forward the browser navigation
//    private void forward() {
//        if (webView.canGoForward()) {
//            webView.goForward();
//        }
//    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
