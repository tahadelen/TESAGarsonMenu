package com.example.td190.tesagarson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.td190.tesagarson.Model.Tables;

import java.io.ByteArrayOutputStream;

import java.util.ArrayList;


public class TablesActivity extends Activity{

    private Spinner floorDropDown;
    private static final String floors[] = {"0","1"};

    public TablesActivity CustomListView = null;
    public ArrayList<Tables> tables = new ArrayList<>();
    public ArrayList<Tables> f_tables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RequestItemsServiceTask().execute();
    }

    /**
     * populate list in background while showing progress dialog.
     */
    private class RequestItemsServiceTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(TablesActivity.this);

        @Override
        protected void onPreExecute() {
            // TODO i18n
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... unused) {
            // The ItemService would contain the method showed
            // in the previous paragraph
            try {
                findAllItems();
                dialog.hide();
            } catch (Throwable e) {
                // handle exceptions
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            setContentView(R.layout.activity_tables);

            CustomListView = TablesActivity.this;

            floorDropDown = (Spinner) findViewById(R.id.floorSpinner);
            ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(TablesActivity.this, android.R.layout.simple_spinner_dropdown_item, floors);

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
        }
    }

    public void findAllItems() {
        JSONObject serviceResult = RequestWebService.requestWebService("http://192.168.1.4:8080/serviceDB/getTables/tables");

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte imageGreen[] = stream.toByteArray();

        Bitmap image_2 = BitmapFactory.decodeResource(getResources(), R.drawable.grey);
        ByteArrayOutputStream stream_2 = new ByteArrayOutputStream();
        image_2.compress(Bitmap.CompressFormat.PNG, 100, stream_2);
        byte imageGrey[] = stream_2.toByteArray();

        Bitmap image_3 = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        ByteArrayOutputStream stream_3 = new ByteArrayOutputStream();
        image_3.compress(Bitmap.CompressFormat.PNG, 100, stream_3);
        byte imageRed[] = stream_3.toByteArray();

        try {
            JSONArray items = serviceResult.getJSONArray("tables");

            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                if (obj.getInt("_tableStatus")==0){
                    tables.add(new Tables(obj.getInt("_id"), obj.getString("_tableName"), obj.getInt("_floor"), obj.getInt("_tableCustNum"), obj.getInt("_tableStatus"), imageGreen));
                } else if (obj.getInt("_tableStatus")==1){
                    tables.add(new Tables(obj.getInt("_id"), obj.getString("_tableName"), obj.getInt("_floor"), obj.getInt("_tableCustNum"), obj.getInt("_tableStatus"), imageGrey));
                } else if (obj.getInt("_tableStatus")==2){
                    tables.add(new Tables(obj.getInt("_id"), obj.getString("_tableName"), obj.getInt("_floor"), obj.getInt("_tableCustNum"), obj.getInt("_tableStatus"), imageRed));
                }
            }



        } catch (JSONException e) {
            // handle exception
        }
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
        final Tables tempValues = tables.get(mPosition);
        final int _tableId = f_tables.get(mPosition).get_id();


        //Toast.makeText(CustomListView, tempValues.getTableName() + " " + tempValues.getTableCustNum() + " kişi var" + " masa durumu: "+tempValues.getTableStatus(), Toast.LENGTH_LONG).show();
        Intent newIntent = new Intent(TablesActivity.this, RestaurantMenuActivity.class);
        newIntent.putExtra("key", _tableId);
        TablesActivity.this.startActivity(newIntent);

        Toast.makeText(CustomListView, tempValues.get_tableName() + " " + tempValues.get_tableCustNum() + " kişi var" + " masa durumu: "+tempValues.get_tableStatus(), Toast.LENGTH_LONG).show();
    }

}
