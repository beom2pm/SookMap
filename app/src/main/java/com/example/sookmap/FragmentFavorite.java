package com.example.sookmap;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class FragmentFavorite extends Fragment {

    public FragmentFavorite() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        //adapter
        ArrayList<String> list = new ArrayList<>();
        final ListView listView = (ListView) view.findViewById(R.id.favorite_listview);

        FavoriteDatabase databaseManager = FavoriteDatabase.getInstance(getActivity());

        String[] columns = new String[]{"class_name"};
        Cursor cursor = databaseManager.query(columns, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                System.out.println("들어가긴");
                String class_name = cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                list.add(class_name);
                System.out.println("여히" + class_name);
            }
        }
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getActivity(),list);
        listView.setAdapter(favoriteAdapter);
        // Inflate the layout for this fragment
        return view;

    }
}
