package com.example.td190.tesagarson;

/**
 * Created by td190 on 18/07/2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import com.example.td190.tesagarson.Model.Tables;
import com.example.td190.tesagarson.Model.Users;
import com.example.td190.tesagarson.Model.Products;
import com.example.td190.tesagarson.Model.Category;
import com.example.td190.tesagarson.Model.Orders;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper{

    //Version of the database
    private static final int DATABASE_VERSION = 1;
    //Name of my database file
    private static final String DATABASE_NAME = "restaurantRecords";

    //Table for orders
    public static final String TABLE_ORDERS = "orders";
    //Columns for products
    public static final String COLUMN_ORDER_ID = "_orderId";
    public static final String COLUMN_ORDER_TABLE_ID = "_tableId";
    public static final String COLUMN_ORDER_PRODUCT_ID = "_productId";
    public static final String COLUMN_ORDER_PIECE = "_piece";
    public static final String COLUMN_ORDER_PORTION = "_portion";

    //Table for products
    public static final String TABLE_PRODUCTS = "products";
    //Columns for products
    public static final String COLUMN_PRODUCT_ID = "_productId";
    public static final String COLUMN_PRODUCTNAME = "_productName";
    public static final String COLUMN_PRODUCTCAT = "_productCat";
    public static final String COLUMN_PRODUCTIMG = "_img";
    public static final String COLUMN_PRODUCTPRICE = "_price";

    //Table for categories
    public static final String TABLE_CATEGORY = "category";
    //Columns for categories
    public static final String COLUMN_CATID = "_catId";
    public static final String COLUMN_CATNAME = "_catName";
    public static final String COLUMN_CATIMG = "_catImg";

    //Table for tables
    public static final String TABLE_TABLES = "tables";
    //Columns for table tables
    public static final String COLUMN_TABLE_ID = "_tableId";
    public static final String COLUMN_TABLENAME = "_tableName";
    public static final String COLUMN_FLOOR = "_floor";
    public static final String COLUMN_TABLECUSTNUM = "_tableCustNum";
    public static final String COLUMN_TABLESTATUS = "_tableStatus";
    public static final String COLUMN_TABLEIMG = "_tableImg";

    //table for users
    public static final String TABLE_USERS = "users";
    //Columns for table users
    public static final String COLUMN_USER_ID = "_userId";
    public static final String COLUMN_USERNAME = "_userName";
    public static final String COLUMN_USERPASSWORD = "_userPassword";

    private ArrayList<Products> productList = new ArrayList<>();
    private ArrayList<Orders> orderList = new ArrayList<>();
    private ArrayList<Category> categoryList = new ArrayList<>();

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
                COLUMN_TABLESTATUS + " INTEGER, " +
                COLUMN_TABLEIMG + " BLOB " +
                ");";

        String query_cat = "CREATE TABLE " + TABLE_CATEGORY + "(" +
                COLUMN_CATID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATNAME + " TEXT, " +
                COLUMN_CATIMG + " BLOB " +
                ");";

        String query_product = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCTNAME + " TEXT, " +
                COLUMN_PRODUCTCAT + " TEXT, " +
                COLUMN_PRODUCTIMG + " BLOB, " +
                COLUMN_PRODUCTPRICE + " INTEGER " +
                ");";
//Put product id as foreign key
        String query_order = "CREATE TABLE " + TABLE_ORDERS + "(" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_PIECE + " INTEGER, " +
                COLUMN_ORDER_PORTION + " INTEGER, " +
                COLUMN_ORDER_TABLE_ID + " INTEGER, " +
                COLUMN_ORDER_PRODUCT_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ORDER_TABLE_ID + ")" + " REFERENCES " + TABLE_TABLES + "(" + COLUMN_TABLE_ID + ")," +
                " FOREIGN KEY(" + COLUMN_ORDER_PRODUCT_ID + ")" + " REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" +
                ");";

        sqLiteDatabase.execSQL(query_table);
        sqLiteDatabase.execSQL(query_cat);
        sqLiteDatabase.execSQL(query_product);
        sqLiteDatabase.execSQL(query_order);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(sqLiteDatabase);
    }

    public void addTable(Tables table){
        ContentValues table_values = new ContentValues();
        table_values.put(COLUMN_TABLENAME, table.get_tableName());
        table_values.put(COLUMN_FLOOR, table.get_floor());
        table_values.put(COLUMN_TABLECUSTNUM, table.get_tableCustNum());
        table_values.put(COLUMN_TABLESTATUS, table.get_tableStatus());
        table_values.put(COLUMN_TABLEIMG, table.get_image());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TABLES, null, table_values);
        db.close();
    }

    public void addProduct(Products product){
        ContentValues product_values = new ContentValues();
        product_values.put(COLUMN_PRODUCTNAME, product.get_productName());
        product_values.put(COLUMN_PRODUCTCAT, product.get_productCat());
        product_values.put(COLUMN_PRODUCTPRICE, product.get_price());
        product_values.put(COLUMN_PRODUCTIMG, product.get_img());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, product_values);
        db.close();
    }

    public void addCat(Category category){
        ContentValues category_values = new ContentValues();
        category_values.put(COLUMN_CATNAME, category.get_catName());
        category_values.put(COLUMN_CATIMG,  category.get_catImg());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CATEGORY, null, category_values);
        db.close();
    }

    public Tables getTable(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TABLES + " WHERE "
                + COLUMN_TABLE_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Tables td = new Tables();
        td.set_id(c.getInt(c.getColumnIndex(COLUMN_TABLE_ID)));
        td.set_tableName((c.getString(c.getColumnIndex(COLUMN_TABLENAME))));
        td.set_image(c.getBlob(c.getColumnIndex(COLUMN_TABLEIMG)));
        td.set_floor(c.getInt(c.getColumnIndex(COLUMN_FLOOR)));
        td.set_tableCustNum(c.getInt(c.getColumnIndex(COLUMN_TABLECUSTNUM)));
        td.set_tableStatus(c.getInt(c.getColumnIndex(COLUMN_TABLESTATUS)));

        return td;
    }

    public Products getProduct(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE "
                + COLUMN_PRODUCT_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Products td = new Products();
        td.set_id(c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID)));
        td.set_img((c.getBlob(c.getColumnIndex(COLUMN_PRODUCTIMG))));
        td.set_price(c.getInt(c.getColumnIndex(COLUMN_PRODUCTPRICE)));
        td.set_productCat(c.getString(c.getColumnIndex(COLUMN_PRODUCTCAT)));
        td.set_productName(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)));

        return td;
    }

    public void addChoice(Orders order){
        ContentValues order_values = new ContentValues();
        order_values.put(COLUMN_ORDER_PIECE, order.getPiece());
        order_values.put(COLUMN_ORDER_PORTION, order.getPortion());
        order_values.put(COLUMN_ORDER_TABLE_ID, order.getTable().get_id());
        order_values.put(COLUMN_ORDER_PRODUCT_ID, order.getProduct().get_id());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ORDERS, null, order_values);
        db.close();
    }

    public void deleteOrder(Orders order){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ORDERS, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(order.getOrderId())});
    }

    public ArrayList getOrders(int oId){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " +
                COLUMN_ORDER_TABLE_ID + " = " + oId;

        Cursor c = db.rawQuery(query, null);
        orderList.clear();

        if(!(c.moveToFirst()) || c.getCount() == 0){
            db.close();
            return orderList;
        }else{
            do{
                Orders order = new Orders();

                Tables table = getTable(oId);
                Products product = getProduct(c.getInt(c.getColumnIndex(COLUMN_ORDER_PRODUCT_ID)));
                order.setTable(table);
                order.setProduct(product);

                order.setPiece(c.getInt(c.getColumnIndex(COLUMN_ORDER_PIECE)));
                order.setPortion(c.getInt(c.getColumnIndex(COLUMN_ORDER_PORTION)));
                order.setOrderId(c.getInt(c.getColumnIndex(COLUMN_ORDER_ID)));

                orderList.add(order);
            }while(c.moveToNext());

            db.close();
            return orderList;
        }
    }

    public ArrayList getProducts() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        productList.clear();

        if (!(c.moveToFirst()) || c.getCount() == 0) {
            db.close();
            return null;
        }else {
            do{
                Products product = new Products();
                product.set_id(c.getInt(c.getColumnIndex(COLUMN_PRODUCT_ID)));
                product.set_productName(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)));
                product.set_productCat(c.getString(c.getColumnIndex(COLUMN_PRODUCTCAT)));
                product.set_img(c.getBlob(c.getColumnIndex(COLUMN_PRODUCTIMG)));
                product.set_price(c.getInt(c.getColumnIndex(COLUMN_PRODUCTPRICE)));

                productList.add(product);
            }while (c.moveToNext());
            db.close();
            return productList;

        }
    }

    public ArrayList getCategories(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE 1";
        Cursor c = db.rawQuery(query, null);

        if(!(c.moveToFirst()) || c.getCount() == 0){
            db.close();
            return null;
        }else{
            do {
                Category category = new Category();
                category.set_id(c.getInt(c.getColumnIndex(COLUMN_CATID)));
                category.set_catName(c.getString(c.getColumnIndex(COLUMN_CATNAME)));
                category.set_catImg(c.getBlob(c.getColumnIndex(COLUMN_CATIMG)));

                categoryList.add(category);
            }while(c.moveToNext());
            db.close();
            return categoryList;
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TABLES + ";" );
        db.execSQL("DELETE FROM " + TABLE_USERS + ";");
        db.execSQL("DELETE FROM " + TABLE_CATEGORY + ";");
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + ";");
        db.close();
    }

    public void deleteTable(){
        //String tableName
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM " + TABLE_TABLES + " WHERE " + COLUMN_TABLENAME + "=\"" + tableName + "\";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.close();
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
