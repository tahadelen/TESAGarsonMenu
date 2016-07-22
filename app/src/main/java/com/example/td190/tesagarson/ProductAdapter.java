package com.example.td190.tesagarson;

/**
 * Created by td190 on 21/07/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.td190.tesagarson.Model.Category;
import com.example.td190.tesagarson.Model.Products;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter implements OnClickListener{

    private Activity activity;
    private ArrayList products;
    private static LayoutInflater inflater=null;
    private Products modelProduct = new Products();
    public Resources res;

    public ProductAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        products=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if(products.size() < 0)
            return -1;
        else
            return products.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder{

        public TextView proName;
        public TextView proPrice;
        public ImageView proImage;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.proitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.proName = (TextView) vi.findViewById(R.id.proName);
            holder.proPrice = (TextView) vi.findViewById(R.id.proPrice);
            holder.proImage = (ImageView)vi.findViewById(R.id.proImage);

            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(products.size()<=0)
        {
            holder.proName.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            modelProduct = null;
            modelProduct = ( Products ) products.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.proName.setText( modelProduct.get_productName() );
            holder.proPrice.setText( Integer.toString(modelProduct.get_price()) );
            holder.proImage.setImageBitmap(BitmapFactory.decodeByteArray(modelProduct.get_img(), 0, modelProduct.get_img().length));

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener(position));
        }

        return vi;
    }

    private class OnItemClickListener  implements OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            RestaurantMenuActivity sct = (RestaurantMenuActivity) activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }

    @Override
    public void onClick(View view) {

    }

}
