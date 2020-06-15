package com.example.sookmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavoriteDatabase {
    static final String DB_NAME="Sookmap.db";
    static final String TABLE_NAME="Favorite";
    static final int DB_VERSION = 1;

    Context myContext=null;
    private static FavoriteDatabase myDBManger=null;
    private SQLiteDatabase mydatabase=null;

    public static FavoriteDatabase getInstance(Context context){
        if(myDBManger == null){
            myDBManger = new FavoriteDatabase(context);
        }
        return myDBManger;
    }

    private FavoriteDatabase(Context context){
        myContext = context;

        //db open
        mydatabase = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE,null);

        //table 생성
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+
                "class_name TEXT PRIMARY KEY);");

    }
    public void insertColumn( String class_name){
        ContentValues addValue = new ContentValues();
        //addValue.put("class_id",class_id);
        addValue.put("class_name",class_name);
        mydatabase.insert(TABLE_NAME,null,addValue);
    }

    public Cursor query(String[] column, String selection, String[] selectionArgs, String groupBy, String having, String orderby){
        return mydatabase.query(TABLE_NAME,column,selection,selectionArgs,groupBy,having,orderby);
    }
}
