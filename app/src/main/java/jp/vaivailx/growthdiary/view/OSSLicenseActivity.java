package jp.vaivailx.growthdiary.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import jp.vaivailx.growthdiary.R;

public class OSSLicenseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_osslicense);
    WebView webView = (WebView) findViewById(R.id.OSSLicenseWebView);
    webView.loadUrl("file:///android_asset/osslicense.html");
  }

  protected void onClickOKButton(View v){
    finish();
  }
}
