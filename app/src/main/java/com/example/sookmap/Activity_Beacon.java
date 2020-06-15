package com.example.sookmap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import static com.example.sookmap.FragmentMap.beacon_flag;

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

        //sqlite query test
        String last_class="";
        DatabaseManager databaseManager=DatabaseManager.getInstance(this);
        TextView textView_test=(TextView)findViewById(R.id.sqlite_test);
        String[] columns = new String[] {"minor_id","class_id","class_name","class_value"};
        textView_test.setText("");
        Cursor cursor=databaseManager.query(columns,null,null,null,null,null);
        if(cursor !=null){
            while (cursor.moveToNext()){
                String beacon_id=cursor.getString(cursor.getColumnIndexOrThrow("minor_id"));
                String class_id=cursor.getString(cursor.getColumnIndexOrThrow("class_id"));
                String class_name=cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                String class_value=cursor.getString(cursor.getColumnIndexOrThrow("class_value"));
                textView_test.append("minor:"+beacon_id+" class_id:"+class_id+" class_name:"+class_name+'\n');
                System.out.println(beacon_id);
                last_class=class_value;
            }
        }
        //beacon

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
        //activity to fragment 값 전달
        FragmentMap fragmentMap = new FragmentMap();
        Button btn_check=(Button)findViewById(R.id.check_btn);
        final String finalLast_class = last_class;
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMap fragmentMap = new FragmentMap();
                Bundle bundle= new Bundle(1);
                bundle.putString("class_value", finalLast_class);
                fragmentMap.setArguments(bundle);
                beacon_flag=1;
                replaceFragment(fragmentMap);
            }
        });


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

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

}
