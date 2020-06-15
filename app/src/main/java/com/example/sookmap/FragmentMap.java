package com.example.sookmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.Company.androidTest_cube.UnityPlayerActivity;
import com.google.android.material.chip.ChipGroup;
import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;

public class FragmentMap extends Fragment {
    public UnityPlayer mUnityPlayer;
    static int beacon_flag=0;
    static int flag_select=0;
    static EditText edt_start;
    static EditText edt_end;
    public ToggleButton favoriteButton;

    public FragmentMap() {
        // Required empty public constructor

    }

    public static FragmentMap newInstance(String param1, String param2) {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        //db 객체 얻어오기
        //DatabaseManager databaseManager=DatabaseManager.getInstance(getActivity());

        String beacon_str = "";
        if(beacon_flag==1) {
            System.out.println("액티비티 몇번");
            //beacon값 받아오기

            Bundle bundle = getArguments();
            if (bundle != null)
                beacon_str = bundle.getString("class_value");
            Log.d("activity to frag",beacon_str);
        }

        //select item을 settext하기
        edt_start=view.findViewById(R.id.edit_start);
        edt_end=view.findViewById(R.id.edit_end);
        //비콘 값 전달할때 사용하면 될듯
        /*if(flag_select!=0){
            //select item result 받아오기
            System.out.println("액티비티 몇번");
            String select_text="";
            if(getArguments()!=null) {
                select_text=getArguments().getString("select_item");
                System.out.println("여기다"+select_text);
            }
        }*/

        //unity project

        mUnityPlayer = new UnityPlayer(getActivity());


        //출발지 검색바
        ImageButton btn_start = view.findViewById(R.id.btn_search);
        final FragmentManager fm = getFragmentManager();
        final Find_dialog dialog_fragment = new Find_dialog();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edt 값 받아오기
                String search_value =edt_start.getText().toString();
                Bundle args = new Bundle();
                args.putString("search_value",search_value);
                flag_select=1;

                dialog_fragment.setArguments(args);
                dialog_fragment.show(fm,"dialog_test");
            }
        });


        //도착지 검색바
        ImageButton btn_end = view.findViewById(R.id.btn_search2);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edt 값 받아오기
                String search_value =edt_end.getText().toString();
                Bundle args = new Bundle();
                args.putString("search_value",search_value);
                flag_select=2;

                dialog_fragment.setArguments(args);
                dialog_fragment.show(fm,"dialog_test");
            }
        });


        ImageButton btn_location = view.findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getActivity(), Activity_Beacon.class);
                startActivity(intent1);
            }
        });
        String contents="success";


        Button btn_guide = view.findViewById(R.id.btn_guide);
        //final로 변환
        final String finalBeacon_str = beacon_str;

        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments()!=null) {
                    String select_start ="";
                    String select_end ="";
                    select_start=getArguments().getString("select_start");
                    select_end =getArguments().getString("select_end");
                    System.out.println("여기다"+ select_start);
                }

                Intent intent_unity=new Intent(getActivity(), UnityPlayerActivity.class);
                startActivity(intent_unity);
                /*Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        mUnityPlayer.UnitySendMessage("Cube","move","left");
                    }
                };*/
                //s2에 finalBeacon_str넣어주면 됨
                //도착
                mUnityPlayer.UnitySendMessage("PathShower","setDestination", "302");
                //시작
                mUnityPlayer.UnitySendMessage("IndoorNavController","CurPlace","304");
                //handler.sendEmptyMessageDelayed(0,9000);

            }
        });

        //즐겨찾기 버튼

        final FavoriteDatabase databaseManager=FavoriteDatabase.getInstance(getActivity());
        favoriteButton=(ToggleButton)view.findViewById(R.id.button_favorite);
        favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String search_value =edt_end.getText().toString();
                    databaseManager.insertColumn(search_value);
                    Toast.makeText(getActivity(),search_value,Toast.LENGTH_SHORT).show();
                }

            }
        });

        //즐겨찾기에서 자동으로 도착지 설정
        //Bundle bundle1 = getArguments();
        String favorite_start="";
        if (getArguments()!=null){
            System.out.println("과연 즐겨찾기는");
            favorite_start=getArguments().getString("favorite_start");
            edt_start.setText(favorite_start);
            System.out.println("즐겨찾기"+favorite_start);
        }


        return view;
    }


}
