package com.example.user.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText txtMessage;
    private Button sendBtn,button1;
    private String uriAPI = "http://192.168.1.106:8081/set_temp.php";
    WebView mWebView = null;
    /** 「要更新版面」的訊息代碼 */
    protected static final int REFRESH_DATA = 0x00000001;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 顯示網路上抓取的資料
                case REFRESH_DATA:
                    String result = null;
                    if (msg.obj instanceof String)
                        result = (String) msg.obj;
                    if (result != null)
                        // 印出網路回傳的文字
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMessage = (EditText) findViewById(R.id.txt_message);
        sendBtn = (Button) findViewById(R.id.send_btn);
        button1 = (Button) findViewById(R.id.button1);
        if (sendBtn != null)
            //sendBtn.setOnClickListener(this);
            sendBtn .setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (txtMessage != null)
                    {
                        // 擷取文字框上的文字
                        String msg = txtMessage.getEditableText().toString();
                        // 啟動一個Thread(執行緒)，將要傳送的資料放進Runnable中，讓Thread執行
                        Thread t = new Thread(new sendPostRunnable(msg));
                        t.start();
                    }
                }
            });
        if (button1 != null)
            //sendBtn.setOnClickListener(this);
            button1 .setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mWebView = (WebView)findViewById(R.id.webView);
                    mWebView.setWebViewClient(mWebViewClient);
                    mWebView.setInitialScale(1);
                    mWebView.getSettings().setLoadWithOverviewMode(true);
                    mWebView.getSettings().setUseWideViewPort(true);
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.loadUrl("http://192.168.1.106:8081/graph.html ");
                }
            });
    }
    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
    private String sendPostDataToInternet(String strTxt)
    {
       /* 建立HTTP Post連線 */
        HttpPost httpRequest = new HttpPost(uriAPI);
       /*
        * Post運作傳送變數必須用NameValuePair[]陣列儲存
        */
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data", strTxt));
        try
        {
           /* 發出HTTP request */
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
           /* 取得HTTP response */
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
           /* 若狀態碼為200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
              /* 取出回應字串 */
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                // 回傳回應字串
                return strResult;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    class sendPostRunnable implements Runnable
    {
        String strTxt = null;
        // 建構子，設定要傳的字串
        public sendPostRunnable(String strTxt)
        {
            this.strTxt = strTxt;
        }
        @Override
        public void run()
        {
            String result = sendPostDataToInternet(strTxt);
            mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
        }
    }
}
