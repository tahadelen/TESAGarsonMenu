package com.example.td190.tesagarson;

/**
 * Created by td190 on 26/07/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.td190.tesagarson.Model.ChoosenProduct;

import java.util.ArrayList;

public class GridviewAdapter extends BaseAdapter{

    Context mContext;
    private ArrayList<ChoosenProduct> choices;
    private LayoutInflater mInflater;

    public GridviewAdapter(Context c, ArrayList<ChoosenProduct> d)
    {
        mContext=c;
        choices = d;
        mInflater = LayoutInflater.from(c);
    }

    public int getCount()
    {
        return choices.size();
    }
    public Object getItem(int position)
    {
        return position;
    }
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if(convertView==null)
        {
            convertView = mInflater.inflate(R.layout.customgrid, parent,false);
            holder = new ViewHolder();
            holder.product_name=(TextView)convertView.findViewById(R.id.product_name);
            holder.product_name.setPadding(100, 10,10 , 10);
            holder.product_piece=(TextView)convertView.findViewById(R.id.product_piece);
            holder.product_piece.setPadding(100, 10, 10, 10);
            holder.product_portion=(TextView)convertView.findViewById(R.id.product_portion);
            holder.product_portion.setPadding(100, 10, 10, 10);
            if(position==0)
            {
                convertView.setTag(holder);
            }
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.product_name.setText(choices.get(position).getProduct().get_productName());
        holder.product_piece.setText(Integer.toString(choices.get(position).getPiece()));
        holder.product_portion.setText(Double.toString(choices.get(position).getPortion()));

        return convertView;

    }
    static class ViewHolder
    {
        TextView product_name;
        TextView product_piece;
        TextView product_portion;
    }
}
