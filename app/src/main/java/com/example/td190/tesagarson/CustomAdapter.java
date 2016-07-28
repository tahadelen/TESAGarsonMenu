package com.example.td190.tesagarson;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.td190.tesagarson.Model.Tables;

import java.util.ArrayList;

/**
 * Created by td190 on 14/07/2016.
 */
public class CustomAdapter extends ArrayAdapter implements OnClickListener{

    private Context context;
    private Activity activity;
    private int layoutResourceId;
    private ArrayList<Tables> data = new ArrayList<>();

    public Resources res;

    public CustomAdapter(Activity _activity, Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.activity = _activity;
    }

    @Override
    public int getCount() {
        if(data.size() <= 0)
            return -1;
        else
            return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.tableName);
            holder.image = (ImageView) row.findViewById(R.id.tableStatus);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Tables item = data.get(position);
        holder.imageTitle.setText(item.get_tableName());
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(item.get_image(), 0, item.get_image().length));

        row.setOnClickListener(new OnItemClickListener(position));

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
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
