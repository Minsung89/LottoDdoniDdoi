package com.ddoniddoi.lotto;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LottoLuckActivity extends AppCompatActivity {

    WebView lottoLuckView;
    WebSettings mWebSettings;
    ProgressBar progressBar1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lotto_luck);

        Intent getIntent = getIntent();
        String url = getIntent.getStringExtra("URL");

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

        lottoLuckView = (WebView) findViewById(R.id.lottoLuckView);
        lottoLuckView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = lottoLuckView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

        lottoLuckView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress){
                if (progress<100){
                    progressBar1.setVisibility(ProgressBar.VISIBLE);
                }else if (progress==100){
                    progressBar1.setVisibility(ProgressBar.GONE);
                }
                progressBar1.setProgress(progress);
            }
        });

        lottoLuckView.loadUrl(url); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

    }

    @Override
    public void onBackPressed() {
        //WebVirwMain은 Webview webViewMain;
        //webViewMain = (WebView) findViewById(R.id.Activity);
        if (lottoLuckView.canGoBack()) { // 뒤로가기 눌렀을 때, 뒤로 갈 곳이 있을 경우
            lottoLuckView.goBack(); // 뒤로가기
        } else {//뒤로 갈 곳이 없는 경우
            super.onBackPressed();
        }
    }
}
