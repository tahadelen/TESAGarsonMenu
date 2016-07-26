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
import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter implements OnClickListener {

    private Activity activity;
    private ArrayList categories;
    private static LayoutInflater inflater=null;
    private Category modelCategory = new Category();

    public Resources res;

    public CategoryAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        categories=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if(categories.size() <= 0)
            return -1;
        else
            return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void onClick(View view) {

    }

    public static class ViewHolder{

        public TextView catName;
        public ImageView catImage;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.catitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.catName = (TextView) vi.findViewById(R.id.catName);
            holder.catImage=(ImageView)vi.findViewById(R.id.catImage);

            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(categories.size()<=0)
        {
            holder.catName.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            modelCategory=null;
            modelCategory = ( Category ) categories.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.catName.setText( modelCategory.get_catName() );
            holder.catImage.setImageBitmap(BitmapFactory.decodeByteArray(modelCategory.get_catImg(), 0, modelCategory.get_catImg().length));

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

            sct.onItemCatClick(mPosition);
        }
    }
}
