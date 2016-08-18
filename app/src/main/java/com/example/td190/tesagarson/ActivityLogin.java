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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ActivityLogin extends Activity {

    private Button loginButton,cancelButton;
    private EditText nameText,passText;
    private TextView tx1, errorMsg;
    private int CTR = 3;

    public MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);

        dbHandler = new MyDBHandler(this, null, null, 1);
        addDataToDatabase(dbHandler);

        Toast.makeText(getApplicationContext(), "veri tabanında: " + dbHandler.databaseToString(), Toast.LENGTH_LONG).show();

        loginButton=(Button)findViewById(R.id.loginButton);
        nameText=(EditText)findViewById(R.id.nameText);
        passText=(EditText)findViewById(R.id.passText);
        errorMsg = (TextView)findViewById(R.id.login_error);

        cancelButton=(Button)findViewById(R.id.cancelButton);
        tx1=(TextView)findViewById(R.id.textView);
        tx1.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String password = passText.getText().toString();
                RequestParams params = new RequestParams();

                params.put("userName", name);
                params.put("userPassword", password);
                invokeWS(params);
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

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params){

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.1.4:8080/serviceDB/LoginUser/doLoginUser",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                        Intent myIntent = new Intent(ActivityLogin.this, TablesActivity.class);
                        myIntent.putExtra("key", Integer.toString(obj.getInt("userId"))); //Optional parameters
                        ActivityLogin.this.startActivity(myIntent);
                        CTR=3;
                        tx1.setText(Integer.toString(CTR));
                    }
                    // Else display error message
                    else{
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                        tx1.setVisibility(View.VISIBLE);
                        tx1.setBackgroundColor(Color.WHITE);
                        CTR--;
                        tx1.setText(Integer.toString(CTR));

                        if (CTR == 0) {
                            loginButton.setEnabled(false);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {

                Toast.makeText(getApplicationContext(), "code" + statusCode , Toast.LENGTH_LONG).show();
                error.printStackTrace();

                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void addDataToDatabase (MyDBHandler dbHandler){

        Tables table = new Tables();
        Products product = new Products();
        Category category = new Category();

        Bitmap imageGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        ByteArrayOutputStream streamGreen = new ByteArrayOutputStream();
        imageGreen.compress(Bitmap.CompressFormat.PNG, 100, streamGreen);
        byte imageInByteGreen[] = streamGreen.toByteArray();

        Bitmap imageRed = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        ByteArrayOutputStream streamRed = new ByteArrayOutputStream();
        imageRed.compress(Bitmap.CompressFormat.PNG, 100, streamRed);
        byte imageInByteRed[] = streamRed.toByteArray();

        Bitmap imageGrey = BitmapFactory.decodeResource(getResources(), R.drawable.grey);
        ByteArrayOutputStream streamGrey = new ByteArrayOutputStream();
        imageGrey.compress(Bitmap.CompressFormat.PNG, 100, streamGrey);
        byte imageInByteGrey[] = streamGrey.toByteArray();

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

        for (int i = 0; i < 8; i++) {

            table.set_tableName("masa_" + i);
            table.set_tableCustNum(i);
            table.set_tableStatus(i%3);
            table.set_floor(i%2);

            if(table.get_tableStatus()==0)
                table.set_image(imageInByteGreen);
            else if(table.get_tableStatus()==1)
                table.set_image(imageInByteRed);
            else
                table.set_image(imageInByteGrey);

            dbHandler.addTable(table);
        }
    }

}
