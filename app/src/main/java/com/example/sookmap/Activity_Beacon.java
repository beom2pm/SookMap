package com.example.sookmap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class Activity_Beacon extends AppCompatActivity implements BeaconConsumer {
    private BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();
    private String TAG = "ACTIVITY_BEACON";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        textView = (TextView)findViewById(R.id.textView_beacon);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.setRangeNotifier(new RangeNotifier() {
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
                Toast.makeText(Activity_Beacon.this, "비콘 연결됨",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "연결ㄱ끊김");
                Toast.makeText(Activity_Beacon.this, "비콘 연결끊김",Toast.LENGTH_SHORT).show();

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
    public void OnButtonClicked(View view){
        handler.sendEmptyMessage(0);
    }
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            textView.setText("");
            //비콘 아이디와 거리를 측정해 textview에 붙임
            for(Beacon beacon:beaconList){
                textView.append("ID:"+beacon.getId3()+"/ Distance:"+
                        Double.parseDouble(String.format("%.3f",beacon.getDistance()))+"m\n");

            }
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };

}
