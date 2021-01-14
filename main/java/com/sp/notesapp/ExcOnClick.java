package com.sp.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ExcOnClick extends AppCompatActivity {
    private WebView view;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exc_on_click);

        description = findViewById(R.id.instructionsTV);
        view = (WebView) findViewById(R.id.mWebView);

        view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                emulateClick(view);
            }
        });
        view.getSettings().setAppCacheEnabled(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
        view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        view.getSettings().setMediaPlaybackRequiresUserGesture(false);
        //String vidUrl = "https://www.youtube.com/embed/ECxYJcnvyMw";

        String vidUrl = getIntent().getStringExtra("VIDEOURL");
        String descrip = getIntent().getStringExtra("DESCRIP");

        description.setText(descrip);
        view.loadUrl(vidUrl);
    }

    @Override
    public void onBackPressed(){
        if(view.canGoBack()){
            view.goBack();
        } else{
            super.onBackPressed();
        }
    }

    private void emulateClick(final WebView webview) {
        long delta = 100;
        long downTime = SystemClock.uptimeMillis();
        float x = webview.getLeft() + webview.getWidth()/2; //in the middle of the webview
        float y = webview.getTop() + webview.getHeight()/2;

        final MotionEvent downEvent = MotionEvent.obtain( downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0 );
        // change the position of touch event, otherwise, it'll show the menu.
        final MotionEvent upEvent = MotionEvent.obtain( downTime, downTime+ delta, MotionEvent.ACTION_UP, x+10, y+10, 0 );

        webview.post(new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(downEvent);
                    webview.dispatchTouchEvent(upEvent);
                }
            }
        });
    }

    //add a onBackPress method that shows dialog, so that when users working out, they don't accidentally go back


}