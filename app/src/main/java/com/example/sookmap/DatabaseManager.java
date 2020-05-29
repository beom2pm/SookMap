package com.example.sookmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    static final String DB_NAME="Beacon.db";
    static final String TABLE_BEACON="Beacon";
    static final int DB_VERSION = 1;

    Context myContext=null;
    private static DatabaseManager myDBManger=null;
    private SQLiteDatabase mydatabase=null;

    public static DatabaseManager getInstance(Context context){
        if(myDBManger == null){
            myDBManger = new DatabaseManager(context);
        }
        return myDBManger;
    }

    private DatabaseManager(Context context){
        myContext = context;

        //db open
        mydatabase = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE,null);

        //table 생성
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_BEACON+"("+
                        "minor_id TEXT PRIMARY KEY," +
                        "class_id TEXT,"+ "class_name TEXT);");
        //table insert
        insertColumn("m408",	"22200155",	"10435");
        insertColumn("m409","22200156",	"10437");
        insertColumn("m410A","222001572","10439");
        insertColumn("m413","22200158","10443");
        insertColumn("m414","22200159","10414");
        insertColumn("m415","22200160","10415");
        insertColumn("m416","22200161","10416");
        insertColumn("m417","22200162","10417");
        insertColumn("m418","22200163","10418");
        insertColumn("m420","22200164","10420");
        insertColumn("m421","22200165","10421");
        /*insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");*/

    }
    public void insertColumn(String class_name, String class_id, String beacon_id){
        ContentValues addValue = new ContentValues();
        addValue.put("minor_id",beacon_id);
        addValue.put("class_id",class_id);
        addValue.put("class_name",class_name);
        mydatabase.insert(TABLE_BEACON,null,addValue);
    }

    public Cursor query(String[] column, String selection, String[] selectionArgs, String groupBy, String having, String orderby){
        return mydatabase.query(TABLE_BEACON,column,selection,selectionArgs,groupBy,having,orderby);
    }

}
