package com.example.simplerpncalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class EULAActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);

        // Display eula.html in "/app/src/main/assets" folder
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/eula.html");
    }
}