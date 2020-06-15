package com.example.sookmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    static final String DB_NAME="Sookmap.db";
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

        //mydatabase.execSQL("DROP TABLE "+TABLE_BEACON);
        //table 생성
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_BEACON+"("+
                "minor_id TEXT PRIMARY KEY," +
                "class_id TEXT,"+ "class_value TEXT,"+"class_name TEXT);");
        //table insert
        insertColumn("명신관 408",	"22200155",	"10435","m408");
        insertColumn("명신관 409","22200156",	"10437","m409");
        insertColumn("명신관 410A","222001572","10439","m410A");
        insertColumn("명신관 413","22200158","10443","m413");
        insertColumn("명신관 414","22200159","10414","m414");
        insertColumn("명신관 415","22200160","10415","m415");
        insertColumn("명신관 416","22200161","10416","m416");
        insertColumn("명신관 417","22200162","10417","m417");
        insertColumn("명신관 418","22200163","10418","m418");
        insertColumn("명신관 420","22200164","10420","m420");
        insertColumn("명신관 421","22200165","10421","m421");
        insertColumn("명신관 423","22200166","10423","m423");
       /* insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");
        insertColumn("","","");*/

    }
    public void insertColumn(String class_name, String class_id, String beacon_id,String class_value){
        ContentValues addValue = new ContentValues();
        addValue.put("minor_id",beacon_id);
        addValue.put("class_id",class_id);
        addValue.put("class_value",class_value);
        addValue.put("class_name",class_name);
        mydatabase.insert(TABLE_BEACON,null,addValue);
    }

    public Cursor query(String[] column, String selection, String[] selectionArgs, String groupBy, String having, String orderby){
        return mydatabase.query(TABLE_BEACON,column,selection,selectionArgs,groupBy,having,orderby);
    }

}
