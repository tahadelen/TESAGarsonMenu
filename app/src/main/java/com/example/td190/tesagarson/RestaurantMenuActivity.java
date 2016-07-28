package com.example.td190.tesagarson;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridView;

import com.example.td190.tesagarson.Model.Category;
import com.example.td190.tesagarson.Model.ChoosenProduct;
import com.example.td190.tesagarson.Model.Products;

import java.util.ArrayList;

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
    private ArrayList<ChoosenProduct> choices = new ArrayList<>();
    private ArrayList<Products> filtred = new ArrayList<>();
    private ChoosenProduct chc;

    private Button addButton, removeButton, sendButton, deleteButton;
    private TextView quantity;
    private int ctrQuantity = 0;

    public RestaurantMenuActivity CustomListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        addButton = (Button) findViewById(R.id.addButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        quantity = (TextView) findViewById(R.id.quantity);
        quantity.setVisibility(View.GONE);

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ctrQuantity++;
                quantity.setText(Integer.toString(ctrQuantity));
                quantity.setVisibility(View.VISIBLE);
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
                GridView gridview = (GridView) findViewById(R.id.choosen);
                gridview.setAdapter(new GridviewAdapter(RestaurantMenuActivity.this, choices));
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ChoosenProduct choice = new ChoosenProduct();

                choice.setProduct(product);
                choice.setPiece(ctrQuantity);
                choice.setPortion(1.5);

                choices.add(choice);

                ctrQuantity = 0;
                quantity.setText(Integer.toString(ctrQuantity));

                GridView gridview = (GridView) findViewById(R.id.choosen);
                gridview.setAdapter(new GridviewAdapter(RestaurantMenuActivity.this, choices));
            }
        });

        Intent intent = getIntent();
        String table = intent.getStringExtra("key");
        Toast.makeText(getApplicationContext(), "Table: " + table, Toast.LENGTH_SHORT).show();

        categories = db.getCategories();

        CustomListView = this;
        res = getResources();
        //Resources res = getResources();

        list_cat = ( ListView )findViewById( R.id.listView_menu_cat );  // List defined in XML ( See Below )

        cat_adapter = new CategoryAdapter( CustomListView, categories, res );
        list_cat.setAdapter( cat_adapter );
    }

    public void clearList(){
        ArrayList<Products> empty = new ArrayList<>();

        empty.clear();
        empty.add(new Products());

        //CustomListView = this;
        //Resources res = getResources();
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

        //
       /* pro_adapter = new ProductAdapter(CustomListView, empty, res);
        pro_adapter.notifyDataSetChanged();
        list_pro.setAdapter(pro_adapter);*/

        products = db.getProducts();

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

        //Toast.makeText(CustomListView, "Kategori: " + tempValue.get_catName(), Toast.LENGTH_LONG).show();
    }

}
