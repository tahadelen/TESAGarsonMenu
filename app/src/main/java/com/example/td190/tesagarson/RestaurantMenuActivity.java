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

    private ListView list_cat;
    private ListView list_pro;
    private CategoryAdapter cat_adapter;
    private ProductAdapter pro_adapter;
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

        CustomListView = this;

        Resources res = getResources();
        list_cat = ( ListView )findViewById( R.id.listView_menu_cat );  // List defined in XML ( See Below )

        cat_adapter = new CategoryAdapter( CustomListView, categories, res );
        list_cat.setAdapter( cat_adapter );
    }

    public void onItemClick(int mPosition)
    {
        final Category tempValue = categories.get(mPosition);
        ArrayList<Products> filtred = new ArrayList<>();

        products = db.getProducts();

        CustomListView = this;
        Resources res = getResources();
        list_pro = (ListView)findViewById(R.id.listview_menu_pro);

        filtred.clear();
        for (int i=0; i<categories.size(); i++){
            for(int j=0; j<products.size(); j++) {
                if (products.get(j).get_productCat().equals(categories.get(i).get_catName())){
                    filtred.add(products.get(j));
                }
            }
        }

        pro_adapter = new ProductAdapter(CustomListView, filtred, res);
        list_pro.setAdapter(pro_adapter);

        //Toast.makeText(CustomListView, "Kategori: " + tempValue.get_catName(), Toast.LENGTH_LONG).show();
    }

}
