package com.example.hankzz.purenote;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class ModifyNote extends AppCompatActivity {

    private EditText title_edit;
    private EditText content_edit;
    private ImageView back;
    private ImageView finish;
    private ActionBar actionBar;
    private Intent modify_intent;
    private String time;
    private String title;
    private String content;
    private List<Note> note_list;
    private RecognizerDialog mDialog;
    private ImageView recordaudio;

    public static void modify_note(Context context,String time){
        Intent intent = new Intent(context,ModifyNote.class);
        intent.putExtra("mod_time",time);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);
        title_edit = (EditText) findViewById(R.id.modifytitle_edit);
        content_edit = (EditText) findViewById(R.id.modifycontent_edit);
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //先给修改界面获取title、content
        modify_intent = getIntent();
        time = modify_intent.getStringExtra("mod_time");
        note_list = LitePal.select("*").where("time=?",time).find(Note.class);
        title = note_list.get(0).getTitle();
        content = note_list.get(0).getContent();
        title_edit.setText(title);
        content_edit.setText(content);
        content_edit.requestFocus();
        content_edit.setSelection(content_edit.getText().length());
        //返回事件
        back = (ImageView) findViewById(R.id.toptitle_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //完成修改note事件
        finish = (ImageView) findViewById(R.id.toptitle_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_edit.getText().toString();
                String content = content_edit.getText().toString();
                note_list.get(0).setTitle(title);
                note_list.get(0).setContent(content);
                note_list.get(0).updateAll("time=?",note_list.get(0).getTime());;
                finish();
            }
        });
        //语音输入
        recordaudio = (ImageView) findViewById(R.id.recordaudio_image);
        recordaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ModifyNote.this,Manifest.
                        permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ModifyNote.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},1);
                }else{
                    initSpeech(ModifyNote.this);
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
        NewNote.Voice voiceBean = gson.fromJson(resultString,NewNote.Voice.class);
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<NewNote.Voice.WSBean> ws = voiceBean.ws;
        for(NewNote.Voice.WSBean wsBean : ws){
            String word = wsBean.cw.get(0).w;
            stringBuffer.append(word);
        }
        return stringBuffer.toString();
    }

    public class Voice{
        public ArrayList<NewNote.Voice.WSBean> ws;

        public class WSBean {
            public ArrayList<NewNote.Voice.CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }
}
