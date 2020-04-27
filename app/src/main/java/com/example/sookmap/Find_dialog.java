package com.example.sookmap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Find_dialog extends DialogFragment {

    ArrayList<String>list = new ArrayList<>();
    public Find_dialog(){}
    public static Find_dialog getInstance(){
        Find_dialog e = new Find_dialog();
        return e;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_dialog, container);
        ArrayList<String>list = new ArrayList<>();
        //기록하기! arrayadapter에 this 하면 view가 주어짐->context를 주어야함
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1,list);
        ListView listView= (ListView)v.findViewById(R.id.find_listview);
        listView.setAdapter(adapter);
        list.add("명신 414");
        list.add("명신 413");
        list.add("명신 412");

        return v;
    }


}
