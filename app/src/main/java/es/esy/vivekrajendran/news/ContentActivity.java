package es.esy.vivekrajendran.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import es.esy.vivekrajendran.news.util.NetworkChecker;


public class ContentActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private WebView moreWebView;
    private Button loadUrlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("title"));

        progressBar = (ProgressBar) findViewById(R.id.pb_content_web);
        ImageView imageView = (ImageView) findViewById(R.id.iv_activity_scrollling_toolbar);
        loadUrlButton = (Button) findViewById(R.id.btn_content_loadurl);
        moreWebView = (WebView) findViewById(R.id.wv_content);
        moreWebView.setWebViewClient(new MyWebViewClient());
        moreWebView.getSettings().setJavaScriptEnabled(true);

        TextView descriptionTextView = (TextView) findViewById(R.id.tv_content_detail);
        descriptionTextView.setText(getIntent().getStringExtra("description"));
        loadUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(intent.getStringExtra("urlNews"));
            }
        });

        Glide.with(this)
                .load(intent.getStringExtra("url"))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .error(R.drawable.ic_warning)
                .placeholder(R.drawable.ic_reload)
                .into(imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra("urlNews"));
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
            }
        });
    }

    private void loadWebView(String url) {
        moreWebView.loadUrl(url);
        loadUrlButton.setVisibility(View.GONE);
        moreWebView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void loadUrl(final String url) {
        if (!NetworkChecker.getInstance(getApplicationContext()).isNetworkAvailable()) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cool_content);
            Snackbar.make(coordinatorLayout, "Network unavailable", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadUrl(url);
                        }
                    })
                    .show();
        } else {
            loadWebView(url);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if(progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setProgress(View.GONE);
            }
            return true;
        }
    }
}
