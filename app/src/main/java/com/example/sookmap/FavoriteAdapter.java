package com.example.sookmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class FavoriteAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> data;
    public FavoriteAdapter(Context context, ArrayList<String> data){
        this.context=context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public int getCount(){
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final View view = layoutInflater.inflate(R.layout.favorite_item, null);
        final TextView textView = view.findViewById(R.id.txt_favorite);
        textView.setText(data.get(position));

        ImageButton btn_start = view.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_favoriteStart dialog_favoriteStart = new Dialog_favoriteStart();
                String txt_favorite = "";
                txt_favorite =textView.getText().toString();
                final String txt_favoriteName="";

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("도착지로 설정하시겠습니까?");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentMap fragmentMap = new FragmentMap();
                        Bundle bundle= new Bundle(1);
                        bundle.putString("favorite_start", txt_favoriteName);
                        fragmentMap.setArguments(bundle);
                        ((MainActivity)MainActivity.mContext).replaceFragment(fragmentMap);
                    }
                });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        return view;
    }
}
