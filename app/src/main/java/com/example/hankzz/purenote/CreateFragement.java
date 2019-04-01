package com.example.hankzz.purenote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CreateFragement extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_fragment,container,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewNote.create_newnote(getActivity());
            }
        });
        return view;
    }
}
