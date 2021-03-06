package com.example.td190.tesagarson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridView;

import com.example.td190.tesagarson.Model.Category;
import com.example.td190.tesagarson.Model.Orders;
import com.example.td190.tesagarson.Model.Products;
import com.example.td190.tesagarson.Model.Tables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class RestaurantMenuActivity extends Activity {

    private ListView list_cat;
    private ListView list_pro;
    private Products product;

    private CategoryAdapter cat_adapter;
    private ProductAdapter pro_adapter;
    private MyDBHandler db = new MyDBHandler(this, null, null, 1);
    private Resources res;
    private ArrayList<Products> products = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Orders> choices = new ArrayList<>();
    private ArrayList<Products> filtred = new ArrayList<>();
    private ArrayList<Orders> exists = new ArrayList<>();
    private Orders chc;

    private Button addButton, removeButton, sendButton, deleteButton, addPortion, removePortion;
    private TextView quantity;
    private TextView quantityP;
    private int ctrQuantity = 0;
    private double ctrPortion = 0.0;
    private static final double ADD = 0.5;

    public RestaurantMenuActivity CustomListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        new RequestItemsServiceTask().execute();

        addButton = (Button) findViewById(R.id.addButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        addPortion = (Button) findViewById(R.id.addPortion);
        removePortion = (Button) findViewById(R.id.removePortion);

        quantity = (TextView) findViewById(R.id.quantity);
        quantity.setVisibility(View.GONE);
        quantityP = (TextView) findViewById(R.id.quantityP);
        quantityP.setVisibility(View.GONE);

        Intent intent = getIntent();
        final int tableId = intent.getIntExtra("key", 0);
        Toast.makeText(getApplicationContext(), "The id of the tabel: " + tableId, Toast.LENGTH_SHORT).show();

        choices = db.getOrders(tableId);

//fix the product name problem
        GridView gridview = (GridView) findViewById(R.id.choosen);
        gridview.setAdapter(new GridviewAdapter(RestaurantMenuActivity.this, exists));

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ctrQuantity++;
                quantity.setText(Integer.toString(ctrQuantity));
                quantity.setVisibility(View.VISIBLE);
            }
        });

        addPortion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ctrPortion+=ADD;
                quantityP.setText(Double.toString(ctrPortion));
                quantityP.setVisibility(View.VISIBLE);
            }
        });

        removePortion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ctrPortion-=ADD;
                quantityP.setText(Double.toString(ctrPortion));
                quantityP.setVisibility(View.VISIBLE);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ctrQuantity--;
                quantity.setText(Integer.toString(ctrQuantity));
                quantity.setVisibility(View.VISIBLE);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                choices.remove(chc);
                db.deleteOrder(chc);
                ArrayList<Orders> remainOrders = db.getOrders(tableId);
                GridView gridview = (GridView) findViewById(R.id.choosen);
                gridview.setAdapter(new GridviewAdapter(RestaurantMenuActivity.this, remainOrders));
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Orders choice = new Orders();

                if (product == null)
                    return;

                choice.setProduct(product);
                choice.setPiece(ctrQuantity);
                choice.setPortion(ctrPortion);
                choice.setTable(db.getTable(tableId));
                db.addChoice(choice);

                choices.add(choice);

                ctrQuantity = 0;
                ctrPortion = 0.0;
                quantity.setText(Integer.toString(ctrQuantity));
                quantityP.setText(Double.toString(ctrPortion));

                GridView gridview = (GridView) findViewById(R.id.choosen);
                gridview.setAdapter(new GridviewAdapter(RestaurantMenuActivity.this, choices));
            }
        });
    }

    /**
     * populate list in background while showing progress dialog.
     */
    private class RequestItemsServiceTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(RestaurantMenuActivity.this);

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
                takeCategories();
                takeProducts();
                takeOrderExist();
                dialog.hide();
            } catch (Throwable e) {
                // handle exceptions
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            dialog.hide();
            CustomListView = RestaurantMenuActivity.this;
            res = getResources();

            list_cat = ( ListView )findViewById( R.id.listView_menu_cat );  // List defined in XML ( See Below )

            cat_adapter = new CategoryAdapter( CustomListView, categories, res );
            list_cat.setAdapter( cat_adapter );

        }

    }

    private void takeOrderExist(){
        Intent intent = getIntent();
        final int tableId = intent.getIntExtra("key", 0);
        JSONArray itemsArray;
        JSONObject itemsObject;
        JSONObject serviceResult = RequestWebService.requestWebService("http://192.168.1.4:8080/serviceDB/TransectionOrder/getOrders?tableId=" + Integer.toString(tableId));

        try {
            Object items = serviceResult.get("orders");

            if (items instanceof JSONArray){
                itemsArray = (JSONArray) items;

                Log.d("items", "item sayısı: " + Integer.toString(itemsArray.length()));
                for (int i = 0; i < itemsArray.length(); i++) {
                    Tables table = new Tables();
                    Products product = new Products();
                    JSONObject obj = itemsArray.getJSONObject(i);
                    product.set_productName(obj.getJSONObject("product").getString("_productName"));
                    table.set_id(tableId);
                    exists.add(new Orders(obj.getInt("orderId"), table, obj.getInt("piece"), obj.getDouble("portion"), product));
                }
            } else if (items instanceof JSONObject){
                itemsObject = (JSONObject) items;
                Log.d("itemObject:      ", Integer.toString(itemsObject.getInt("orderId")));
                Tables table = new Tables();
                Products product = new Products();
                product.set_productName(itemsObject.getJSONObject("product").getString("_productName"));
                table.set_id(tableId);
                exists.add(new Orders(itemsObject.getInt("orderId"), table, itemsObject.getInt("piece"), itemsObject.getDouble("portion"), product));
            }

        } catch (JSONException e) {
            // handle exception
        }

    }

    private void takeCategories(){
        JSONObject serviceResult = RequestWebService.requestWebService("http://192.168.1.4:8080/serviceDB/TransectionCategory/getCategory");
        JSONArray itemsArray;
        JSONObject itemsObject;

        try {
            Object items = serviceResult.getJSONArray("category");
            if (items instanceof JSONArray) {
                itemsArray = (JSONArray) items;

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.category);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte imageInByte[] = stream.toByteArray();

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject obj = itemsArray.getJSONObject(i);
                    categories.add(new Category(obj.getInt("_id"), obj.getString("_catName"), imageInByte));
                }
            } else if (items instanceof JSONObject){
                itemsObject = (JSONObject) items;

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.category);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte imageInByte[] = stream.toByteArray();
                    categories.add(new Category(itemsObject.getInt("_id"), itemsObject.getString("_catName"), imageInByte));
                }

        } catch (JSONException e) {
            // handle exception
        }

    }

    private void takeProducts(){

        JSONObject serviceResult = RequestWebService.requestWebService("http://192.168.1.4:8080/serviceDB/TransectionProduct/getProducts");
        JSONArray itemsArray;
        JSONObject itemsObject;

        try {
            Object items = serviceResult.getJSONArray("product");
            if (items instanceof JSONArray) {

                itemsArray = (JSONArray) items;

                Bitmap image_2 = BitmapFactory.decodeResource(getResources(), R.drawable.product);
                ByteArrayOutputStream stream_2 = new ByteArrayOutputStream();
                image_2.compress(Bitmap.CompressFormat.PNG, 100, stream_2);
                byte imageInByte_2[] = stream_2.toByteArray();

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject obj = itemsArray.getJSONObject(i);
                    products.add(new Products(obj.getInt("_id"), obj.getString("_productName"), obj.getString("_productCat"), imageInByte_2, obj.getInt("_price")));
                }
            }
            else if (items instanceof JSONObject){
                itemsObject = (JSONObject) items;

                Bitmap image_2 = BitmapFactory.decodeResource(getResources(), R.drawable.product);
                ByteArrayOutputStream stream_2 = new ByteArrayOutputStream();
                image_2.compress(Bitmap.CompressFormat.PNG, 100, stream_2);
                byte imageInByte_2[] = stream_2.toByteArray();

                products.add(new Products(itemsObject.getInt("_id"), itemsObject.getString("_productName"), itemsObject.getString("_productCat"), imageInByte_2, itemsObject.getInt("_price")));
            }

        } catch (JSONException e) {
            // handle exception
        }

    }

    private void clearList(){
        ArrayList<Products> empty = new ArrayList<>();

        empty.clear();
        empty.add(new Products());

        list_pro = (ListView)findViewById(R.id.listview_menu_pro);

        pro_adapter = new ProductAdapter(CustomListView, empty, res);
        pro_adapter.notifyDataSetChanged();
        list_pro.setAdapter(pro_adapter);
    }

    public void onItemProClick(int mPosition){
        product = filtred.get(mPosition);
    }

    public void onGridItemClick(int mPosition){
        chc = choices.get(mPosition);
    }

    public void onItemCatClick(int mPosition)
    {
        final Category tempValue = categories.get(mPosition);

        clearList();

        //There is some problem with this loop
        //
        filtred.clear();
        for (int i=0; i<products.size(); i++){
                if (products.get(i).get_productCat().equals(tempValue.get_catName())){
                    filtred.add(products.get(i));
                }
        }

        pro_adapter = new ProductAdapter(CustomListView, filtred, res);
        list_pro.setAdapter(pro_adapter);

    }

}
