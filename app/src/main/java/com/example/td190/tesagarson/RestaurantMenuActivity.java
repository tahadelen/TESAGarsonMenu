package com.example.td190.tesagarson;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.td190.tesagarson.Model.Category;
import com.example.td190.tesagarson.Model.Products;

import java.util.ArrayList;

public class RestaurantMenuActivity extends Activity {

    private ListView list;
    private CategoryAdapter adapter;
    private MyDBHandler db = new MyDBHandler(this, null, null, 1);
    public RestaurantMenuActivity CustomListView = null;
    public ArrayList<Products> products = new ArrayList<>();
    public ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        Intent intent = getIntent();
        String table = intent.getStringExtra("key");
        Toast.makeText(getApplicationContext(), "Table: " + table, Toast.LENGTH_SHORT).show();

        categories = db.getCategories();
        products = db.getProducts();

        CustomListView = this;

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.listView_menu_cat );  // List defined in XML ( See Below )

        adapter = new CategoryAdapter( CustomListView, categories, res );
        list.setAdapter( adapter );
    }

    public void onItemClick(int mPosition)
    {
        final Category tempValue = categories.get(mPosition);
        Toast.makeText(CustomListView, "Kategori: " + tempValue.get_catName(), Toast.LENGTH_LONG).show();
    }
}
