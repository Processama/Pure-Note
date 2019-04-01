package com.example.hankzz.purenote;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.Date;

public class NewNote extends AppCompatActivity {
    private Note note;
    private String title;
    private String content;
    private EditText title_edit;
    private EditText content_edit;
    private ImageView back;
    private ImageView finish;
    private ActionBar actionBar;
    private RecognizerDialog mDialog;
    private ImageView recordaudio;

    public static void create_newnote(Context context){
        Intent intent = new Intent(context,NewNote.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=5c1704b9");
        Mynote.state = 1;
        note = new Note();
        title_edit = (EditText) findViewById(R.id.newtitle_edit);
        content_edit = (EditText) findViewById(R.id.newcontent_edit);
        content_edit.requestFocus();
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //返回事件
        back = (ImageView) findViewById(R.id.toptitle_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //完成创建note事件
        finish = (ImageView) findViewById(R.id.toptitle_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = title_edit.getText().toString();
                content = content_edit.getText().toString();
                if(title == null || title.length() == 0){
                    title="Note of no title";
                }
                if(content == null || content.length() == 0){
                    content="Note of no content";
                }
                note.setTitle(title);
                note.setContent(content);
                note.setTime(new Date().toString());
                note.setImportance(-1);
                note.save();
                finish();
            }
        });
        //语音输入事件
        recordaudio = (ImageView) findViewById(R.id.recordaudio_image);
        recordaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(NewNote.this,Manifest.
                        permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(NewNote.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},1);
                }else{
                    initSpeech(NewNote.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initSpeech(this);
                }else{
                    Toast.makeText(this,"you denied the permission!",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void initSpeech(final Context context){
        mDialog = new RecognizerDialog(context,null);
        mDialog.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT,"madarin");
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if(!b){
                    String result = parseVoice(recognizerResult.getResultString());
                    String content = content_edit.getText()+result;
                    content_edit.setText(content);
                    content_edit.setSelection(content_edit.getText().length());
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        mDialog.show();
    }
    public String parseVoice(String resultString){
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString,Voice.class);
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for(Voice.WSBean wsBean : ws){
            String word = wsBean.cw.get(0).w;
            stringBuffer.append(word);
        }
        return stringBuffer.toString();
    }

    public class Voice{
        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }
}
