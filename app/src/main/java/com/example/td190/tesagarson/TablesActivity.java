package com.example.td190.tesagarson;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.td190.tesagarson.Model.Tables;

import java.util.ArrayList;

public class TablesActivity extends Activity{

    private Spinner floorDropDown;
    private static final String floors[] = {"0","1"};
    private MyDBHandler db = new MyDBHandler(this, null, null, 2);

    public TablesActivity CustomListView = null;
    public ArrayList<Tables> tables = new ArrayList<>();
    public ArrayList<Tables> f_tables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        getTables();

        CustomListView = this;

        floorDropDown = (Spinner) findViewById(R.id.floorSpinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, floors);
        
        floorDropDown.setSelection(1);
        floorDropDown.setAdapter(spinner_adapter);

        floorDropDown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = floorDropDown.getSelectedItemPosition();

                filtertable(position);

                GridView gridView = (GridView) findViewById(R.id.gridTable);
                CustomAdapter gridAdapter = new CustomAdapter(CustomListView, TablesActivity.this, R.layout.grid_table_item, f_tables);
                gridView.setAdapter(gridAdapter);

                Toast.makeText(getBaseContext(), "Selected: " + floors[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
               /* Resources res = getResources();
                list = ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

                adapter = new CustomAdapter( CustomListView, tables, res );
                list.setAdapter( adapter );*/
            }
        });


        Intent intent = getIntent();
        String user = intent.getStringExtra("key");
        Toast.makeText(getApplicationContext(), "kullanıcı: " + user, Toast.LENGTH_SHORT).show();

        /*CustomListView = this;

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        *//**************** Create Custom Adapter *********//*
        adapter = new CustomAdapter( CustomListView, f_tables, res );
        list.setAdapter( adapter );*/

    }

    public void getTables(){
        Cursor c = db.selectAll();
        int i=0;
        c.moveToFirst();
        do{
            if(c.getString(c.getColumnIndex("_tableName")) != null) {
                Tables table = new Tables();

                table.set_id(c.getInt(c.getColumnIndex("_tableId")));
                table.set_tableName(c.getString(c.getColumnIndex("_tableName")));
                table.set_floor(c.getInt(c.getColumnIndex("_floor")));
                table.set_tableStatus(c.getInt(c.getColumnIndex("_tableStatus")));
                table.set_tableCustNum(c.getInt(c.getColumnIndex("_tableCustNum")));
                table.set_image(c.getBlob(c.getColumnIndex("_tableImg")));

                tables.add(i, table);
                i++;
            }
        }while(c.moveToNext());
    }

    public void filtertable(int position){

        //tables.addAll(f_tables);
        f_tables.clear();
        int y=0;
        for (int i = 0 ; i < tables.size() ; i++){
            if(tables.get(i).get_floor()==position){
                f_tables.add(y, tables.get(i));
                y++;
            }
        }
    }

    public void onItemClick(int mPosition)
    {
        final Tables tempValues = ( Tables ) tables.get(mPosition);
        final int _tableId = f_tables.get(mPosition).get_id();


        //Toast.makeText(CustomListView, tempValues.getTableName() + " " + tempValues.getTableCustNum() + " kişi var" + " masa durumu: "+tempValues.getTableStatus(), Toast.LENGTH_LONG).show();
        Intent newIntent = new Intent(TablesActivity.this, RestaurantMenuActivity.class);
        newIntent.putExtra("key", _tableId);
        TablesActivity.this.startActivity(newIntent);

        Toast.makeText(CustomListView, tempValues.get_tableName() + " " + tempValues.get_tableCustNum() + " kişi var" + " masa durumu: "+tempValues.get_tableStatus(), Toast.LENGTH_LONG).show();
    }

}
