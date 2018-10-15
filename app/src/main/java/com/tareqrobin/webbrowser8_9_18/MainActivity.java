package com.tareqrobin.webbrowser8_9_18;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar pb;
    ImageView imageView;
    LinearLayout ll;
    String currentUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView=findViewById(R.id.webView);
        pb=findViewById(R.id.progressBar);
        imageView=findViewById(R.id.imageView);
        ll=findViewById(R.id.linearLayout);

        pb.setMax(100);

        webView.loadUrl("https://www.tripadvisor.com/");
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                imageView.setImageBitmap(icon);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                ll.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ll.setVisibility(View.GONE);
                currentUrl=url;
                super.onPageFinished(view, url);
            }
        });
    }
    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        }
        else {
            finish();
        }
    }
    public void onForwardPressed(){
        if (webView.canGoForward()){
            webView.goForward();
        }
        else {
            Toast.makeText(this,"Can't go any further",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.super_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.forward:
                onForwardPressed();
                break;
            case R.id.refresh:
                webView.reload();
                break;
            case R.id.share:
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,currentUrl);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Copied URL");
                startActivity(Intent.createChooser(shareIntent,"Share URL with Friends"));
                break;
                default:break;

        }
        return super.onOptionsItemSelected(item);
    }
}
