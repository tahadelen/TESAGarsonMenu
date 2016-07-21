package com.example.td190.tesagarson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.td190.tesagarson.Model.Category;
import com.example.td190.tesagarson.Model.Products;
import com.example.td190.tesagarson.Model.Tables;
import com.example.td190.tesagarson.Model.Users;
import java.io.ByteArrayOutputStream;

public class ActivityLogin extends Activity {

    private Button loginButton,cancelButton;
    private EditText nameText,passText;
    private TextView tx1;
    private int CTR = 3;

    public MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);

        addDataToDatabase(dbHandler);

        Toast.makeText(getApplicationContext(), "veri tabanında: " + dbHandler.databaseToString(), Toast.LENGTH_LONG).show();

        loginButton=(Button)findViewById(R.id.loginButton);
        nameText=(EditText)findViewById(R.id.nameText);
        passText=(EditText)findViewById(R.id.passText);

        cancelButton=(Button)findViewById(R.id.cancelButton);
        tx1=(TextView)findViewById(R.id.textView);
        tx1.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = dbHandler.getUser(nameText.getText().toString(), passText.getText().toString());
                if(user != null) {
                    Toast.makeText(getApplicationContext(), "Oldu",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(ActivityLogin.this, TablesActivity.class);
                    myIntent.putExtra("key", user.get_id()); //Optional parameters
                    ActivityLogin.this.startActivity(myIntent);
                    CTR=3;
                    tx1.setText(Integer.toString(CTR));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Hatalı Giriş",Toast.LENGTH_SHORT).show();

                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.WHITE);
                    CTR--;
                    tx1.setText(Integer.toString(CTR));

                    if (CTR == 0) {
                        loginButton.setEnabled(false);
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteAll();
                finish();
            }
        });
    }

    void addDataToDatabase (MyDBHandler dbHandler){

        Tables table = new Tables();
        Users user = new Users();
        Products product = new Products();
        Category category = new Category();

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.category);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte imageInByte[] = stream.toByteArray();

        Bitmap image_2 = BitmapFactory.decodeResource(getResources(), R.drawable.product);
        ByteArrayOutputStream stream_2 = new ByteArrayOutputStream();
        image_2.compress(Bitmap.CompressFormat.PNG, 100, stream_2);
        byte imageInByte_2[] = stream_2.toByteArray();

        for (int i=0; i<4; i++){
            category.set_catImg(imageInByte);
            category.set_catName("Kategori: " + i);
            dbHandler.addCat(category);
        }

        int j=0;

        for (int i=0; i<8; i++){
            product.set_price(i);
            product.set_productName("ürün: " + i);
            product.set_productCat("Kategori: " + j++);
            product.set_img(imageInByte_2);
            if(j==4)
                j=0;

            dbHandler.addProduct(product);
        }


        for (int i=0; i<2; i++){
            user.set_name("a"+i);
            user.set_password(Integer.toString(i));

            dbHandler.addUser(user);
        }

        for (int i = 0; i < 8; i++) {

            table.set_tableName("masa_" + i);
            table.set_tableCustNum(i);
            table.set_tableStatus(i%3);
            table.set_floor(i%2);

            dbHandler.addTable(table);
        }
    }

}
