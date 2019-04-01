package com.example.hankzz.purenote;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<Note> notes_list;
    //内部类ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        TextView time;
        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title_text);
            content = (TextView) view.findViewById(R.id.content_text);
            time = (TextView) view.findViewById(R.id.time_text);
        }
    }
    public NotesAdapter(List<Note> list){
        notes_list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.notes_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Note note = notes_list.get(holder.getAdapterPosition());
                String time = note.getTime();
                SpecificContent.openSpecificContent(viewGroup.getContext(),time);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Note note = notes_list.get(i);
        viewHolder.title.setText(note.getTitle());
        viewHolder.content.setText(note.getContent());
        viewHolder.time.setText(note.getTime());
    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }
}
