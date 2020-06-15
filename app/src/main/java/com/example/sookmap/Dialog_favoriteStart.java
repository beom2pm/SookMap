package com.example.sookmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class Dialog_favoriteStart extends DialogFragment {

    public Dialog_favoriteStart(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_favorite_start, container, false);
        Button btn_yes=view.findViewById(R.id.btn_yes);

        String favorite_text="";
        Bundle bundle = getArguments();
        if (bundle != null)
            favorite_text = bundle.getString("favorite");
        //받은 출발지 보냄
        final String start_text;
        start_text=favorite_text;

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMap fragmentMap = new FragmentMap();
                Bundle args = new Bundle();
                args.putString("start_favorite",start_text);
                fragmentMap.setArguments(args);
                ((MainActivity)getActivity()).replaceFragment(fragmentMap);

            }
        });
        return view;
    }
}
