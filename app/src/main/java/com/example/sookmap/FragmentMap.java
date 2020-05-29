package com.example.sookmap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.Company.androidTest_cube.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;


public class FragmentMap extends Fragment {
    protected UnityPlayer mUnityPlayer;
    static int beacon_flag=0;

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
                beacon_str = bundle.getString("class_name");
            Log.d("activity to frag",beacon_str);
        }


        //unity project

        mUnityPlayer = new UnityPlayer(getActivity());

        ImageButton btn_dialog = view.findViewById(R.id.btn_search);
        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Find_dialog dialog_fragment = new Find_dialog();
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

                Intent intent_unity=new Intent(getActivity(), UnityPlayerActivity.class);
                startActivity(intent_unity);
                /*Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        mUnityPlayer.UnitySendMessage("Cube","move","left");
                    }
                };*/
                //s2에 finalBeacon_str넣어주면 됨
                mUnityPlayer.UnitySendMessage("Cube","move", "left");
                //handler.sendEmptyMessageDelayed(0,9000);

            }
        });

        return view;
    }

}
