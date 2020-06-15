package com.example.sookmap;

import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.example.sookmap.FragmentMap.edt_end;
import static com.example.sookmap.FragmentMap.edt_start;
import static com.example.sookmap.FragmentMap.flag_select;


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

        //search result 받아오기
        String search_text="";
        if(getArguments()!=null) {
            search_text=getArguments().getString("search_value");
            //System.out.println("여기다"+search_text);
        }

        //adapter
        ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, list);
        final ListView listView = (ListView) v.findViewById(R.id.find_listview);
        listView.setAdapter(adapter);

        //db 불러오기
        if(search_text!=null) {
            //Find_dialog fragment = new Find_dialog();
            DatabaseManager databaseManager = DatabaseManager.getInstance(getActivity());

            String[] columns = new String[]{"class_name"};
            String selection = "class_name" + " LIKE ?";
            String[] selectionArgs={"%"+search_text+"%"};
            Cursor cursor = databaseManager.query(columns, selection, selectionArgs, null, null, null);
            if(cursor !=null){
                while (cursor.moveToNext()){
                    String class_name=cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                    list.add(class_name);
                }
            }
        }

        //item 선택
        final FragmentMap fragmentMap = new FragmentMap();
        final String[] selected_item = {""};
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selected_item[0] =(String)adapterView.getAdapter().getItem(position);
                Bundle args = new Bundle();
                if(flag_select==1){
                    edt_start.setText(selected_item[0]);
                    args.putString("select_start",selected_item[0]);
                }
                else if(flag_select==2){
                    edt_end.setText(selected_item[0]);
                    args.putString("select_end",selected_item[0]);
                }

                fragmentMap.setArguments(args);

                //보내고 종료
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(Find_dialog.this).commit();
                fragmentManager.popBackStack();
            }
        });


        return v;
    }


}
