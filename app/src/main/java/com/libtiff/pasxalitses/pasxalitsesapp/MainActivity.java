package com.libtiff.pasxalitses.pasxalitsesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // When user clicks a hyperlink, load in the existing WebView
            view.loadUrl(url);
            return true;
        }
    }

    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Start of WebView Declarations
        WebView htmlWebView = (WebView)findViewById(R.id.webView);
        htmlWebView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        htmlWebView.pageDown(true);



        WifiManager wifi =(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (wifi.isWifiEnabled() || connectivityManager.getActiveNetworkInfo()!= null){
            //wifi/data is enabled
            htmlWebView.loadUrl("http://www.pasxalitses.com/");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No internet connection , please enable your wifi or data connection.",Toast.LENGTH_LONG).show();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MobileAds.initialize(this, "ca-app-pub-9082725429338291~2647212126");
        View header = navigationView.getHeaderView(0);

        AdView adView = header.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class CustomWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                view.reload();
                return true;
            }
            else if (url.startsWith("mailto:")) {
                //Handle mail Urls
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
            }
            else {
                view.loadUrl(url);
                view.pageDown(true);
            }
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        WebView mWebView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = mWebView.getSettings();
        mWebView.getSettings().setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setGeolocationEnabled(true);
        mWebView.setWebChromeClient(new GeoWebChromeClient());

        int id = item.getItemId();

        switch (id)
        {
            case R.id.facebook:
            {
                mWebView.loadUrl("https://www.facebook.com/pasxalitsescy");
                mWebView.getSettings().setUseWideViewPort(false);
                mWebView.setInitialScale(100);
                break;
            }
            case R.id.website:
            {
                //htmlWebView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
                //Fixing the appearance of website
                mWebView.loadUrl("http://www.pasxalitses.com");
                mWebView.getSettings().setUseWideViewPort(true);
                mWebView.setInitialScale(60);
                break;
            }
            case R.id.linkedin:
            {
                mWebView.loadUrl("https://www.linkedin.com/company/pasxalitses");
                mWebView.getSettings().setUseWideViewPort(false);
                mWebView.setInitialScale(100);
                break;
            }
            case R.id.google:
            {
                mWebView.loadUrl("https://www.youtube.com/channel/UC7piQw-VpDkoH-L1ZBq7ZPw");
                mWebView.getSettings().setUseWideViewPort(false);
                mWebView.setInitialScale(100);
                break;
            }
            case R.id.maps:
            {
                mWebView.loadUrl("https://www.google.com/maps/place/%CE%A0%CE%B1%CE%B9%CE%B4%CE%B9%CE%BA%CF%8C%CF%82+%CE%A3%CF%84%CE%B1%CE%B8%CE%BC%CF%8C%CF%82+%CE%A0%CE%B1%CF%83%CF%87%CE%B1%CE%BB%CE%AF%CF%84%CF%83%CE%B5%CF%82/@35.159173,33.360404,15z/data=!4m2!3m1!1s0x0:0xbe41190b56c7bda3?sa=X&ved=0ahUKEwifm_-ly5bZAhVLKlAKHWDgBdQQ_BIIqgEwCw");
                mWebView.getSettings().setUseWideViewPort(false);
                mWebView.setInitialScale(100);
                break;
            }
            case R.id.dev:
            {
                mWebView.loadUrl("http://libtiff.eu5.net");
                mWebView.getSettings().setUseWideViewPort(false);
                mWebView.setInitialScale(100);
                break;
            }
            default:
            {
                break;
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}