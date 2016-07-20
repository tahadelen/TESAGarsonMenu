package com.example.td190.tesagarson;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by td190 on 14/07/2016.
 */
public class CustomAdapter extends BaseAdapter implements OnClickListener{

    private Activity activity;
    private ArrayList tables;
    private static LayoutInflater inflater=null;
    public Resources res;
    Tables modelTable = new Tables();

    public CustomAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        tables=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if(tables.size() <= 0)
            return -1;
        else
            return tables.size();
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

        public TextView tableName;
        public TextView H_tableCustNum;
        public TextView H_tableStatus;
        public ImageView image;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.tableName = (TextView) vi.findViewById(R.id.tableName);
            holder.H_tableCustNum=(TextView)vi.findViewById(R.id.H_tableCustNum);
            holder.H_tableStatus=(TextView) vi.findViewById(R.id.H_tableStatus);
            holder.image=(ImageView)vi.findViewById(R.id.image);

            /************  Set holder with LayoutInflater ************/
            if (modelTable.get_tableStatus()==0)
                vi.setBackgroundColor(0xFF58FA58);
            else if (modelTable.get_tableStatus()==1)
                vi.setBackgroundColor(0xFFFE2E2E);
            else if (modelTable.get_tableStatus()==2)
                vi.setBackgroundColor(0xFFBDBDBD);
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(tables.size()<=0)
        {
            holder.tableName.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            modelTable=null;
            modelTable = ( Tables ) tables.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.tableName.setText( modelTable.get_tableName() );
            holder.H_tableCustNum.setText( Integer.toString(modelTable.get_tableCustNum()) );
            holder.H_tableCustNum.setText( Integer.toString(modelTable.get_tableCustNum()));

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            TablesActivity sct = (TablesActivity) activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }

    @Override
    public void onClick(View view) {
        Log.v("CustomAdapter", "button clicked");
    }
}
