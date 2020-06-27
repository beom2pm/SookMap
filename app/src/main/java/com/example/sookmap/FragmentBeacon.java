package com.example.sookmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.example.sookmap.FragmentMap.beacon_flag;
import static com.example.sookmap.FragmentMap.edt_start;


public class FragmentBeacon extends DialogFragment implements BeaconConsumer{
    private BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();
    private String TAG = "ACTIVITY_BEACON";

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    TextView tv_beacon;

    public FragmentBeacon() {
        // Required empty public constructor
    }

    public static FragmentBeacon getInstance(){
        FragmentBeacon e = new FragmentBeacon();
        return e;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view =inflater.inflate(R.layout.fragment_beacon, container);

        //beacon

        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        tv_beacon = view.findViewById(R.id.textView_beacon);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
        //final double[] min_distance = {9999};
        //final Identifier[] min_id = new Identifier[1];


        final DatabaseManager databaseManager=DatabaseManager.getInstance(getActivity());
        final String[] class_name = {"없음"};
        @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                for(Beacon beacon:beaconList){
                    //db query
                    String[] columns = new String[] {"class_name"};
                    String selection = "minor_id = ?";
                    String[] selectionArgs={beacon.getId3().toString()};
                    Cursor cursor=databaseManager.query(columns,selection,selectionArgs,null,null,null);
                    if(cursor !=null) {
                        while (cursor.moveToNext()) {
                            tv_beacon.setText("ID:" + cursor.getString(cursor.getColumnIndexOrThrow("class_name"))+'\n');
                            class_name[0] =cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                        }
                    }
                    /*tv_beacon.append("ID:"+ beacon.getId3() +"/ Distance:"+
                            Double.parseDouble(String.format("%.3f",beacon.getDistance()))+"m");*/
                }

            }
        };

        Button btn_beacon = view.findViewById(R.id.btn_beacon);
        btn_beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(0);
            }
        });
        tv_beacon.setText("찾는중");

        handler.sendEmptyMessageDelayed(0,1000);

        //exit
        Button btn_check=view.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("비콘 클래스 확인"+class_name[0]);
                if(class_name[0]!=null){
                    edt_start.setText(class_name[0]);
                }
                //보내고 종료
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(FragmentBeacon.this).commit();
                fragmentManager.popBackStack();
            }
        });


        return view;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            //비콘이 감지되면 해당 함수가 호출됨 -> collection에는 감지된 비콘의 리스트가, region에는 비콘에 대응하는 객체가 들어옴
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons.size()>0){
                    Log.i(TAG,"The first beacon I see is about "+
                            "((Beacon)beacons.iterator().next()).getDistance()"+" meters away");
                    beaconList.clear();
                    for(Beacon beacon:beacons){
                        beaconList.add(beacon);
                    }
                }
            }
        });
        //beacon 상태 확인
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "연결됐다");
                Toast.makeText(getActivity(), "비콘 연결됨",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "연결ㄱ끊김");
                Toast.makeText(getActivity(), "비콘 연결끊김",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "연결ㄱ끊김"+state);

            }
        });
        try{
            beaconManager.startRangingBeaconsInRegion(new Region("myRanginUniqueId", null,null,null));
        } catch (RemoteException e) {

        }
    }


    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }
}