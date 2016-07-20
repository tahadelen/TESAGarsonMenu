package com.example.td190.tesagarson;

/**
 * Created by td190 on 18/07/2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper{

    //Version of the database
    private static final int DATABASE_VERSION = 1;
    //Name of my database file
    private static final String DATABASE_NAME = "restaurantRecords";

    //table for tables
    public static final String TABLE_TABLES = "tables";

    //Columns for table tables
    public static final String COLUMN_TABLE_ID = "_id";
    public static final String COLUMN_TABLENAME = "_tableName";
    public static final String COLUMN_FLOOR = "_floor";
    public static final String COLUMN_TABLECUSTNUM = "_tableCustNum";
    public static final String COLUMN_TABLESTATUS = "_tableStatus";

    //table for users
    public static final String TABLE_USERS = "users";

    //Columns for table users
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USERNAME = "_name";
    public static final String COLUMN_USERPASSWORD = "_password";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query_table = "CREATE TABLE " + TABLE_TABLES + "(" +
                COLUMN_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TABLENAME + " TEXT, " +
                COLUMN_FLOOR + " INTEGER, " +
                COLUMN_TABLECUSTNUM + " INTEGER, " +
                COLUMN_TABLESTATUS + " INTEGER " +
                ");";

        String query_user = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_USERPASSWORD + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query_user);
        sqLiteDatabase.execSQL(query_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    public void addTable(Tables table){
        ContentValues table_values = new ContentValues();
        table_values.put(COLUMN_TABLENAME, table.get_tableName());
        table_values.put(COLUMN_FLOOR, table.get_floor());
        table_values.put(COLUMN_TABLECUSTNUM, table.get_tableCustNum());
        table_values.put(COLUMN_TABLESTATUS, table.get_tableStatus());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TABLES, null, table_values);
        db.close();
    }

    public void addUser(Users user){
        ContentValues user_values = new ContentValues();
        user_values.put(COLUMN_USERNAME, user.get_name());
        user_values.put(COLUMN_USERPASSWORD, user.get_password());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERS, null, user_values);
        db.close();
    }

    public Users getUser(String User_Name, String User_Password){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_USERNAME + " = " + "'" + User_Name + "'"
                + " AND "
                + COLUMN_USERPASSWORD + " = " + "'" + User_Password + "'";

        //String query = "SELECT * FROM " + TABLE_USERS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);


        if (!(c.moveToFirst()) || c.getCount() == 0)
            return null;
        else {
            Users user = new Users();
            user.set_id(c.getInt(c.getColumnIndex(COLUMN_USER_ID)));
            user.set_name(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
            user.set_password(c.getString(c.getColumnIndex(COLUMN_USERPASSWORD)));

            return user;
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TABLES + ";" );
        db.execSQL("DELETE FROM " + TABLE_USERS + ";");
        db.close();
    }

    public void deleteTable(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TABLES + " WHERE " + COLUMN_TABLENAME + "=\"" + tableName + "\";");
    }

    public Cursor selectAll(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TABLES + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        return c;
    }

    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TABLES + " WHERE 1";

        //Cursor point to location in your results
        Cursor c = db.rawQuery(query,null);
        //move to the first raw in your results
        c.moveToFirst();

        do{
            if(c.getString(c.getColumnIndex("_tableName"))!=null){
                dbString += c.getString(c.getColumnIndex("_tableName"));
                dbString += " " + c.getString(c.getColumnIndex("_floor"));
                dbString += " " + c.getString(c.getColumnIndex("_tableStatus"));
                dbString += "\n";
            }
        }while(c.moveToNext());

        db.close();
        return dbString;
    }
}
