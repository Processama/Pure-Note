package com.example.hankzz.purenote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.List;

public class SpecificContent extends AppCompatActivity {
    private TextView specifictitle_text;
    private TextView specificcontent_text;
    private ImageView return_image;
    private ImageView collect_image;
    private ImageView write_image;
    private ImageView delete_image;
    private Intent spe_intent;
    private String time;
    private String title;
    private String content;
    private int importance;
    private List<Note> note_list;
    private ActionBar actionBar;

    public static void openSpecificContent(Context context,String time){
        Intent intent = new Intent(context,SpecificContent.class);
        intent.putExtra("spe_time",time);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_content);
        specifictitle_text = (TextView) findViewById(R.id.specifictitle_text);
        specificcontent_text = (TextView) findViewById(R.id.specificcontent_text);
        return_image = (ImageView) findViewById(R.id.return_image);
        collect_image = (ImageView) findViewById(R.id.collect_image);
        write_image = (ImageView) findViewById(R.id.write_image);
        delete_image = (ImageView) findViewById(R.id.delete_image);
        spe_intent = getIntent();
        time = spe_intent.getStringExtra("spe_time");
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        return_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        write_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyNote.modify_note(SpecificContent.this,note_list.get(0).getTime());
            }
        });
        collect_image.setOnClickListener(new View.OnClickListener(){
            public void onClick(View V){
                collect();
            }
        });
        delete_image.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                delete();
            }
        });
    }
    //内容界面初始化
    private void init(){
        note_list = LitePal.select("*").where("time=?",time).find(Note.class);
        title = note_list.get(0).getTitle();
        content = note_list.get(0).getContent();
        importance = note_list.get(0).getImportance();
        specifictitle_text.setText(title);
        specificcontent_text.setText(content);
        if(importance == 1){
            collect_image.setImageResource(R.drawable.collect);
        }else if(importance == -1){
            collect_image.setImageResource(R.drawable.collect_not);
        }
    }
    //点击收藏按钮
    private void collect(){
        if(note_list.get(0).getImportance() == 1){
            note_list.get(0).setImportance(-1);
            note_list.get(0).updateAll("time=?",note_list.get(0).getTime());
            collect_image.setImageResource(R.drawable.collect_not);
        }else{
            note_list.get(0).setImportance(1);
            note_list.get(0).updateAll("time=?",note_list.get(0).getTime());;
            collect_image.setImageResource(R.drawable.collect);
        }
    }
    //点击删除按钮
    private void delete(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(SpecificContent.this);
        dialog.setTitle("Are you sure to delete this note?");
        dialog.setMessage("it can't be restored after being deleted");
        dialog.setCancelable(true);
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LitePal.deleteAll(Note.class,"time=?",note_list.get(0).getTime());
                finish();
            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
