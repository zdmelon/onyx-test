package com.example.zd.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.zd.myapplication.manager.AudioRecorderManager;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class TestActivity extends AppCompatActivity {

    private final static int REQUEST_RECORDER = 100;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
//        Spanned s = Html.fromHtml("<table class=\"edittable\"><tbody><tr><td width=\"68\">24时计时法</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td width=\"71\">10：00</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td width=\"71\">15：00</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td width=\"71\">21：00</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td></tr><tr><td>普通计时法</td><td>上午8时</td><td><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td>中午12时</td><td><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td>下午5时</td><td><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td>凌晨3时</td></tr></tbody></table>");
//        Log.i("111", "onCreate: "+s.toString());
    }

    private void initView() {
//        String data = "5. 【解答题】【分数：0】<br/><!--B1--><span class=\"qseq\"></span><a class=\"ques-source\">（2015秋•郧西县期末）</a>填表&nbsp;<table class=\"edittable\"><tbody><tr><td width=\"68\">24时计时法</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td width=\"71\">10：00</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td width=\"71\">15：00</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td width=\"71\">21：00</td><td width=\"71\"><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td></tr><tr><td>普通计时法</td><td>上午8时</td><td><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td>中午12时</td><td><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td>下午5时</td><td><!--BA--><div class=\"quizPutTag\">______</div><!--EA--></td><td>凌晨3时</td></tr></tbody></table><!--E1--><br />";
//        WebView webView = (WebView) findViewById(R.id.html_text);
//        webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);

        Button button = findViewById(R.id.recorder);
        button.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this
                        , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
            }else {
               startRecorder();
            }
        });
    }

    private void startRecorder() {
//        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
//        startActivityForResult(intent,REQUEST_RECORDER);
        String path = Environment.getExternalStorageDirectory()+"/123.mp3";
        AudioRecorderManager.getInstance().startRecord(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && REQUEST_RECORDER == requestCode){
            uri = data.getData();
        }
    }
}
