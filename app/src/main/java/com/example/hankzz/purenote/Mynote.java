package com.example.hankzz.purenote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Mynote extends AppCompatActivity {
    public static int state = 1;
    private List<Note> noteList;
    private RecyclerView recyclerView;
    private TextView title;
    private LinearLayoutManager layoutManager;
    private NotesAdapter adapter;
    private ActionBar actionBar;
    private ImageView all_image;
    private ImageView important_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynote);
        LitePal.getDatabase();
        all_image = findViewById(R.id.apple_image);
        important_image = findViewById(R.id.strawbery_image);
        recyclerView = (RecyclerView) findViewById(R.id.note_recyclerview);
        title = (TextView) findViewById(R.id.my_title);
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(state == 1){
            setAll_image();
        }else{
            setImportant_image();
        }
        all_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAll_image();
                state = 1;
            }
        });
        important_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImportant_image();
                state = 2;
            }
        });
    }
    //点击全部note
    private void setAll_image(){
        title.setText("MyNote");
        all_image.setImageResource(R.drawable.apple);
        important_image.setImageResource(R.drawable.strawbery_not);
        noteList = LitePal.findAll(Note.class);
        layoutManager = new LinearLayoutManager(Mynote.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotesAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }
    //点击收藏note
    private void setImportant_image(){
        title.setText("ImportantNote");
        important_image.setImageResource(R.drawable.strawbery);
        all_image.setImageResource(R.drawable.apple_not);
        noteList = LitePal.select("*").where("importance=?","1").find(Note.class);
        layoutManager = new LinearLayoutManager(Mynote.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotesAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }
}
